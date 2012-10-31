iesl-sbt-base
=============

_Finally, SBT is simple._

iesl-sbt-base is an [SBT](https://github.com/harrah/xsbt/wiki) plugin that provides a pile of boilerplate, so that the `Build.scala` file for your project can be trivially short.  It provides:

 * **Resolver configuration**
 * **Simplified dependency resolution with automatic updating**
 * **Clarity on what transitive dependencies are used**
 * **Optional protection from snapshot versions**
 * **Cleaned-up logging configuration**

A few of the features are specific to [IESL](http://iesl.cs.umass.edu) (in particular, it references our maven repositories), but these would be straightforward to factor out or change in a fork.

This is a complete, functional Build.scala using iesl-sbt-base:

    import sbt._
    import edu.umass.cs.iesl.sbtbase.{Dependencies, IeslProject}
    import edu.umass.cs.iesl.sbtbase.IeslProject._

    object FooBarBuild extends Build {
      implicit val allDeps: Dependencies = new Dependencies()
      import allDeps._

      val name = "foobar"
      val version = "0.1-SNAPSHOT"
      val deps = Seq(scalatest(),jdom("1.1.3"))

      lazy val foobar = IeslProject(name, version, deps, Public).cleanLogging.standardLogging
    }


Installation
============

The easiest way to start using iesl-sbt-base for a new project is to grab the template project [here](https://dev-iesl.cs.umass.edu/jenkins/job/iesl-sbt-base/lastSuccessfulBuild/artifact/target/iesl-sbt-base-template.tgz), or equivalently check out the repository and copy `/src/iesl-sbt-base-template`.  Then modify the `project/Build.scala` you find there.

The main point of the template is that it loads the iesl-sbt-plugin from our Maven repository (in `project/project/Plugins.scala`).  It also provides a standard and fairly complete `.hgignore` file, as well as a stub `logback.xml` logging configuration (see below).

To add the plugin manually (i.e., to an existing project), add these to `project/plugins.sbt`:

    resolvers += Resolver.url("IESL Public Releases", url("https://dev-iesl.cs.umass.edu/nexus/content/groups/public"))

    addSbtPlugin("edu.umass.cs.iesl" %% "iesl-sbt-base" % "latest.release")





Artifact repositories
=====================


All downloads routed through IESL resolvers
-------------------------------------------

iesl-sbt-base provides artifact resolvers for four IESL repositories: {releases, snapshots} x {public, private}.

Ideally, do not refer to any additional non-IESL repositories in your sbt build files.  The public URLs in fact refer to repository groups, which proxy a list of remote repositories which contain all the jars you are likely to want.  By routing all requests through dev-iesl, any remote artifact that you need will be locally cached, so your project can always be built even if the remote repo goes down, or stops providing the artifact.  (If you want something from a remote repo that dev-iesl doesn't already provide, just ask us to add the desired remote repo to the proxy list.)

Uploads routed to appropriate repositories
------------------------------------------

When using `sbt publish` (or ideally, when [Jenkins](http://dev-iesl.cs.umass.edu) publishes your project for you), iesl-sbt-base chooses the appropriate repository, depending on whether the build is a release or a snapshot, and on whether it is public or private (i.e., available only to IESL users with login credentials for our private Maven repository).  SNAPSHOT status is simply determined from the version string, and "public" status is specified as an argument to the `IeslProject` constructor.



Dependency resolution
=====================


Shorthand dependency names
--------------------------

The `Dependencies` object lists a lot of packages that we use under simple names to reduce clutter in your configuration.  That's why you can say `scalatest()` above, instead of `"org.scalatest" %% "scalatest" % "latest.release" % "test"`.  Please let us know of additional packages that you would like to be listed here.  You can always use the full dependency form too, of course.

Up-to-date dependencies
-----------------------

By default, iesl-sbt-base obtains the latest release version of each dependency.  This is important for testing that your code continues to work when the dependencies are updated.  Depedency resolvers such as Maven and Ivy generally assume that later versions of packages are backwards-compatible--at least, when the major version number has not changed--and so will tend to prefer more recent versions in the event that transitive dependency resolution results in requests for multiple versions.  Thus, a downstream user may well try to use your package with a more recent version of some dependency than you used to develop it.  So my approach is just to stay up to date across the board.

This behavior is accomplished by setting the default version of each dependency to `latest.release`.  If you do need a specific version of something, just include the version string, e.g.: `jdom("1.1.3")`

Updates to iesl-sbt-base itself
-------------------------------

iesl-sbt-base versions are always releases with integer version numbers (21 presently), never snapshots.  The plugin loader is set to automatically stay up to date with the latest version (it requests `latest.release`).  However, my impression is that `sbt update` at the project level does not update the plugins.  You can easily force it with `sbt clean-plugins`.

Listing dependency versions
---------------------------

iesl-sbt-base provides the `version-report` task, which prints a list of all the jars on the current project classpath (including transitive dependencies) and their version numbers.  Especially in the event of confusions regarding transitive dependencies, it can be very helpful to see which artifacts actually get loaded.


Updating dependencies to the latest version
-------------------------------------------

In principle `sbt update` should obtain the latest releases of upstream dependencies.  However, there seems to be some inconsistent behavior in this regard among recent versions of sbt.  `sbt clean; sbt update` seems to work more reliably.

Using or prohibiting SNAPSHOT versions
--------------------------------------

By default, iesl-sbt-base does not configure resolvers for our snapshot repositories.  It is more robust to develop against release versions of dependencies if at all possible; simply refusing to download artifacts from snapshot repositories is a nice centralized way to enforce this principle.  If you do need snapshot dependencies, simply set the optional fifth argument to `IeslProject` to enable the resolvers:

    lazy val foobar = IeslProject("foobar", version, deps, Public, WithSnapshotDependencies)

To refer to a specific snapshot version, just set it in the usual way, e.g: `jdom("1.1.4-SNAPSHOT")`. To specify that the most recent snapshot should be used, set the version string to `latest.integration`.

Generated Maven poms
--------------------

One thing that can be confusing about publishing SBT-based projects is that they must generate a `pom.xml` file in order to be uploaded to a Maven repository.  That is: although SBT dependency resolution is actually done by Ivy under the hood, the Maven repository format is the accepted standard, so we have to conform to it.  A hiccup is that Maven does not provide options like Ivy's `latest.release` and `latest.integration`; Maven `pom.xml` files always list a specific version.  Thus the usual case is that poms generated from SBT projects using that feature do not work.

iesl-sbt-base overcomes this limitation by automatically fixing the dependency version numbers in generated poms.  This takes the form of two tasks:

 * `sbt accept-versions` records the current versions of dependencies in a `dependency-versions` file at the root of your project.  Whenever a pom.xml file is generated, the versions listed in this file are used.
 * `sbt version-update-report` lists any discrepancies between the recorded version numbers and those actually in use (i.e., that are listed with `sbt version-report`).  This makes it easy to see when upstream dependencies have been updated.  If the updates are OK, simply run `sbt accept-versions` again to record them.

 It is a good practice to add the `dependency-versions` file to revision control; that way other users of your code can easily see the latest dependency versions that were accepted.  This may help track down bugs resulting from updated dependencies.

 Adding the file to revision control may be useful when using a build server like Jenkins, because in this case the `pom.xml` generation occurs on the server.  Alternatively, you can ensure that the Jenkins build runs the `accept-versions` task before publishing.



Clean logging
=============

The proliferation of logging packages in the Java ecosystem has produced widespread confusion, especially because they often load whatever configurations and components they find on the classpath at runtime.  If your project depends on other projects that use different loggers (e.g. log4j, commons-logging, java.util.logging, etc), then--unless you're very careful--all bets are off as to which log messages you will see, which configuration files will be read, where the log messages will be written, etc.

iesl-sbt-base solves this problem by allowing you to forcibly exclude all transitive dependencies on logging packages, by appending `.cleanLogging` to your project definition.  You can then use `.standardLogging` to add a dependency to the now-standard [Logback](http://logback.qos.ch), together with bridges that intercept logging calls written against other APIs.

The upshot is that **all log messages--even those generated by upstream dependencies, possibly targeted at different logging packages--are routed through Logback, so log levels and destinations can be controlled from a single config file**.  By default, Logback configures itself using the first instance of `/logback.xml` found on the classpath.  If for some reason there is more than one such file (i.e., some dependency provides one too) then Logback should complain about that.

To avoid any ambiguity about which configuration file is loaded, and to make log levels editable at runtime even when your program is packaged in a jar, it's best to explicitly provide a `logback.xml` via a Java system property, like this:

    java -Dlogback.configurationFile=/home/soergel/.bibmogrify/logback.xml ...

`standardLogging` also includes [slf4j](http://www.slf4j.org), which provides a consistent API aiming to alleviate this problem in the future, and the [slf4s](https://github.com/weiglewilczek/slf4s) Scala wrappers for it.  You can thus write log messages like this:

    import com.weiglewilczek.slf4s.Logging

    object Hello extends Logging {
      def hello = logger.debug("Hello World")
      }
      
Substituting local dependencies for remote ones
===============================================

Sometimes it can be useful to reference a local project directory for some external dependency, instead of referring to it as a jar using the usual Ivy mechanism.  To perform such a substitution, use the following system property when running sbt:

    -DlocalModules=com.foobar:baz=/path/to/com.foobar/baz

(In this case the requested version number (and/or SNAPSHOT status) of the com.foobar:baz module will be ignored.)

Questions and feedback
======================

Please contact David Soergel <soergel@cs.umass.edu>.
