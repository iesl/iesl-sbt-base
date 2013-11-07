/*
 * Copyright (c) 2013  University of Massachusetts Amherst
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import sbt._
import edu.umass.cs.iesl.sbtbase.Dependencies
import edu.umass.cs.iesl.sbtbase.IeslProject._


// this is just an example, to show how simple a build can be once all the boilerplate stuff is factored out.

object FooBarBuild extends Build {

  val vers = "0.1-SNAPSHOT"

  implicit val allDeps = new Dependencies()

  import allDeps._

  val deps = Seq(
    ieslScalaCommons("latest.integration"),
    scalaCompiler(),
    scalatest(),
    specs2(),
    scalaIoCore(),
    scalaIoFile(),
    jdom("1.1.3"))

  lazy val scalacommons = Project("foobar", file(".")).ieslSetup(vers, deps, Public, WithSnapshotDependencies).cleanLogging.standardLogging

}
