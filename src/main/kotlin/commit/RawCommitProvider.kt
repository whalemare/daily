package commit

import Provider
import ext.execute

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class RawCommitProvider(val since: Since = Since.Raw("6am")) : Provider<String> {

    override fun provide(): String {
        val since = when (since) {
            is Since.LatestTag -> {
                "git describe --tags --abbrev=0".execute().trim()
            }
            is Since.Raw -> since.text
        }

        val rawText = "git log --since='$since' --pretty=medium --date=iso-strict".execute()
        return rawText
    }
}