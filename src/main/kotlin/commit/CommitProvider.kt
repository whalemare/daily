package commit

import ISO8601
import Provider

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class CommitProvider(
    private val rawProvider: Provider<String>
) : Provider<List<Commit>> {

    override fun provide(): List<Commit> {
        val rawText = rawProvider.provide()
        val lines = rawText.split("\n")
        val commits = mutableListOf<Commit>()

        var index = 0
        for (line in lines) {
            val prefixCommit = "commit "
            val prefixAuthor = "Author: "
            val prefixDate = "Date: "

            if (line.contains(prefixCommit)) {
                commits.add(Commit())
                commits.last().hash = line.removePrefix(prefixCommit).trim()
            } else if (line.contains(prefixAuthor)) {
                commits.last().author = line.removePrefix(prefixAuthor).trim()
            } else if (line.contains(prefixDate)) {
                val rawDate = line.removePrefix(prefixDate).trim()
                val calendar = ISO8601.toCalendar(rawDate)
                commits.last().date = calendar
            } else if (line.isEmpty()) {
                val linesStart = lines.subList(index + 1, lines.size)
                index = linesStart.indexOf("")
                val linesBody = linesStart.subList(0, index)
                val body = startBody(linesBody)
                commits.last().body = body
            }

            index++
        }

        return commits
    }

    private fun startBody(lines: List<String>): String {
        return lines.joinToString("\n")
    }
}