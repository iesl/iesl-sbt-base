import sbt._
import sbt.Keys._

object IeslSbtBaseBuild extends Build {

  val iesl = "edu.umass.cs.iesl"
  val scalaV = "2.9.1"
  val vers = "16"

  lazy val ieslSbtBase =
    Project("iesl-sbt-base", file("."))
      .settings(
      sbtPlugin := true,
      organization := iesl,
      version := vers,
      scalaVersion := scalaV,
      publishToIesl(vers),
      creds)

  def publishToIesl(vers: String) = publishTo := {
    def repo(name: String) = name at "https://dev-iesl.cs.umass.edu/nexus/content/repositories/" + name
    val isSnapshot = vers.endsWith("SNAPSHOT")
    val repoName = (if (isSnapshot) "snapshots" else "releases")
    Some(repo(repoName))
  }

  val creds = credentials += {
    Seq("build.publish.user", "build.publish.password").map(k => Option(System.getProperty(k))) match {
      case Seq(Some(user), Some(pass)) =>
        Credentials("Sonatype Nexus Repository Manager", "dev-iesl.cs.umass.edu", user, pass)
      case _ =>
        Credentials(Path.userHome / ".ivy2" / ".credentials")
    }
  }


}
