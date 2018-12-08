package commit

import Provider
import org.zeroturnaround.exec.ProcessExecutor

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class RawCommitProvider : Provider<String> {

    override fun provide(): String {
        val rawText = ProcessExecutor().command(
            "git", "log",
            "--since='6am'",
            "--pretty=medium",
            "--date=iso-strict"
        ).readOutput(true)
            .execute()
            .outputUTF8()

        return rawText
    }
}