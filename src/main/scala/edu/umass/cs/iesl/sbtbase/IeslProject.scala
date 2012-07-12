package edu.umass.cs.iesl.sbtbase

import sbt._
import sbt.Keys._
import scala.Some
import Config._

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
    javacOptions ++= Seq("-Xlint:unchecked")
  )

  // todo: use this mechanism to print the classpath to a file, to record what "latest.release" actually resolved to for each dependency in this build.

  val getJars = TaskKey[Unit]("get-jars")

  val getJarsTask = getJars <<= (target, fullClasspath in Runtime) map { (target, cp) =>
    println("Target path is: "+target)
    println("Full classpath is: "+cp.map(_.data).mkString(":"))
  }
}
