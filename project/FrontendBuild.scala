import play.sbt.PlayRunHook
import sbt._

import scala.sys.process.Process

object FrontendBuild {

  def apply(base: File): PlayRunHook = {
    object UIBuildHook extends PlayRunHook {
      var process: Option[Process] = None
      var yarnInstall: String = FrontendCommands.dependencyInstall
      var yarnRun: String = FrontendCommands.build

      override def beforeStarted(): Unit = {
        if (!(base / "ui" / "node_modules").exists()) Process(yarnInstall, base / "ui").!
      }

      override def afterStarted(): Unit = {
        process = Option(
          Process(yarnRun, base / "ui").run
        )
      }

      override def afterStopped(): Unit = {
        process.foreach(_.destroy())
        process = None
      }
    }

    UIBuildHook
  }
}
