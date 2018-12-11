package command

import commit.CommitFormatter
import commit.CommitProvider
import commit.RawCommitProvider
import ext.clipboard
import picocli.CommandLine
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

    @CommandLine.Option(names = ["--path", "-p"])
    private var path: String = ""

    @CommandLine.Option(names = ["--relative", "-r"])
    private var relative: Boolean = false

    override fun run() {
        val commits = CommitProvider(RawCommitProvider(
            path = path,
            all = !relative
        )).provide()
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