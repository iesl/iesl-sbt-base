package edu.umass.cs.iesl.sbtbase

import sbt._
import Config._

object Dependencies {

  implicit def enrichModuleID(m: ModuleID) = new RichModuleID(m)

  class RichModuleID(m: ModuleID) {
    def excludeLoggers: ModuleID = m exclude("commons-logging", "commons-logging") exclude("ch.qos.logback", "logback-classic") exclude("ch.qos.logback", "logback-core") exclude("log4j", "log4j")
  }


  def jsonic(v: String = "latest.release") = "net.arnx" % "jsonic" % v excludeLoggers()

  def jclOverSlf4j(v: String = "latest.release") = "org.slf4j" % "jcl-over-slf4j" % v excludeLoggers()

  def commonsVfs2(v: String = "latest.release") = "org.apache.commons" % "commons-vfs2" % v excludeLoggers()

  def commonsCollections(v: String = "latest.release") = "commons-collections" % "commons-collections" % v excludeLoggers()

  def commonsCompress(v: String = "latest.release") = "org.apache.commons" % "commons-compress" % v excludeLoggers()

  def commonsLang(v: String = "latest.release") = "commons-lang" % "commons-lang" % v excludeLoggers()

  def langdetect(v: String = "latest.release") = "com.cybozu.labs" % "langdetect" % v excludeLoggers()

  def scalaCompiler(v: String = scalaV) = "org.scala-lang" % "scala-compiler" % v excludeLoggers()

  def ieslScalaCommons(v: String = "latest.release") = "edu.umass.cs.iesl" %% "scalacommons" % v notTransitive() //  exclude("com.davidsoergel", "dsutils")

  def bibmogrify(v: String = "latest.release") = "edu.umass.cs.iesl" %% "bibmogrify" % v excludeLoggers()

  def pdf2meta(v: String = "latest.release") = "edu.umass.cs.iesl" %% "pdf2meta" % v excludeLoggers()

  def pdfbox(v: String = "latest.release") = "org.apache.pdfbox" % "pdfbox" % v excludeLoggers()

  def dsutils(v: String = "latest.release") = "com.davidsoergel" % "dsutils" % v excludeLoggers()

  def classutil(v: String = "latest.release") = "org.clapper" %% "classutil" % v excludeLoggers()

  def slf4s(v: String = "latest.release") = "com.weiglewilczek.slf4s" %% "slf4s" % v //"slf4s_2.9.1"

  def scalaIoCore(v: String = "latest.release") = "com.github.scala-incubator.io" %% "scala-io-core" % v excludeLoggers()

  def scalaIoFile(v: String = "latest.release") = "com.github.scala-incubator.io" %% "scala-io-file" % v excludeLoggers()

  def akkaActor(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-actor" % v excludeLoggers()

  def akkaSlf4j(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-slf4j" % v excludeLoggers()

  def akkaRemote(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-remote" % v excludeLoggers()

  def boxterBrown(v: String = "latest.release") = "cc.acs" %% "boxter-brown" % v excludeLoggers()

  def casbahLib(s: String)(v: String = "latest.release") = "com.mongodb.casbah" % ("casbah-" + s + "_" + scalaV) % v excludeLoggers()

  def casbahLibs(v: String = "latest.release") = "core commons query".split(" ").toSeq map (l => casbahLib(l)(v))

  def commonsIo(v: String = "latest.release") = "commons-io" % "commons-io" % v excludeLoggers()

  def dispatchCore(v: String = "latest.release") = "net.databinder" %% "dispatch-core" % v excludeLoggers()

  def dispatchHttp(v: String = "latest.release") = "net.databinder" %% "dispatch-http" % v excludeLoggers()

  def dispatchNio(v: String = "latest.release") = "net.databinder" %% "dispatch-nio" % v excludeLoggers()

  def dispatchMime(v: String = "latest.release") = "net.databinder" %% "dispatch-mime" % v excludeLoggers()

  def dispatchJson(v: String = "latest.release") = "net.databinder" %% "dispatch-json" % v excludeLoggers()

  def h2(v: String = "latest.release") = "com.h2database" % "h2" % v % "compile" excludeLoggers()

  def hibem(v: String = "latest.release") = "org.hibernate" % "hibernate-entitymanager" % v % "compile" excludeLoggers()

  def hibval(v: String = "latest.release") = "org.hibernate" % "hibernate-validator-annotation-processor" % v % "compile" excludeLoggers()

  // java simplified encryption
  def jasypt(v: String = "latest.release") = "org.jasypt" % "jasypt" % v excludeLoggers()

  def jdom(v: String = "latest.release") = "org.jdom" % "jdom" % v excludeLoggers()

  def mavenCobertura(v: String = "latest.release") = "org.codehaus.mojo" % "cobertura-maven-plugin" % v % "test" excludeLoggers()

  def mavenFindbugs(v: String = "latest.release") = "org.codehaus.mojo" % "findbugs-maven-plugin" % v % "test" excludeLoggers()

  def jaxen(v: String = "latest.release") = (("jaxen" % "jaxen" % v notTransitive())
    .exclude("maven-plugins", "maven-cobertura-plugin")
    .exclude("maven-plugins", "maven-findbugs-plugin")
    .exclude("dom4j", "dom4j")
    .exclude("jdom", "jdom")
    .exclude("xml-apis", "xml-apis")
    .exclude("xerces", "xercesImpl")
    .exclude("xom", "xom"))

  def jettison(v: String = "latest.release") = "org.codehaus.jettison" % "jettison" % v excludeLoggers()

  def jwebunit(v: String = "latest.release") = "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % v excludeLoggers()

  def liftAmqp(v: String = "latest.release") = "net.liftweb" %% "lift-amqp" % v excludeLoggers()

  def liftJson(v: String = "latest.release") = "net.liftweb" %% "lift-json" % v excludeLoggers()

  def liftCouch(v: String = "latest.release") = "net.liftweb" %% "lift-couchdb" % v excludeLoggers()

  def liftJpa(v: String = "latest.release") = "net.liftweb" %% "lift-jpa" % v excludeLoggers()

  def liftJta(v: String = "latest.release") = "net.liftweb" %% "lift-jta" % v excludeLoggers()

  def liftMachine(v: String = "latest.release") = "net.liftweb" %% "lift-machine" % v excludeLoggers()

  def liftMapper(v: String = "latest.release") = "net.liftweb" %% "lift-mapper" % v excludeLoggers()

  def liftMongo(v: String = "latest.release") = "net.liftweb" %% "lift-mongodb-record" % v excludeLoggers()

  def liftPaypal(v: String = "latest.release") = "net.liftweb" %% "lift-paypal" % v excludeLoggers()

  def liftScalate(v: String = "latest.release") = "net.liftweb" %% "lift-scalate" % v excludeLoggers()

  def liftSqueryl(v: String = "latest.release") = "net.liftweb" %% "lift-squeryl-record" % v excludeLoggers()

  def liftTestkit(v: String = "latest.release") = "net.liftweb" %% "lift-testkit" % v excludeLoggers()

  def liftTextile(v: String = "latest.release") = "net.liftweb" %% "lift-textile" % v excludeLoggers()

  def liftWebkit(v: String = "latest.release") = "net.liftweb" %% "lift-webkit" % v excludeLoggers()

  def liftWidgets(v: String = "latest.release") = "net.liftweb" %% "lift-widgets" % v excludeLoggers()

  def liftWizard(v: String = "latest.release") = "net.liftweb" %% "lift-wizard" % v excludeLoggers()

  def mongodb(v: String = "latest.release") = "org.mongodb" % "mongo-java-driver" % v excludeLoggers()

  def mysql(v: String = "latest.release") = "mysql" % "mysql-connector-java" % v excludeLoggers()

  def neo4j(v: String = "latest.release") = "org.neo4j" % "neo4j" % v excludeLoggers()

  def ostrich(v: String = "latest.release") = "com.twitter" % "ostrich" % v excludeLoggers()

  //"9.0-801.jdbc4"
  def postgresql(v: String = "latest.release") = "postgresql" % "postgresql" % v excludeLoggers()

  def redstoneXMLRPC(v: String = "latest.release") = "org.kohsuke.redstone" % "redstone" % v excludeLoggers()

  def scalateCore(v: String = "latest.release") = "org.fusesource.scalate" % "scalate-core" % v excludeLoggers()

  def scalazCore(v: String = "latest.release") = "org.scalaz" %% "scalaz-core" % v excludeLoggers()

  def scalaQuery(v: String = "latest.release") = "org.scalaquery" %% "scalaquery" % v excludeLoggers()

  def selenium(v: String = "latest.release") = "org.seleniumhq.selenium" % "selenium-java" % v excludeLoggers()

  def seleniumsvr(v: String = "latest.release") = "org.seleniumhq.selenium" % "selenium-server" % v excludeLoggers()

  def shiroCore(v: String = "latest.release") = "org.apache.shiro" % "shiro-core" % v excludeLoggers()

  def shiroWeb(v: String = "latest.release") = "org.apache.shiro" % "shiro-web" % v excludeLoggers()

  def shiroEHCache(v: String = "latest.release") = "org.apache.shiro" % "shiro-ehcache" % v excludeLoggers()

  def apacheCommonsLang3(v: String = "latest.release") = "org.apache.commons" % "commons-lang3" % v excludeLoggers()

  def apacheCommonsEmail(v: String = "latest.release") = "org.apache.commons" % "commons-email" % v excludeLoggers()

  def specs(v: String = "latest.release") = "org.scala-tools.testing" %% "specs" % v excludeLoggers()

  def sprayJson(v: String = "latest.release") = "cc.spray" %% "spray-json" % v excludeLoggers()

  def sprayServer(v: String = "latest.release") = "cc.spray" % "spray-server" % v excludeLoggers()

  def slf4j(v: String = "latest.release") = "org.slf4j" % "slf4j-api" % v excludeLoggers()

  def subcut(v: String = "latest.release") = "org.scala-tools.subcut" %% "subcut" % v excludeLoggers()

  def jacksonCore(v: String = "latest.release") = "org.codehaus.jackson" % "jackson-core-asl" % v excludeLoggers()

  def bson4jackson(v: String = "latest.release") = "de.undercouch" % "bson4jackson" % v excludeLoggers()

  def jerkson(v: String = "latest.release") = "com.codahale" %% "jerkson" % v excludeLoggers()

  def unfiltered(v: String = "latest.release") = "net.databinder" %% "unfiltered" % v excludeLoggers()

  def unfilteredScalatest(v: String = "latest.release") = "net.databinder" %% "unfiltered-scalatest" % v excludeLoggers()

  def unfilteredSpec(v: String = "latest.release") = "net.databinder" %% "unfiltered-spec" % v excludeLoggers()

  def unfilteredFilter(v: String = "latest.release") = "net.databinder" %% "unfiltered-filter" % v excludeLoggers()

  def unfilteredJetty(v: String = "latest.release") = "net.databinder" %% "unfiltered-jetty" % v excludeLoggers()

  def unfilteredNetty(v: String = "latest.release") = "net.databinder" %% "unfiltered-netty" % v excludeLoggers()

  def unfilteredUploads(v: String = "latest.release") = "net.databinder" %% "unfiltered-uploads" % v excludeLoggers()

  def activeMQ(v: String = "latest.release") = "org.apache.activemq" % "activemq-core" % v excludeLoggers()

  def activeMQCamel(v: String = "latest.release") = "org.apache.activemq" % "activemq-camel" % v excludeLoggers()

  def activeMQPool(v: String = "latest.release") = "org.apache.activemq" % "activemq-pool" % v excludeLoggers()

  def camel(components: String*)(v: String = "latest.release"): Seq[ModuleID] = {
    val compNames = ("ahc,amqp,apns,atom,aws,bam,bean-validator,bindy,blueprint,buildtools,bundle,cache,castor,cometd,context,core,core-osgi,core-xml,crypto,csv,cxf,cxf-transport,dns,dozer,eclipse,ejb,eventadmin,exec,flatpack,freemarker,ftp,gae,groovy,guice,hamcrest,hawtdb,hazelcast,hdfs,hl7,http,http4,ibatis,irc,itest,itest-osgi,itest-spring-2.0,itest-spring-2.5,itest-standalone,jackson,jasypt,javaspace,jaxb,jclouds,jcr,jdbc,jdbc-aggregator,jetty,jhc,jibx,jing,jms,jmx,josql,jpa,jsch,jt400,juel,jxpath,kestrel,krati,ldap,lucene,mail,manual,maven-plugin,mina,mina2,msv,mvel,mybatis,nagios,netty,ognl,osgi,parent,partial-classpath-test,paxlogging,printer,protobuf,quartz,quickfix,rest,restlet,rmi,routebox,rss,ruby,saxon,scala,script,servlet,shiro,sip,smpp,snmp,soap,solr,spring,spring-integration,spring-javaconfig,spring-osgi,spring-security,spring-ws,sql,stax,stream,stringtemplate,swing,syslog,tagsoup,test,testng,typeconverterscan-test,uface,velocity,web,web-standalone,xmlbeans,xmlsecurity,xmpp,xstream,zookeeper"
      .split(",")
      .toSeq);

    def dep(c: String) = "org.apache.camel" % ("camel-" + c) % v excludeLoggers()
    for {
      c <- components
    } yield {
      assert(compNames.contains(c))
      dep(c)
    }
  }


  def junit4(v: String = "latest.release") = "junit" % "junit" % v % "test" excludeLoggers()

  def specs2(v: String = "latest.release") = "org.specs2" %% "specs2" % v % "test" excludeLoggers()

  def scalacheck(v: String = "latest.release") = "org.scala-tools.testing" %% "scalacheck" % v % "test" excludeLoggers()

  def scalatest(v: String = "latest.release") = "org.scalatest" %% "scalatest" % v % "test" excludeLoggers()

  def jettyWebApp(v: String = "latest.release") = "org.eclipse.jetty" % "jetty-webapp" % v % "container" excludeLoggers()

  def jettyContainer(v: String = "latest.release") = "org.mortbay.jetty" % "jetty" % v % "container" excludeLoggers()

  def jetty(v: String = "latest.release") = "org.mortbay.jetty" % "jetty" % v excludeLoggers()

  def logbackClassic(v: String = "latest.release") = "ch.qos.logback" % "logback-classic" % v excludeLoggers()

  def logbackCore(v: String = "latest.release") = "ch.qos.logback" % "logback-core" % v excludeLoggers()

  def servletApi(v: String = "latest.release") = "javax.servlet" % "servlet-api" % v % "provided" excludeLoggers()


}
