package commit

import java.text.SimpleDateFormat
import java.util.*

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class CommitFormatter(private val commits: List<Commit>) {

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
        "[+]" to "Код слегка препудрен улучшениями, делающими жизнь пользователей и разработчика приятнее",
        "[-]" to "Убраны маленькие неточности, которые являлись излишними",
        "[*]" to "Сделаны небольшие изменения, ради всеобщего блага"
    )

    private fun findPrefix(commit: Commit): String {
        val pair: Pair<String, String> = prefixes.firstOrNull { (_, value) ->
            commit.head.contains(value, ignoreCase = true)
        } ?: "" to ""
        return pair.first
    }

    private fun onEmptyCommits(): String {
        val today = SimpleDateFormat("dd.MM.YYYY").format(Date())
        val text = """
            Невозможно создать отчет за $today, вероятно в этот день не было работы
        """.trimIndent()
        return text
    }

    /**
     * Handle empty list of commits, and return default message
     * @return not empty strings.
     */
    fun format(): String {
        if (commits.isEmpty()) return onEmptyCommits()

        val messages = mutableListOf<String>()
        val minorChanges = mutableSetOf<String>()
        commits.forEach { commit ->
            val prefix = findPrefix(commit)

            if (commit.body.isBlank()) {
                if (prefix.isNotBlank()) {
                    minorChanges.add("$prefix ${postfixes[prefix]!!}")
                }
            } else {
                val bodies = commit.body.split("\n")
                bodies.forEach { body ->
                    messages.add("$prefix $body")
                }
            }
        }

        minorChanges.forEach { messages.add(it) }

        return messages.joinToString("\n")
    }
}