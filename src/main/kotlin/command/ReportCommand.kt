package command

import commit.Commit
import commit.CommitProvider
import commit.RawCommitProvider
import ext.clipboard
import picocli.CommandLine
import java.text.SimpleDateFormat
import java.util.*


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

    private val postfixes = mapOf(
        "[+]" to "Добавлены небольшие улучшения, делающие жизнь пользователей и разработчика приятнее",
        "[-]" to "Убраны небольшие неточности, которые являлись излишними",
        "[*]" to "Сделаны небольшие изменения, ради всеобщего блага"
    )

    private val provider = CommitProvider(RawCommitProvider())

    override fun run() {
        val commits = provider.provide()
        if (commits.isEmpty()) {
            onEmptyCommits()
            return
        }

        val messages = mutableListOf<String>()
        val minorChanges = mutableSetOf<String>()
        commits.forEach { commit ->
            val prefix = findPrefix(commit)

            if (commit.body.isBlank()) {
                minorChanges.add("$prefix ${postfixes[prefix]!!}")
            } else {
                val bodies = commit.body.split("\n")
                bodies.forEach { body ->
                    messages.add("$prefix $body")
                }
            }
        }

        minorChanges.forEach { messages.add(it) }

        val message = messages.joinToString("\n")
        clipboard(message)

        val today = SimpleDateFormat("dd.MM.YYYY").format(commits.first().date.timeInMillis)
        println("Отчет за $today")
        println(message)
    }

    private fun onEmptyCommits() {
        val today = SimpleDateFormat("dd.MM.YYYY").format(Date())
        println("Дата: $today")
        println("Невозможно создать отчет, если Вы еще не поработали :)")
    }

    private fun findPrefix(commit: Commit): String {
        val pair: Pair<String, String> = prefixes.firstOrNull { (_, value) ->
            commit.head.contains(value, ignoreCase = true)
        } ?: "" to ""
        return pair.first
    }
}