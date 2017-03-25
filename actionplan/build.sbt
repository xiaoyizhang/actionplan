name := """actionplan"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

// set the location of the JDK to use for compiling Java code.
// if 'fork' is true, this is used for 'run' as well
//javaHome := Some(file("C:/Program Files/Java/jdk1.6.0_45"))

libraryDependencies ++= Seq(
  "fr.janalyse"   %% "janalyse-ssh" % "0.9.18" % "compile",
  "org.yaml" % "snakeyaml" % "1.15",
  jdbc,
  anorm,
  cache,
  ws
)

val cygwinDeployTask = TaskKey[Unit]("deploy-win-qa", "Build and deploy zip file to hadoop cluster")

cygwinDeployTask <<= (packageBin in Universal, target) map {
  (asm, target) =>
    val account = "qa-estool"
    val local = asm.getPath.replace("S:","\\cygdrive\\s").replace("\\","/")
    val name = asm.getName
    println(s"$local $name")
    val remote = account + ":."
    println(s"Copying: $local -> $account:$remote")
    Seq("scp", local, remote) !!
    val cmd = "unzip -o " + name
    println(s"Unzipping: $cmd")
    Seq("ssh", account, cmd) !!
}


