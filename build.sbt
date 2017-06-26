lazy val root = project.in(file("."))
  .enablePlugins(ScalaJSPlugin)

name := "calc"

version := "0.0.1"

scalaVersion := "2.12.2"

scalaJSStage in Global := FastOptStage

scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint")

licenses +=("MIT", url("http://opensource.org/licenses/MIT"))

//jsDependencies += RuntimeDOM
jsDependencies in Test += RuntimeDOM

// see https://gitter.im/scala-js/scala-js/archives/2017/04/13
scalaJSUseMainModuleInitializer := false
// scalaJSMainModuleInitializer := Some("praxkit.HelloWorld1")
//mainClass in Compile := Some("praxkit.HelloWorld1")

jsDependencies +=
  "org.webjars" % "jquery" % "2.1.3" / "2.1.3/jquery.js"

//skip in packageJSDependencies := false

libraryDependencies ++= Seq("com.lihaoyi" %%% "utest" % "0.4.7" % "test")
testFrameworks += new TestFramework("utest.runner.Framework")


libraryDependencies ++= Seq(
  "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
  "org.scala-js" %%% "scalajs-dom" % "0.9.2",
  "com.thoughtworks.binding" %%% "dom" % "latest.release",
  "com.github.japgolly.scalacss" %%% "core" % "0.5.3",
  "com.lihaoyi" %%% "scalatags" % "0.6.5"

)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
