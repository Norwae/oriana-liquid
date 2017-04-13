name := "oriana-liqibase"

organization := "com.github.norwae"

description := "oriana-liquid provides a liquibase-supported upgrader for schema creation and evolution"

version := "1.1.0-SNAPSHOT"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.11.8")

publishMavenStyle := true

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= {
  Seq(
    "com.github.norwae" %% "oriana" % "1.1.0",
    "org.liquibase" % "liquibase-core" % "3.5.3",

    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "com.h2database" % "h2" % "1.4.191" % "test"
  )
}

scmInfo := Some(ScmInfo(url("https://github.com/norwae/oriana"), "scm:git:https://github.com/Norwae/oriana-liquid.git", Some("scm:git:ssh://git@github.com:Norwae/oriana-liquid")))

pomExtra :=
  Seq(<licenses>
    <license>
      <name>BSD 2-Clause</name>
      <url>https://github.com/Norwae/oriana-liquid/blob/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>, <developers>
    <developer>
      <name>Stefan Schulz</name>
      <email>schulz.stefan@gmail.com</email>
    </developer>
  </developers>, <url>https://github.com/norwae/oriana-liquid</url>)
