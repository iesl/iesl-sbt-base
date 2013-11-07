/*
 * Copyright (c) 2013  University of Massachusetts Amherst
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.umass.cs.iesl.sbtbase

import sbt._
import Keys._

object CommonHelpers {

  def projectIn(path:String) = Project(path, file(path))

  def addDeps: (sbt.ModuleID*) => Project => Project =
    deps => proj => proj.settings(libraryDependencies ++= deps:_*)

  def set: (Seq[Project.Setting[_]]) => Project => Project =
    settings => proj => proj.settings(settings:_*)
}

object ShellPrompt {

  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }

  val current = """\*\s+([^\s]+)""".r

  def gitBranches = ("git branch --no-color" lines_! devnull mkString)
  def hgBranch = ("hg branch" lines_! devnull mkString)

  val buildShellPrompt = {
    (state: State) => {
      val currBranch = hgBranch
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (currBranch, currProject, "0.0")
      // "%s:%s:%s> ".format (currBranch, currProject, BuildSettings.buildVersion)
    }
  }
}





