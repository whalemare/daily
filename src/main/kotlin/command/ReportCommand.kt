package command

import commit.Commit
import commit.CommitProvider
import commit.RawCommitProvider
import ext.clipboard
import picocli.CommandLine


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

        var message = ""
        commits.forEach { commit ->
            // ignore commits with empty body
            if (commit.body.isBlank()) return

            val prefix = findPrefix(commit)
            message += "$prefix ${commit.body}\n"
        }

        clipboard(message)
        println(message)
    }

    private fun findPrefix(commit: Commit): String {
        val pair: Pair<String, String> = prefixes.firstOrNull { (_, value) ->
            commit.head.contains(value, ignoreCase = true)
        } ?: "" to ""
        return pair.first
    }
}