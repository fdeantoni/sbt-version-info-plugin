A fork of [Guardian's sbt-version-info-plugin](https://github.com/guardian/sbt-version-info-plugin) that places a
little bit of extra data in the version.txt. The System properties have also been streamlined to avoid confusion.

sbt-version-info-plugin
=======================

This sbt plugin generates a version.txt file in the root of your classpath.

To use:

1. Add the sbt-version-info-plugin to your sbt build, by creating project/project/plugins.scala that looks like:

        import sbt._

        object Plugins extends Build {
          lazy val plugins = Project("plugins", file("."))
            .dependsOn(
              uri("git://github.com/fdeantoni/sbt-version-info-plugin.git")
            )
        }

2. The VersionInfo trait relies on a couple of system properties to obtain the build number etc from the CI server.
   To pass these to Jenkins create an SBT start script that does the following:

        #!/bin/bash

        cat /dev/null | java -Xmx1G -XX:MaxPermSize=250m \
         -Dsbt.log.noformat=true \
         -Dbuild.number="$BUILD_NUMBER" \
         -Dbuild.vcs.branch="$GIT_BRANCH" \
         -Dbuild.vcs.revision="$GIT_COMMIT" \
         -jar sbt-launch.jar "$@"

   Alternatively you can put the properties in the "sbt Flags" field in the Jenkins build definition.
