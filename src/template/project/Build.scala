import sbt._
import edu.umass.cs.iesl.sbtbase.{Dependencies, IeslProject}
import edu.umass.cs.iesl.sbtbase.IeslProject._


// this is just an example, to show how simple a build can be once all the boilerplate stuff is factored out.

object ScalaCommonsBuild extends Build {

  val vers = "0.1-SNAPSHOT"

  implicit val allDeps: Dependencies = new Dependencies(); //(CleanLogging.excludeLoggers)  // doesn't work?

  import allDeps._

  val deps = Seq(
    dsutils(),
    commonsIo(),
    dispatchCore(),
    dispatchHttp(),
    classutil(),
    scalaCompiler(),
    scalatest(),
    scalazCore(),
    specs2(),
    scalaIoCore(),
    scalaIoFile(),
    jdom("1.1.3"),
    mavenCobertura(),
    mavenFindbugs())


  lazy val scalacommons = IeslProject("scalacommons", vers, deps, Public).cleanLogging.standardLogging

}
