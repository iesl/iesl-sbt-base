package edu.umass.cs.iesl.sbtbase

import sbt._
import sbt.Keys._
import scala.{xml, Some}
import Config._

import Scoped._

import io.{BufferedSource, Source}
import java.io.{IOException, FileWriter, BufferedWriter}
import xml.transform.{RewriteRule, RuleTransformer}
import xml.{Node => XNode, Text, Elem}

//import DependencyVersioning._

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

object IeslProject {

  implicit def enrichProject(p: Project)(implicit allDeps: Dependencies): IeslProject = new IeslProject(p, allDeps)

  sealed case class SnapshotsAllowedType()

  case object WithSnapshotDependencies extends SnapshotsAllowedType

  case object NoSnapshotDependencies extends SnapshotsAllowedType


  sealed case class RepoType()

  case object Public extends RepoType

  case object Private extends RepoType

  /*
    def apply(id: String, vers: String, deps: Seq[ModuleID],
              repotype: RepoType, allowSnapshots: SnapshotsAllowedType = NoSnapshotDependencies, path: String = ".", org: String = iesl, conflict: ConflictStrategy = ConflictStrict): Project =
      Project(id, file(path)).ieslSetup(vers, deps, repotype, allowSnapshots,org,conflict)
  */

  def publishToIesl(vers: String, repotype: RepoType) = publishTo := {
    def repo(name: String) = name at nexusHttpsUrl + "/content/repositories/" + name
    val isSnapshot = vers.endsWith("SNAPSHOT")
    val isPrivate = if (repotype == Private) "private-" else ""
    val repoName = isPrivate + (if (isSnapshot) "snapshots" else "releases")
    Some(repo(repoName))
  }

  val creds = credentials += {
    Seq("build.publish.user", "build.publish.password").map(k => Option(System.getProperty(k))) match {
      case Seq(Some(user), Some(pass)) =>
        Credentials(publishRealm, publishHost, user, pass)
      case _ =>
        Credentials(Path.userHome / ".ivy2" / ".credentials")
    }
  }

  val scalaSettings = Seq(
    scalaVersion := scalaV,
    scalacOptions := Seq("-Xlint", "-deprecation", "-unchecked", "-Xcheckinit", "-encoding", "utf8"),
    javacOptions ++= Seq("-Xlint:unchecked", "-encoding", "utf8")
  )

  val getJars = TaskKey[Unit]("get-jars")

  val getJarsTask = getJars <<= (target, fullClasspath in Runtime) map {
    (target, cp) =>
      println("Target path is: " + target)
      println("Full classpath is: " + cp.map(_.data).mkString(":"))
  }


  val allExternalDependencyClasspath = TaskKey[Seq[Attributed[File]]]("all-external-dependency-classpath")

  val allExternalDependencyClasspathTask = allExternalDependencyClasspath <<= (externalDependencyClasspath in Compile, externalDependencyClasspath in Test, externalDependencyClasspath in Runtime) map {
    (cpA: Seq[Attributed[File]], cpB: Seq[Attributed[File]], cpC: Seq[Attributed[File]]) => (cpA ++ cpB ++ cpC).toSet.toSeq
  }


  val versionReport = TaskKey[String]("version-report")

  // Add this setting to your project.
  val versionReportTask = versionReport <<= (allExternalDependencyClasspath, streams) map {
    (cp: Seq[Attributed[File]], streams) =>
      val report = cp.map {
        attributed =>
          attributed.get(Keys.moduleID.key) match {
            case Some(moduleId) => "%30s %30s %12s %8s".format(
              moduleId.organization,
              moduleId.name,
              moduleId.revision,
              moduleId.configurations.getOrElse("")
            )
            case None =>
              // unmanaged JAR, just
              attributed.data.getAbsolutePath
          }
      }.mkString("\n")
      streams.log.info(report)
      report
  }

  val versionUpdateReport = TaskKey[Unit]("version-update-report")

  val versionUpdateReportTask = versionUpdateReport <<= (allExternalDependencyClasspath, baseDirectory, streams) map doVersionUpdateReport


  def getStoredVersions(baseDir: File): Map[String, String] = {
    val storedFile: Option[BufferedSource] = try {
      //streams.log.info("Reading dependency versions file.")
      Some(Source.fromFile(baseDir + java.io.File.separator + "dependency-versions"))
    } catch {
      case e: IOException => None
    }
    val storedVersions: Map[String, String] = storedFile.map(_.getLines().map(_.trim).filter(!_.startsWith("#")).filter(!_.isEmpty).map(_.split("\t")).map(a => (a(0), a(1))).toMap).getOrElse(Map.empty[String, String]) //.withDefaultValue("(none)")
    storedVersions
  }

  def doVersionUpdateReport(cp: Seq[Attributed[File]], baseDir: File, streams: TaskStreams) {
    val newVersions = {

      streams.log.info("Collecting current dependency versions.")

      cp.map {
        m =>
          m.get(Keys.moduleID.key) match {
            case Some(moduleId) => Some((moduleId.organization + ":" + moduleId.name, moduleId.revision))

            case None => None
          }
      }.flatten.toMap //.withDefaultValue("(none)")
    }

    val storedVersions = getStoredVersions(baseDir)

    val allDeps = newVersions.keySet ++ storedVersions.keySet

    val diffs = allDeps.map(k => (k, (storedVersions.get(k), newVersions.get(k)))).filter(p => (p._2._1 != p._2._2))

    if (diffs.isEmpty) {
      streams.log.info("Dependency versions are up to date.")
    }
    else {
      streams.log.warn("Dependency versions have changed:")
      for (d <- diffs) {
        d match {
          case (key: String, (oldV: Option[String], newV: Option[String])) => {
            streams.log.warn("%40s : %14s => %14s".format(key, oldV.getOrElse("(none)"), newV.getOrElse("(none)")))
          }
        }
      }
      streams.log.warn("Use 'accept-versions' to accept new versions.")
    }
  }

  lazy val acceptVersions = TaskKey[Unit]("accept-versions")

  val acceptVersionsTask = acceptVersions <<= (allExternalDependencyClasspath, baseDirectory, streams) map {
    (cp: Seq[Attributed[File]], baseDir, streams) => {
      val writer: BufferedWriter = new BufferedWriter(new FileWriter(baseDir + java.io.File.separator + "dependency-versions"))
      writer.write("# automatically maintained; use 'sbt version-update-report' and 'sbt accept-versions'\n")

      cp.map {
        m =>
          m.get(Keys.moduleID.key) match {
            case Some(moduleId) => writer.write(moduleId.organization + ":" + moduleId.name + "\t" + moduleId.revision + "\n")

            case None => None
          }
      }

      writer.close()
      streams.log.info("Wrote updated dependency-versions file.")
    }
  }

  val updateWithVersionReport = update <<= (update, allExternalDependencyClasspath, baseDirectory, streams) map {
    (u, cp: Seq[Attributed[File]], baseDir, streams) => {
      val result = u
      //doVersionUpdateReport(cp, baseDir, streams)
      result
    }
  }


  // we use ivy dynamic versions like "latest.release", and those get passed through to the published pom.xml by
  // default, but then Maven doesn't understand them.  So we have to manually update the version numbers for each
  // dependency in the pom.
  // Because a SettingKey can't depend on a TaskKey, pomPostProcess can't depend on update.  So we have to use a
  // previously-written dependency-versions file.

  val fixPom = pomPostProcess <<= baseDirectory(LatestVersionUpdater)

  def LatestVersionUpdater(baseDir: File)(pom: XNode): XNode = {
    val storedVersions = getStoredVersions(baseDir)

    def replaceVersion(n: XNode, v: String) = n match {
      case Elem(prefix, label, attribs, scope, child@_*) =>
        Elem(prefix, label, attribs, scope, child.filter(_.label != "version") ++ <version>
          {v}
        </version>: _ *)
      case _ => sys.error("Can only add children to elements!")
    }

    class ReplaceVersions extends RewriteRule {
      override def transform(n: XNode) = n match {
        case n@Elem(_, "dependency", _, _, _*) => {
          val group = (n \ "groupId").text.trim
          val artifact = (n \ "artifactId").text.trim
          val newVersion = storedVersions.get(group + ":" + artifact)
          newVersion match {
            case Some(v) => replaceVersion(n, v)
            case None => {
              // where can I get a proper logger??
              System.err.println("No version found for " + group + ":" + artifact + "; excluding from pom.xml")
              System.err.println("   That may be OK if it was supposed to be excluded.")
              System.err.println("   Otherwise, run accept-versions and try again.")


              // drop the dependency entirely.
              new Text("")

            }
          }
        }
        case other => other
      }
    }

    val newXML = new RuleTransformer(new ReplaceVersions).transform(pom).head

    newXML
  }


  sealed class ConflictStrategy(s: String)

  case object ConflictAll extends ConflictStrategy("all")

  case object ConflictLatestTime extends ConflictStrategy("latest-time")

  case object ConflictLatestRevision extends ConflictStrategy("latest-revision")

  case object ConflictLatestCompatible extends ConflictStrategy("latest-compatible")

  case object ConflictStrict extends ConflictStrategy("strict")

  def setConflictStrategy(c: ConflictStrategy) = ivyXML :=
    <dependencies>
      <conflict>c.s</conflict>
    </dependencies>

  val splitOnEquals = "(.*)=(.*)".r

  def substituteLocalProjects(deps: Seq[ModuleID]): (Seq[RootProject], Seq[ModuleID]) = {

    val localOpt = Option(sys.props("localModules"))
    localOpt.map(local => {
      val localProjects = local.split(";").map(s => {
        val splitOnEquals(moduleId, path) = s.trim
        //(moduleId, new RootProject(file(path)))
        (moduleId.trim, RootProject(file(path.trim)))
      }).toMap

      val (l, r) = deps.partition(p => localProjects.contains(p.organization + ":" + p.name))

      val lProjects = l.map(p => localProjects(p.organization + ":" + p.name))
      (lProjects, r)
    }).getOrElse(Seq.empty, deps)
  }

}

class IeslProject(p: Project, allDeps: Dependencies) {
  def cleanLogging = p.settings(CleanLogging.cleanLogging)

  val standardLogging : Project = standardLogging("latest.release")
  def standardLogging(slf4jVersion:String="latest.release") : Project = p.settings(libraryDependencies ++= new CleanLogging(allDeps).standardLogging(slf4jVersion))

  import IeslProject._


  def ieslSetup(vers: String, deps: Seq[ModuleID],
                repotype: RepoType, allowSnapshots: SnapshotsAllowedType = NoSnapshotDependencies, org: String = iesl, conflict: ConflictStrategy = ConflictStrict): Project = {

    val (localDeps: Seq[RootProject], remoteDeps: Seq[ModuleID]) = substituteLocalProjects(deps)

    val result = p.settings(scalaSettings: _*)
      .settings(resolvers ++= ((if (allowSnapshots == WithSnapshotDependencies) IESLSnapshotRepos else Seq.empty) ++ IESLReleaseRepos))
      .settings(getJarsTask)
      .settings(versionReportTask, versionUpdateReportTask, acceptVersionsTask, fixPom, allExternalDependencyClasspathTask) //, updateWithVersionReport)
      .settings(
      organization := org,
      version := vers,
      scalaVersion := scalaV,
      libraryDependencies ++= remoteDeps,
      publishToIesl(vers, repotype),
      creds)
      .settings(setConflictStrategy(conflict))

    localDeps.foldLeft(result)((b, a) => b.dependsOn(a).aggregate(a))
  }

}
