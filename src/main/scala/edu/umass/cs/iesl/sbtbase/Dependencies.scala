package edu.umass.cs.iesl.sbtbase

import sbt._
import Config._

/**
 *
 * @param globalModuleFilter a function that is applied to each dependency ModuleID, e.g. to globally exclude certain transitive dependencies.
 */
class Dependencies(globalModuleFilter: ModuleID => ModuleID = (m:ModuleID)=>m) {

  implicit def enrichModuleID(m: ModuleID) = new RichModuleID(m)

  class RichModuleID(m: ModuleID) {
    def applyGlobal(): ModuleID = globalModuleFilter(m)
  }

  //def standardLogging = new CleanLogging(this).standardLogging

  def jsonic(v: String = "latest.release") = "net.arnx" % "jsonic" % v applyGlobal()

  def jclOverSlf4j(v: String = "latest.release") = "org.slf4j" % "jcl-over-slf4j" % v applyGlobal()

  def log4jOverSlf4j(v: String = "latest.release") = "org.slf4j" % "log4j-over-slf4j" % v applyGlobal()

  def julToSlf4j(v: String = "latest.release") = "org.slf4j" % "jul-to-slf4j" % v applyGlobal()

  def commonsVfs2(v: String = "latest.release") = "org.apache.commons" % "commons-vfs2" % v applyGlobal()

  def commonsCollections(v: String = "latest.release") = "commons-collections" % "commons-collections" % v applyGlobal()

  def commonsCompress(v: String = "latest.release") = "org.apache.commons" % "commons-compress" % v applyGlobal()

  def commonsLang(v: String = "latest.release") = "commons-lang" % "commons-lang" % v applyGlobal()

  def langdetect(v: String = "latest.release") = "com.cybozu.labs" % "langdetect" % v applyGlobal()

  def scalaCompiler(v: String = scalaV) = "org.scala-lang" % "scala-compiler" % v applyGlobal()

  def ieslScalaCommons(v: String = "latest.release") = "edu.umass.cs.iesl" %% "scalacommons" % v notTransitive() applyGlobal()//  exclude("com.davidsoergel", "dsutils")

  def namejuggler(v: String = "latest.release") = "edu.umass.cs.iesl" %% "namejuggler" % v applyGlobal()

  def bibmogrify(v: String = "latest.release") = "edu.umass.cs.iesl" %% "bibmogrify" % v applyGlobal()

  def pdf2meta(v: String = "latest.release") = "edu.umass.cs.iesl" %% "pdf2meta" % v applyGlobal()

  def pdfbox(v: String = "latest.release") = "org.apache.pdfbox" % "pdfbox" % v applyGlobal()

  def dsutils(v: String = "latest.release") = "com.davidsoergel" % "dsutils" % v applyGlobal()

  def classutil(v: String = "latest.release") = "org.clapper" %% "classutil" % v applyGlobal()

  def grizzledSlf4j(v: String = "latest.release") = "org.clapper" %% "grizzled-slf4j" % v applyGlobal()

  def slf4s(v: String = "latest.release") = "com.weiglewilczek.slf4s" % "slf4s_2.9.1" % v applyGlobal() //"slf4s_2.9.1"

  def scalaIoCore(v: String = "latest.release") = "com.github.scala-incubator.io" %% "scala-io-core" % v applyGlobal()

  def scalaIoFile(v: String = "latest.release") = "com.github.scala-incubator.io" %% "scala-io-file" % v applyGlobal()

  def typesafeConfig(v: String = "latest.release") = "com.typesafe" % "config" % v applyGlobal()

  def akkaActor(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-actor" % v applyGlobal()

  def akkaSlf4j(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-slf4j" % v applyGlobal()

  def akkaRemote(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-remote" % v applyGlobal()

  def boxterBrown(v: String = "latest.release") = "cc.acs" %% "boxter-brown" % v applyGlobal()

  def casbahLib(s: String)(v: String = "latest.release") = "com.mongodb.casbah" % ("casbah-" + s + "_" + scalaV) % v applyGlobal()

  def casbahLibs(v: String = "latest.release") = "core commons query".split(" ").toSeq map (l => casbahLib(l)(v))

  def commonsIo(v: String = "latest.release") = "commons-io" % "commons-io" % v applyGlobal()

  def dispatchCore(v: String = "latest.release") = "net.databinder" %% "dispatch-core" % v applyGlobal()

  def dispatchHttp(v: String = "latest.release") = "net.databinder" %% "dispatch-http" % v applyGlobal()

  def dispatchNio(v: String = "latest.release") = "net.databinder" %% "dispatch-nio" % v applyGlobal()

  def dispatchMime(v: String = "latest.release") = "net.databinder" %% "dispatch-mime" % v applyGlobal()

  def dispatchJson(v: String = "latest.release") = "net.databinder" %% "dispatch-json" % v applyGlobal()

  def h2(v: String = "latest.release") = "com.h2database" % "h2" % v % "compile" applyGlobal()

  def hibem(v: String = "latest.release") = "org.hibernate" % "hibernate-entitymanager" % v % "compile" applyGlobal()

  def hibval(v: String = "latest.release") = "org.hibernate" % "hibernate-validator-annotation-processor" % v % "compile" applyGlobal()

  // java simplified encryption
  def jasypt(v: String = "latest.release") = "org.jasypt" % "jasypt" % v applyGlobal()

  def jdom(v: String = "latest.release") = "org.jdom" % "jdom" % v applyGlobal()

  def mavenCobertura(v: String = "latest.release") = "org.codehaus.mojo" % "cobertura-maven-plugin" % v % "test" applyGlobal()

  def mavenFindbugs(v: String = "latest.release") = "org.codehaus.mojo" % "findbugs-maven-plugin" % v % "test" applyGlobal()

  def jaxen(v: String = "latest.release") = (("jaxen" % "jaxen" % v notTransitive() applyGlobal())
    .exclude("maven-plugins", "maven-cobertura-plugin")
    .exclude("maven-plugins", "maven-findbugs-plugin")
    .exclude("dom4j", "dom4j")
    .exclude("jdom", "jdom")
    .exclude("xml-apis", "xml-apis")
    .exclude("xerces", "xercesImpl")
    .exclude("xom", "xom"))

  def jettison(v: String = "latest.release") = "org.codehaus.jettison" % "jettison" % v applyGlobal()

  def jwebunit(v: String = "latest.release") = "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % v applyGlobal()

  def liftAmqp(v: String = "latest.release") = "net.liftweb" %% "lift-amqp" % v applyGlobal()

  def liftJson(v: String = "latest.release") = "net.liftweb" %% "lift-json" % v applyGlobal()

  def liftCouch(v: String = "latest.release") = "net.liftweb" %% "lift-couchdb" % v applyGlobal()

  def liftJpa(v: String = "latest.release") = "net.liftweb" %% "lift-jpa" % v applyGlobal()

  def liftJta(v: String = "latest.release") = "net.liftweb" %% "lift-jta" % v applyGlobal()

  def liftMachine(v: String = "latest.release") = "net.liftweb" %% "lift-machine" % v applyGlobal()

  def liftMapper(v: String = "latest.release") = "net.liftweb" %% "lift-mapper" % v applyGlobal()

  def liftMongo(v: String = "latest.release") = "net.liftweb" %% "lift-mongodb-record" % v applyGlobal()

  def liftPaypal(v: String = "latest.release") = "net.liftweb" %% "lift-paypal" % v applyGlobal()

  def liftScalate(v: String = "latest.release") = "net.liftweb" %% "lift-scalate" % v applyGlobal()

  def liftSqueryl(v: String = "latest.release") = "net.liftweb" %% "lift-squeryl-record" % v applyGlobal()

  def liftTestkit(v: String = "latest.release") = "net.liftweb" %% "lift-testkit" % v applyGlobal()

  def liftTextile(v: String = "latest.release") = "net.liftweb" %% "lift-textile" % v applyGlobal()

  def liftWebkit(v: String = "latest.release") = "net.liftweb" %% "lift-webkit" % v applyGlobal()

  def liftWidgets(v: String = "latest.release") = "net.liftweb" %% "lift-widgets" % v applyGlobal()

  def liftWizard(v: String = "latest.release") = "net.liftweb" %% "lift-wizard" % v applyGlobal()

  def mongodb(v: String = "latest.release") = "org.mongodb" % "mongo-java-driver" % v applyGlobal()

  def mysql(v: String = "latest.release") = "mysql" % "mysql-connector-java" % v applyGlobal()

  def neo4j(v: String = "latest.release") = "org.neo4j" % "neo4j" % v applyGlobal()

  def ostrich(v: String = "latest.release") = "com.twitter" % "ostrich" % v applyGlobal()

  //"9.0-801.jdbc4"
  def postgresql(v: String = "latest.release") = "postgresql" % "postgresql" % v applyGlobal()

  def redstoneXMLRPC(v: String = "latest.release") = "org.kohsuke.redstone" % "redstone" % v applyGlobal()

  def scalateCore(v: String = "latest.release") = "org.fusesource.scalate" % "scalate-core" % v applyGlobal()

  def scalazCore(v: String = "latest.release") = "org.scalaz" %% "scalaz-core" % v applyGlobal()

  def scalazTypelevel(v: String = "latest.release") = "org.scalaz" %% "scalaz-typelevel" % v applyGlobal()

  def scalaQuery(v: String = "latest.release") = "org.scalaquery" %% "scalaquery" % v applyGlobal()

  def selenium(v: String = "latest.release") = "org.seleniumhq.selenium" % "selenium-java" % v applyGlobal()

  def seleniumsvr(v: String = "latest.release") = "org.seleniumhq.selenium" % "selenium-server" % v applyGlobal()

  def shiroCore(v: String = "latest.release") = "org.apache.shiro" % "shiro-core" % v applyGlobal()

  def shiroWeb(v: String = "latest.release") = "org.apache.shiro" % "shiro-web" % v applyGlobal()

  def shiroEHCache(v: String = "latest.release") = "org.apache.shiro" % "shiro-ehcache" % v applyGlobal()

  def apacheCommonsLang3(v: String = "latest.release") = "org.apache.commons" % "commons-lang3" % v applyGlobal()

  def apacheCommonsEmail(v: String = "latest.release") = "org.apache.commons" % "commons-email" % v applyGlobal()

  def specs(v: String = "latest.release") = "org.scala-tools.testing" %% "specs" % v applyGlobal()

  def sprayJson(v: String = "latest.release") = "cc.spray" %% "spray-json" % v applyGlobal()

  def sprayServer(v: String = "latest.release") = "cc.spray" % "spray-server" % v applyGlobal()

  def slf4j(v: String = "latest.release") = "org.slf4j" % "slf4j-api" % v applyGlobal()

  def subcut(v: String = "latest.release") =  "org.scala-tools.subcut" % "subcut_2.9.1" % v applyGlobal()  // NEW once 2.0 is released "com.escalatesoft.subcut" %% "subcut" % v applyGlobal()

  def jacksonCore(v: String = "latest.release") = "org.codehaus.jackson" % "jackson-core-asl" % v applyGlobal()

  def bson4jackson(v: String = "latest.release") = "de.undercouch" % "bson4jackson" % v applyGlobal()

  def jerkson(v: String = "latest.release") = "com.codahale" %% "jerkson" % v applyGlobal()

  def unfiltered(v: String = "latest.release") = "net.databinder" %% "unfiltered" % v applyGlobal()

  def unfilteredScalatest(v: String = "latest.release") = "net.databinder" %% "unfiltered-scalatest" % v applyGlobal()

  def unfilteredSpec(v: String = "latest.release") = "net.databinder" %% "unfiltered-spec" % v applyGlobal()

  def unfilteredFilter(v: String = "latest.release") = "net.databinder" %% "unfiltered-filter" % v applyGlobal()

  def unfilteredJetty(v: String = "latest.release") = "net.databinder" %% "unfiltered-jetty" % v applyGlobal()

  def unfilteredNetty(v: String = "latest.release") = "net.databinder" %% "unfiltered-netty" % v applyGlobal()

  def unfilteredUploads(v: String = "latest.release") = "net.databinder" %% "unfiltered-uploads" % v applyGlobal()

  def activeMQ(v: String = "latest.release") = "org.apache.activemq" % "activemq-core" % v applyGlobal()

  def activeMQCamel(v: String = "latest.release") = "org.apache.activemq" % "activemq-camel" % v applyGlobal()

  def activeMQPool(v: String = "latest.release") = "org.apache.activemq" % "activemq-pool" % v applyGlobal()

  def camel(components: String*)(v: String = "latest.release"): Seq[ModuleID] = {
    val compNames = ("ahc,amqp,apns,atom,aws,bam,bean-validator,bindy,blueprint,buildtools,bundle,cache,castor,cometd,context,core,core-osgi,core-xml,crypto,csv,cxf,cxf-transport,dns,dozer,eclipse,ejb,eventadmin,exec,flatpack,freemarker,ftp,gae,groovy,guice,hamcrest,hawtdb,hazelcast,hdfs,hl7,http,http4,ibatis,irc,itest,itest-osgi,itest-spring-2.0,itest-spring-2.5,itest-standalone,jackson,jasypt,javaspace,jaxb,jclouds,jcr,jdbc,jdbc-aggregator,jetty,jhc,jibx,jing,jms,jmx,josql,jpa,jsch,jt400,juel,jxpath,kestrel,krati,ldap,lucene,mail,manual,maven-plugin,mina,mina2,msv,mvel,mybatis,nagios,netty,ognl,osgi,parent,partial-classpath-test,paxlogging,printer,protobuf,quartz,quickfix,rest,restlet,rmi,routebox,rss,ruby,saxon,scala,script,servlet,shiro,sip,smpp,snmp,soap,solr,spring,spring-integration,spring-javaconfig,spring-osgi,spring-security,spring-ws,sql,stax,stream,stringtemplate,swing,syslog,tagsoup,test,testng,typeconverterscan-test,uface,velocity,web,web-standalone,xmlbeans,xmlsecurity,xmpp,xstream,zookeeper"
      .split(",")
      .toSeq);

    def dep(c: String) = "org.apache.camel" % ("camel-" + c) % v applyGlobal()
    for {
      c <- components
    } yield {
      assert(compNames.contains(c))
      dep(c)
    }
  }

  // explicit version required to fix a bug in apache camel spring module
  def jaxbImpl(v: String = "2.2.6") = "com.sun.xml.bind" % "jaxb-impl" % v applyGlobal()

  def junit4(v: String = "latest.release") = "junit" % "junit" % v % "test" applyGlobal()

  def specs2(v: String = "latest.release") = "org.specs2" %% "specs2" % v % "test" applyGlobal()

  def scalacheck(v: String = "latest.release") = "org.scalacheck" %% "scalacheck" % v % "test" applyGlobal()

  def scalatest(v: String = "latest.release") = "org.scalatest" %% "scalatest" % v % "test" applyGlobal()

  def jettyWebApp(v: String = "latest.release") = "org.eclipse.jetty" % "jetty-webapp" % v % "container" applyGlobal()

  def jettyContainer(v: String = "latest.release") = "org.mortbay.jetty" % "jetty" % v % "container" applyGlobal()

  def jetty(v: String = "latest.release") = "org.mortbay.jetty" % "jetty" % v applyGlobal()

  def logbackClassic(v: String = "latest.release") = "ch.qos.logback" % "logback-classic" % v applyGlobal()

  def logbackCore(v: String = "latest.release") = "ch.qos.logback" % "logback-core" % v applyGlobal()

  def servletApi(v: String = "latest.release") = "javax.servlet" % "servlet-api" % v % "provided" applyGlobal()

  def jodatime(v: String = "latest.release") = "joda-time" % "joda-time" % v applyGlobal()
  
  def scalatime(v: String = "latest.release") = "org.scalaj" %% "scalaj-time" % v applyGlobal()

}
