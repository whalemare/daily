package commit

import Provider
import org.zeroturnaround.exec.ProcessExecutor

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class RawCommitProvider(val since: Since = Since.Raw("6am")) : Provider<String> {

    sealed class Since {

        /**
         * TODO: rewrite to windows
         * Works only on Unix
         */
        class LatestTag() : Tag("\$(git describe --tags --abbrev=0 @^)")

        open class Tag(tag: String) : Raw("$tag..HEAD")

        /**
         * Expected some raw result that will be putted
         * ```bash
         * git log --since='PARAM'
         * ```
         * where PARAM it is [text] param
         * @param text 6am, 9pm, c3eeab6..0158d99, tag-name..HEAD, today
         */
        open class Raw(val text: String) : Since()
    }

    override fun provide(): String {
        val since = when(since) {
            is Since.Raw -> {
                "--since='${since.text}'"
            }
        }

        val rawText = ProcessExecutor().command(
            "git", "log",
            since,
            "--pretty=medium",
            "--date=iso-strict"
        ).readOutput(true)
            .execute()
            .outputUTF8()

        return rawText
    }
}