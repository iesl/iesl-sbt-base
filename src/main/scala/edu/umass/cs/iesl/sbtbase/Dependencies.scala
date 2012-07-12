package edu.umass.cs.iesl.sbtbase

import sbt._
import Config._

object Dependencies {


  def jsonic(v: String = "latest.release") = "net.arnx" % "jsonic" % v

  def jclOverSlf4j(v: String = "latest.release") = "org.slf4j" % "jcl-over-slf4j" % v

  def commonsVfs2(v: String = "latest.release") ="org.apache.commons" % "commons-vfs2" % v exclude("commons-logging", "commons-logging")

  def commonsCollections(v: String = "latest.release") = "commons-collections" % "commons-collections" % v

  def commonsCompress(v: String = "latest.release") = "org.apache.commons" % "commons-compress" % v

  def commonsLang(v: String = "latest.release") = "commons-lang" % "commons-lang" % v

  def langdetect(v: String = "latest.release") = "com.cybozu.labs" % "langdetect" % v

  def scalaCompiler(v: String = scalaV) = "org.scala-lang" % "scala-compiler" % v

  def ieslScalaCommons(v: String = "latest.release") = "edu.umass.cs.iesl" %% "scalacommons" % v notTransitive() //  exclude("com.davidsoergel", "dsutils")

  def dsutils(v: String = "latest.release") = "com.davidsoergel" % "dsutils" % v exclude("commons-logging", "commons-logging")

  def classutil(v: String = "latest.release") = "org.clapper" %% "classutil" % v

  def slf4s(v: String = "latest.release") = "com.weiglewilczek.slf4s" %% "slf4s" % v //"slf4s_2.9.1"

  def scalaIoCore(v: String = "latest.release") = "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.0"

  def scalaIoFile(v: String = "latest.release") = "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.0"

  def akkaActor(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-actor" % v

  def akkaSlf4j(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-slf4j" % v

  def akkaRemote(v: String = "latest.release") = "se.scalablesolutions.akka" % "akka-remote" % v

  def boxterBrown(v: String = "latest.release") = "cc.acs" %% "boxter-brown" % "0.1-SNAPSHOT"

  def casbahLib(s: String)(v: String = "latest.release") = "com.mongodb.casbah" % ("casbah-" + s + "_2.9.0-1") % v

  def casbahLibs(v: String = "latest.release") = "core commons query".split(" ").toSeq map (l => casbahLib(l)(v))

  def commonsIo(v: String = "latest.release") = "commons-io" % "commons-io" % v

  def dispatchCore(v: String = "latest.release") = "net.databinder" %% "dispatch-core" % v

  def dispatchHttp(v: String = "latest.release") = "net.databinder" %% "dispatch-http" % v

  def dispatchNio(v: String = "latest.release") = "net.databinder" %% "dispatch-nio" % v

  def dispatchMime(v: String = "latest.release") = "net.databinder" %% "dispatch-mime" % v

  def dispatchJson(v: String = "latest.release") = "net.databinder" %% "dispatch-json" % v

  def h2(v: String = "latest.release") = "com.h2database" % "h2" % v % "compile"

  def hibem(v: String = "latest.release") = "org.hibernate" % "hibernate-entitymanager" % v % "compile"

  def hibval(v: String = "latest.release") = "org.hibernate" % "hibernate-validator-annotation-processor" % v % "compile"

  // java simplified encryption
  def jasypt(v: String = "latest.release") = "org.jasypt" % "jasypt" % v

  def jdom(v: String = "latest.release") = "org.jdom" % "jdom" % v

  def mavenCobertura(v: String = "latest.release") = "org.codehaus.mojo" % "cobertura-maven-plugin" % v % "test"

  def mavenFindbugs(v: String = "latest.release") = "org.codehaus.mojo" % "findbugs-maven-plugin" % v % "test"

  def jaxen(v: String = "latest.release") = (("jaxen" % "jaxen" % v notTransitive())
    .exclude("maven-plugins", "maven-cobertura-plugin")
    .exclude("maven-plugins", "maven-findbugs-plugin")
    .exclude("dom4j", "dom4j")
    .exclude("jdom", "jdom")
    .exclude("xml-apis", "xml-apis")
    .exclude("xerces", "xercesImpl")
    .exclude("xom", "xom"))

  def jettison(v: String = "latest.release") = "org.codehaus.jettison" % "jettison" % v

  def jwebunit(v: String = "latest.release") = "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % v

  def liftAmqp(v: String = "latest.release") = "net.liftweb" %% "lift-amqp" % v

  def liftJson(v: String = "latest.release") = "net.liftweb" %% "lift-json" % v

  def liftCouch(v: String = "latest.release") = "net.liftweb" %% "lift-couchdb" % v

  def liftJpa(v: String = "latest.release") = "net.liftweb" %% "lift-jpa" % v

  def liftJta(v: String = "latest.release") = "net.liftweb" %% "lift-jta" % v

  def liftMachine(v: String = "latest.release") = "net.liftweb" %% "lift-machine" % v

  def liftMapper(v: String = "latest.release") = "net.liftweb" %% "lift-mapper" % v

  def liftMongo(v: String = "latest.release") = "net.liftweb" %% "lift-mongodb-record" % v

  def liftPaypal(v: String = "latest.release") = "net.liftweb" %% "lift-paypal" % v

  def liftScalate(v: String = "latest.release") = "net.liftweb" %% "lift-scalate" % v

  def liftSqueryl(v: String = "latest.release") = "net.liftweb" %% "lift-squeryl-record" % v

  def liftTestkit(v: String = "latest.release") = "net.liftweb" %% "lift-testkit" % v

  def liftTextile(v: String = "latest.release") = "net.liftweb" %% "lift-textile" % v

  def liftWebkit(v: String = "latest.release") = "net.liftweb" %% "lift-webkit" % v

  def liftWidgets(v: String = "latest.release") = "net.liftweb" %% "lift-widgets" % v

  def liftWizard(v: String = "latest.release") = "net.liftweb" %% "lift-wizard" % v

  def mongodb(v: String = "latest.release") = "org.mongodb" % "mongo-java-driver" % v

  def mysql(v: String = "latest.release") = "mysql" % "mysql-connector-java" % v

  def neo4j(v: String = "latest.release") = "org.neo4j" % "neo4j" % v

  def ostrich(v: String = "latest.release") = "com.twitter" % "ostrich" % v

  //"9.0-801.jdbc4"
  def postgresql(v: String = "latest.release") = "postgresql" % "postgresql" % v

  def redstoneXMLRPC(v: String = "latest.release") = "org.kohsuke.redstone" % "redstone" % v

  def scalateCore(v: String = "latest.release") = "org.fusesource.scalate" % "scalate-core" % v

  def scalazCore(v: String = "latest.release") = "org.scalaz" %% "scalaz-core" % v

  def scalaQuery(v: String = "latest.release") = "org.scalaquery" %% "scalaquery" % v

  def selenium(v: String = "latest.release") = "org.seleniumhq.selenium" % "selenium-java" % v

  def seleniumsvr(v: String = "latest.release") = "org.seleniumhq.selenium" % "selenium-server" % v

  def shiroCore(v: String = "latest.release") = "org.apache.shiro" % "shiro-core" % v

  def shiroWeb(v: String = "latest.release") = "org.apache.shiro" % "shiro-web" % v

  def shiroEHCache(v: String = "latest.release") = "org.apache.shiro" % "shiro-ehcache" % v

  def apacheCommonsLang3(v: String = "latest.release") = "org.apache.commons" % "commons-lang3" % v

  def apacheCommonsEmail(v: String = "latest.release") = "org.apache.commons" % "commons-email" % v

  def specs(v: String = "latest.release") = "org.scala-tools.testing" %% "specs" % v

  def sprayJson(v: String = "latest.release") = "cc.spray" %% "spray-json" % v

  def sprayServer(v: String = "latest.release") = "cc.spray" % "spray-server" % v

  def slf4j(v: String = "latest.release") = "org.slf4j" % "slf4j-api" % v

  def subcut(v: String = "latest.release") = "org.scala-tools.subcut" %% "subcut" % v

  def jacksonCore(v: String = "latest.release") = "org.codehaus.jackson" % "jackson-core-asl" % v

  def bson4jackson(v: String = "latest.release") = "de.undercouch" % "bson4jackson" % v

  def jerkson(v: String = "latest.release") = "com.codahale" %% "jerkson" % v

  def unfiltered(v: String = "latest.release") = "net.databinder" %% "unfiltered" % v

  def unfilteredScalatest(v: String = "latest.release") = "net.databinder" %% "unfiltered-scalatest" % v

  def unfilteredSpec(v: String = "latest.release") = "net.databinder" %% "unfiltered-spec" % v

  def unfilteredFilter(v: String = "latest.release") = "net.databinder" %% "unfiltered-filter" % v

  def unfilteredJetty(v: String = "latest.release") = "net.databinder" %% "unfiltered-jetty" % v

  def unfilteredNetty(v: String = "latest.release") = "net.databinder" %% "unfiltered-netty" % v

  def unfilteredUploads(v: String = "latest.release") = "net.databinder" %% "unfiltered-uploads" % v

  def activeMQ(v: String = "latest.release") = "org.apache.activemq" % "activemq-core" % v

  def activeMQCamel(v: String = "latest.release") = "org.apache.activemq" % "activemq-camel" % v

  def activeMQPool(v: String = "latest.release") = "org.apache.activemq" % "activemq-pool" % v


  def camel(components: String*)(v: String = "latest.release"): Seq[ModuleID] = {
    val compNames = ("ahc,amqp,apns,atom,aws,bam,bean-validator,bindy,blueprint,buildtools,bundle,cache,castor,cometd,context,core,core-osgi,core-xml,crypto,csv,cxf,cxf-transport,dns,dozer,eclipse,ejb,eventadmin,exec,flatpack,freemarker,ftp,gae,groovy,guice,hamcrest,hawtdb,hazelcast,hdfs,hl7,http,http4,ibatis,irc,itest,itest-osgi,itest-spring-2.0,itest-spring-2.5,itest-standalone,jackson,jasypt,javaspace,jaxb,jclouds,jcr,jdbc,jdbc-aggregator,jetty,jhc,jibx,jing,jms,jmx,josql,jpa,jsch,jt400,juel,jxpath,kestrel,krati,ldap,lucene,mail,manual,maven-plugin,mina,mina2,msv,mvel,mybatis,nagios,netty,ognl,osgi,parent,partial-classpath-test,paxlogging,printer,protobuf,quartz,quickfix,rest,restlet,rmi,routebox,rss,ruby,saxon,scala,script,servlet,shiro,sip,smpp,snmp,soap,solr,spring,spring-integration,spring-javaconfig,spring-osgi,spring-security,spring-ws,sql,stax,stream,stringtemplate,swing,syslog,tagsoup,test,testng,typeconverterscan-test,uface,velocity,web,web-standalone,xmlbeans,xmlsecurity,xmpp,xstream,zookeeper"
      .split(",")
      .toSeq);

    def dep(c: String) = "org.apache.camel" % ("camel-" + c) % v

    for {
      c <- components
    } yield {
      assert(compNames.contains(c))
      dep(c)
    }
  }


  def junit4(v: String = "latest.release") = "junit" % "junit" % v % "test"

  def specs2(v: String = "latest.release") = "org.specs2" %% "specs2" % v % "test"

  def scalacheck(v: String = "latest.release") = "org.scala-tools.testing" %% "scalacheck" % v % "test"

  def scalatest(v: String = "latest.release") = "org.scalatest" %% "scalatest" % v % "test"

  def jettyWebApp(v: String = "latest.release") = "org.eclipse.jetty" % "jetty-webapp" % v % "container"

  def logbackClassic(v: String = "latest.release") = "ch.qos.logback" % "logback-classic" % v

  def logbackCore(v: String = "latest.release") = "ch.qos.logback" % "logback-core" % v

  def servletApi(v: String = "latest.release") = "javax.servlet" % "servlet-api" % v % "provided"


}
