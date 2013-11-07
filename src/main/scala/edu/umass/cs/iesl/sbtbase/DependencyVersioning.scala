/*
 * Copyright (c) 2013  University of Massachusetts Amherst
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.umass.cs.iesl.sbtbase

import sbt._
import sbt.Keys._
import scala.Some
import io.{BufferedSource, Source}
import java.io.{IOException, FileWriter, BufferedWriter}
import xml.transform.{RewriteRule, RuleTransformer}
import xml.{Node => XNode, Elem}

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

object DependencyVersioning {

val dependencyVersioningSettings = Seq(versionReportTask, versionUpdateReportTask, acceptVersionsTask, fixPomSetting) //, allExternalDependencyClasspathTask)

  /*
  val allExternalDependencyClasspath = TaskKey[Set[Attributed[File]]]("all-external-dependency-classpath")

  val allExternalDependencyClasspathTask = allExternalDependencyClasspath <<= (externalDependencyClasspath in Compile, externalDependencyClasspath in Test, externalDependencyClasspath in Runtime) map {
    (cpA: Seq[Attributed[File]], cpB: Seq[Attributed[File]], cpC: Seq[Attributed[File]]) => (cpA ++ cpB ++ cpC).toSet.toSeq
  }
*/

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


  //doVersionUpdateReport

  //private val doVersionUpdateReport: (Seq[Attributed[sbt.File]], Types.Id[File], Types.Id[Keys.TaskStreams]) => Unit =
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

  val updateWithVersionReport = update <<= (update, externalDependencyClasspath in Compile, baseDirectory, streams) map {
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

  val fixPomSetting = pomPostProcess <<= baseDirectory(LatestVersionUpdater)

  def LatestVersionUpdater(baseDir: File)(pom: XNode): XNode = {
    val storedVersions = getStoredVersions(baseDir)

    def replaceVersion(n: XNode, v: String) = n match {
      case Elem(prefix, label, attribs, scope, child@_*) =>
        Elem(prefix, label, attribs, scope, child.filter(_.label != "version") ++ <version>{v}</version>: _*)
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
              //** log properly and fail gracefully
              sys.error("No version found for " + group + ":" + artifact + ", run accept-versions")
            }
          }
        }
        case other => other
      }
    }

    val newXML = new RuleTransformer(new ReplaceVersions).transform(pom).head

    newXML
  }

}
