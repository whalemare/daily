package commit

import ISO8601
import Provider

/**
 * Return list of commit, sorted by date
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class CommitProvider(
    private val rawProvider: Provider<String>
) : Provider<List<Commit>> {

    /**
     * @return list of commit, sorted by date
     */
    override fun provide(): List<Commit> {
        val rawText = rawProvider.provide()
        val lines = rawText.split("\n")
        val commits = mutableListOf<Commit>()

        var index = 0
        while (index < lines.size) {
            val line = lines[index]

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
                val indexEnd = linesStart.indexOf("")
                index += (indexEnd + 2)
                val linesBody = linesStart.subList(0, indexEnd)
                val (header, body, footer) = startBody(linesBody)
                commits.last().apply {
                    this.head = header
                    this.body = body
                    this.footer = footer
                }
                continue
            }

            index++
        }

        return commits.sortedBy { it.date }
    }

    private fun startBody(lines: List<String>): Triple<String, String, String> {
        val parts = lines.filter { it.isNotBlank() }
        return Triple(
            (parts.elementAtOrNull(0) ?: "").trim(),
            (parts.elementAtOrNull(1) ?: "").trim(),
            (parts.elementAtOrNull(2) ?: "").trim()
        )
    }
}