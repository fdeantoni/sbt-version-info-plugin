package com.gu.versioninfo

import java.net.InetAddress
import java.util.Date
import sbt._
import Keys._
import java.lang.System

object VersionInfo extends Plugin {

  implicit def string2Dequote(s: String) = new {
    lazy val dequote = s.replace("\"", "")
  }

  val buildNumber = SettingKey[String]("version-build-number")
  val vcsBranch = SettingKey[String]("version-vcs-branch")
  val vcsRevision = SettingKey[String]("version-vcs-revision")

  override val settings = Seq(
    buildNumber := System.getProperty("build.number", "DEV"),
    vcsBranch := System.getProperty("build.vcs.branch", "DEV"),
    vcsRevision := System.getProperty("build.vcs.revision", "DEV"),
    resourceGenerators in Compile <+= (resourceManaged in Compile, name, version, vcsBranch, buildNumber, vcsRevision, streams) map buildFile
  )

  def buildFile(outDir: File, projectName: String, projectVersion: String, vcsBranch: String, buildNum: String, vcsRevision: String, s: TaskStreams) = {
    val versionInfo = Map(
      "Project" -> projectName,
      "Version" -> projectVersion,
      "Revision" -> vcsRevision.dequote.trim,
      "Branch" -> vcsBranch.dequote.trim,
      "Build" -> buildNum.dequote.trim,
      "Date" -> new Date().toString,
      "Built-By" -> System.getProperty("user.name", "<unknown>"),
      "Built-On" -> InetAddress.getLocalHost.getHostName)

    val versionFileContents = (versionInfo map { case (x, y) => x + ": " + y }).toList.sorted

    val versionFile = outDir / "version.txt"
    s.log.debug("Writing to " + versionFile + ":\n   " + versionFileContents.mkString("\n   "))

    IO.write(versionFile, versionFileContents mkString "\n")

    Seq(versionFile)
  }
}
