package commit

import Provider
import ext.execute

/**
 * @param since see inherited doc
 * @param path for directory then need scan. By default used current executed directory
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class RawCommitProvider(
    val since: Since = Since.Raw("6am"),
    val path: String = ""
) : Provider<String> {

    override fun provide(): String {
        val repo = if (path.isBlank()) {
            ""
        } else {
            "--git-dir $path/.git"
        }

        val since = when (since) {
            is Since.LatestTag -> {
                "git $repo describe --tags --abbrev=0".execute().trim()
            }
            is Since.Raw -> since.text
        }

        val rawText = "git $repo log --since='$since' --pretty=medium --date=iso-strict".trimIndent().execute()
        return rawText
    }
}