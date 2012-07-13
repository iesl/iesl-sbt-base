import sbt._
import edu.umass.cs.iesl.sbtbase.IeslProject
import edu.umass.cs.iesl.sbtbase.IeslProject._
import edu.umass.cs.iesl.sbtbase.CleanLogging
import edu.umass.cs.iesl.sbtbase.Dependencies

// this is just an example, to show how simple a build can be once all the boilerplate stuff is factored out.

object ScalaCommonsBuild extends Build {

  val vers = "0.1-SNAPSHOT"

  val allDeps = new Dependencies(CleanLogging.excludeLoggers)

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
    mavenFindbugs()) ++ standardLogging


  lazy val scalacommons = IeslProject("scalacommons", vers, deps, Public, WithSnapshotDependencies)  // replace with NoSnapshotDependencies, or just remove, to guarantee releases only

}
