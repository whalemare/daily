package command

import commit.CommitFormatter
import commit.CommitProvider
import commit.RawCommitProvider
import commit.Since
import converter.SinceConverter
import ext.clipboard
import picocli.CommandLine
import picocli.CommandLine.Option
import java.text.SimpleDateFormat


/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
@CommandLine.Command(
    name = "report",
    description = ["Generate daily report for current invokable git repository"]
)
class ReportCommand : Runnable {

    @Option(names = ["--path", "-p"])
    private var path: String = ""

    @Option(names = ["--relative", "-r"])
    private var relative: Boolean = false

    @Option(names = ["--since", "-s", "-d"])
    private var sinceRaw: String = ""

    override fun run() {
        val commits = CommitProvider(
            RawCommitProvider(
                path = path,
                all = !relative,
                since = SinceConverter(Since.Raw("6am")).convert(sinceRaw)
            )
        ).provide()
        val formatter = CommitFormatter(commits)
        val message = formatter.format()

        if (commits.isNotEmpty()) {
            val today = SimpleDateFormat("dd.MM.YYYY").format(commits.first().date.timeInMillis)
            println("Отчет за $today")
        } else {
            clipboard(message)
        }

        println(message)
    }

}