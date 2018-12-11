package command

import commit.CommitFormatter
import commit.CommitProvider
import commit.RawCommitProvider
import commit.Since
import ext.clipboard
import ext.execute
import picocli.CommandLine
import java.text.SimpleDateFormat

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
@CommandLine.Command(name = "release")
class ReleaseCommand : Runnable {

    private val commitProvider = CommitProvider(RawCommitProvider(Since.LatestTag))

    override fun run() {
        val commits = commitProvider.provide()
        val message = CommitFormatter(commits).format()

        if (commits.isNotEmpty()) {
            val today = SimpleDateFormat("dd.MM.YYYY").format(commits.first().date.timeInMillis)
            val lastTag = Since.LatestTag.text.execute().trim()
            println("Release-notes c тега=$lastTag по сегодняший день=$today")
        } else {
            clipboard(message)
        }

        println(message)
    }
}