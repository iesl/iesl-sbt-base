package edu.umass.cs.iesl.sbtbase

import sbt._

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

object Config {

  val iesl = "edu.umass.cs.iesl"
  val scalaV = "2.9.2"

  // any needed 3rd-party repositories should by proxied in Nexus and added to the public group, so their artifacts will be available here automatically.
  // Release and Snapshot repos are separated, to allow easily insuring that no snapshot dependencies exist (by default, in fact)

  val nexusUrl = "http://dev-iesl.cs.umass.edu/nexus"
  val nexusHttpsUrl = "https://dev-iesl.cs.umass.edu/nexus"

  val IESLReleaseRepos = Seq(
    "IESL Public Releases" at  nexusUrl + "/content/groups/public",
    "IESL Private Releases" at nexusUrl + "/content/repositories/private-releases")

  val IESLSnapshotRepos = Seq(
    "IESL Public Snapshots" at nexusUrl + "/content/groups/public-snapshots",
    "IESL Private Snapshots" at  nexusUrl + "content/repositories/private-snapshots")

  val publishRealm: String = "Sonatype Nexus Repository Manager"
  val publishHost: String = "dev-iesl.cs.umass.edu"
}
