name := "scala-redshift-connection"

version := "0.1"

scalaVersion := "2.11.12"

resolvers += "Mulesoft" at "https://repository.mulesoft.org/nexus/content/repositories/public/"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-secretsmanager" % "1.11.339"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
libraryDependencies += "com.amazon.redshift" % "redshift-jdbc42" % "1.2.27.1051"

assemblyMergeStrategy in assembly :=  {
  {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("com", "amazonaws", "auth", xs@_*) => MergeStrategy.last
    case x => MergeStrategy.first
  }
}

