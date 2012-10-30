import sbt._
import sbt.Keys._
import java.io.File

object IeslSbtBaseBuild extends Build {

  val iesl = "edu.umass.cs.iesl"
  val scalaV = "2.9.2"
  val vers = "30"

  val packageTemplate = TaskKey[File]("package-template")

  val packageTemplateTask = packageTemplate <<= (target, sourceDirectory) map {
    (target, sourceDirectory) =>
      import sys.process._
      val templatePackageFilename = target + "/iesl-sbt-base-template.tgz"
      val tarCommand = "tar cvzf " + templatePackageFilename + " iesl-sbt-base-template"
      println(tarCommand)
      val result = Process(tarCommand,sourceDirectory).!!

      println(result)
      println("Built: " + templatePackageFilename)
      new File(templatePackageFilename)
  }

  val templateArtifact = addArtifact(Artifact("template", "tgz", "tgz"), packageTemplate)

  lazy val ieslSbtBase =
    Project("iesl-sbt-base", file("."))
      .settings(
      sbtPlugin := true,
      organization := iesl,
      version := vers,
      scalaVersion := scalaV,
      publishToIesl(vers),
      creds).settings(packageTemplateTask.settings: _*).settings(templateArtifact.settings: _*)

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
