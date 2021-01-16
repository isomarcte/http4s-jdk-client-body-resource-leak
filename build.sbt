lazy val root = (project in file(".")).settings(
  scalaVersion := "2.13.4",
  libraryDependencies ++= List(
    "org.http4s" %% "http4s-jdk-http-client" % "0.3.3",
    "org.http4s" %% "http4s-ember-server" % "0.21.5"
  )
)
