package edu.umass.cs.iesl.sbtbase

import sbt._
import sbt.Keys._
import scala.Some
import Config._
import io.{BufferedSource, Source}
import java.io.{IOException, FileWriter, BufferedWriter}

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

object IeslProject {

  sealed case class SnapshotsAllowedType()

  case object WithSnapshotDependencies extends SnapshotsAllowedType

  case object NoSnapshotDependencies extends SnapshotsAllowedType


  sealed case class RepoType()

  case object Public extends RepoType

  case object Private extends RepoType


  def apply(id: String, vers: String, deps: Seq[ModuleID],
            repotype: RepoType, allowSnapshots: SnapshotsAllowedType = NoSnapshotDependencies): Project =
    Project(id, file("."))
      .settings(scalaSettings: _*)
      .settings(resolvers ++= ((if (allowSnapshots == WithSnapshotDependencies) IESLSnapshotRepos else Seq.empty) ++ IESLReleaseRepos))
      .settings(getJarsTask)
      .settings(versionReportTask, versionUpdateReportTask, acceptVersionsTask) //, updateWithVersionReport)
      .settings(
      organization := iesl,
      version := vers,
      scalaVersion := scalaV,
      libraryDependencies ++= deps,
      publishToIesl(vers, repotype),
      creds)

  def publishToIesl(vers: String, repotype: RepoType) = publishTo := {
    def repo(name: String) = name at nexusUrl + "/content/repositories/" + name
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
    scalacOptions := Seq("-deprecation", "-unchecked", "-Xcheckinit", "-encoding", "utf8"),
    javacOptions ++= Seq("-Xlint:unchecked", "-encoding", "utf8")
  )

  // todo: use this mechanism to print the classpath to a file, to record what "latest.release" actually resolved to for each dependency in this build.

  val getJars = TaskKey[Unit]("get-jars")

  val getJarsTask = getJars <<= (target, fullClasspath in Runtime) map {
    (target, cp) =>
      println("Target path is: " + target)
      println("Full classpath is: " + cp.map(_.data).mkString(":"))
  }

  val versionReport = TaskKey[String]("version-report")

  // Add this setting to your project.
  val versionReportTask = versionReport <<= (externalDependencyClasspath in Compile, streams) map {
    (cp: Seq[Attributed[File]], streams) =>
      val report = cp.map {
        attributed =>
          attributed.get(Keys.moduleID.key) match {
            case Some(moduleId) => "%30s %30s %12s %8".format(
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

  val versionUpdateReportTask = versionUpdateReport <<= (externalDependencyClasspath in Compile, baseDirectory, streams) map doVersionUpdateReport

  val doVersionUpdateReport: (Seq[Attributed[sbt.File]], Types.Id[File], Types.Id[Keys.TaskStreams]) => Unit = (cp: Seq[Attributed[File]], baseDir, streams) => {
    val newVersions = cp.map {
      m =>
        m.get(Keys.moduleID.key) match {
          case Some(moduleId) => Some((moduleId.organization + ":" + moduleId.name, moduleId.revision))

          case None => None
        }
    }.flatten.toMap.withDefaultValue("(none)")

    val storedFile: Option[BufferedSource] = try {
      Some(Source.fromFile(baseDir + java.io.File.separator + "dependency-versions"))
    } catch {
      case e: IOException => None
    }
    val storedVersions: Map[String, String] = storedFile.map(_.getLines().map(_.trim).filter(!_.startsWith("#")).filter(!_.isEmpty).map(_.split("\t")).map(a => (a(0), a(1))).toMap).getOrElse(Map.empty[String, String]).withDefaultValue("(none)")

    val allDeps = newVersions.keySet ++ storedVersions.keySet

    val diffs = allDeps.map(k => (k, (storedVersions.apply(k), newVersions(k)))).filter(p => (p._2._1 != p._2._2))

    if (diffs.isEmpty) {
      streams.log.info("Dependency versions are up to date.")
    }
    else {
      streams.log.warn("Dependency versions have changed:")
      for (d <- diffs) {
        streams.log.warn("%40s : %14s => %14s".format(d._1, d._2._1, d._2._2))
      }
      streams.log.warn("Use 'accept-versions' to accept new versions.")
    }
  }

  lazy val acceptVersions = TaskKey[Unit]("accept-versions")

  val acceptVersionsTask = acceptVersions <<= (externalDependencyClasspath in Compile, baseDirectory, streams) map {
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

  /*
  val updateWithVersionReport = update <<= (update, externalDependencyClasspath in Compile, baseDirectory, streams) map {
    (u, cp: Seq[Attributed[File]], baseDir, streams) => {
      val result = u; doVersionUpdateReport(cp,baseDir,streams); result
    }
  }*/

  implicit def enrichProject(p: Project)(implicit allDeps: Dependencies): IeslProject = new IeslProject(p, allDeps)

}

class IeslProject(p: Project, allDeps: Dependencies) {
  def cleanLogging = p.settings(CleanLogging.cleanLogging)

  def standardLogging = p.settings(libraryDependencies ++= new CleanLogging(allDeps).standardLogging)
}
