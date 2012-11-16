package edu.umass.cs.iesl.sbtbase

import sbt.Keys._

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

object CleanLogging {

  // this method can be used as the globalModuleFilter argument to Dependencies.
  // but, it doesn't work!  The excluded dependencies are still included; don't know why.
  // Fall back to Ivy global excludes, which do work.

  /*
    // note these wildcard exclusions should catch the various classpath-hijacking "bridge" adapters from one framework to another
    val excludeLoggers: ModuleID => ModuleID = m => m
      .exclude("commons-logging", "commons-logging")
      .exclude("log4j", "log4j")
      .exclude("org.slf4j", "slf4j-log4j12")
    // todo exclude more?
    //  .excludeAll(new ExclusionRule(name = "*log4j*"))
    //  .excludeAll(new ExclusionRule(name = "*logback*"))
    //  .excludeAll(new ExclusionRule(name = "*slf4j*"))
  */


  val cleanLogging = ivyXML :=
    <dependencies>
      <exclude name="log4j.*" pattern="regexp"/>
      <exclude name="slf4j.*" pattern="regexp"/>
      <exclude name="slf4s.*" pattern="regexp"/>
      <exclude name="logback.*" pattern="regexp"/>
      <exclude name="commons-logging.*" pattern="regexp"/>
    </dependencies>
}

class CleanLogging(deps: Dependencies) {

  import deps._

  val standardLogging = Seq(
    // see http://www.slf4j.org/legacy.html

    // ultimately log everything via Logback

    logbackCore(),
    logbackClassic(),

    // use the slf4j wrapper API
    slf4j(),

    // nice Scala syntax for slf4j
    slf4s(),

    // direct legacy Jakarta Commons Logging calls to slf4j
    jclOverSlf4j(),

    // direct legacy log4j calls to slf4j
    log4jOverSlf4j(),

    // direct legacy java.util.logging calls to slf4j
    julToSlf4j(),

    // direct grizzled-slf4j calls to slf4j
    grizzledSlf4j()
  )

}
