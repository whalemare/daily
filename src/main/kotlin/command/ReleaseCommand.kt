package command

import commit.CommitFormatter
import commit.CommitProvider
import commit.RawCommitProvider
import commit.Since
import converter.SinceConverter
import ext.clipboard
import ext.execute
import picocli.CommandLine
import picocli.CommandLine.Option
import java.text.SimpleDateFormat

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
@CommandLine.Command(name = "release")
class ReleaseCommand : Runnable {

    @Option(names = ["--path", "-p"])
    private var path: String = ""

    @Option(names = ["--relative", "-r"])
    private var relative: Boolean = false

    @Option(names = ["--since", "-s", "-d"])
    private var sinceRaw: String = ""

    override fun run() {
        val commits = CommitProvider(RawCommitProvider(
            since = SinceConverter(Since.LatestTag).convert(sinceRaw),
            path = path,
            all = !relative
        )).provide()
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