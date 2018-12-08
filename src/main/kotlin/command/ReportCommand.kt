package command

import commit.Commit
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

    private val prefixes = listOf(
        "[+]" to "add",
        "[+]" to "put",
        "[+]" to "make",

        "[*]" to "fix",
        "[*]" to "change",

        "[-]" to "remove",
        "[-]" to "disable"
    )

    private val provider = CommitProvider(RawCommitProvider())

    override fun run() {
        val commits = provider.provide()

        val messages = mutableListOf<String>()
        commits.filter { commit ->
            commit.body.isNotBlank() // ignore commits with empty body
        }.forEach { commit ->
            val prefix = findPrefix(commit)

            val bodies = commit.body.split("\n")
            bodies.forEach { body ->
                messages.add("$prefix $body")
            }
        }

        val message = messages.joinToString("\n")
        clipboard(message)

        val today = SimpleDateFormat("dd.MM.YYYY").format(commits.first().date.timeInMillis)
        println("Отчет за $today")
        println(message)
    }

    private fun findPrefix(commit: Commit): String {
        val pair: Pair<String, String> = prefixes.firstOrNull { (_, value) ->
            commit.head.contains(value, ignoreCase = true)
        } ?: "" to ""
        return pair.first
    }
}