name := "backend-academy"

version := "0.1"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalamock" %% "scalamock" % "5.1.0" % Test,
    "org.scalatest" %% "scalatest" % "3.2.10" % Test
  ),
  scalaVersion := "2.13.6"
)

lazy val catsSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.3.0"
  ),
)

lazy val root = project in file(".") aggregate (
  lesson3,
  lesson4
)

lazy val lesson3 = project in file("lesson3") settings commonSettings
lazy val lesson4 = project in file("lesson4") settings commonSettings
lazy val lesson6 = project in file("lesson6") settings commonSettings ++ catsSettings
