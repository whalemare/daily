package commit

import ISO8601
import Provider

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
open class CommitProvider(
    private val rawProvider: Provider<String>
) : Provider<List<Commit>> {

    /**
     * @return список коммитов отсортированных по дате их создания (от первого к последнему)
     */
    override fun provide(): List<Commit> {
        val rawText = rawProvider.provide()
        val lines = rawText.split("\n")
        val commits = mutableListOf<Commit>()

        if (lines.isEmpty() || lines.size == 1) {
            return emptyList()
        }

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

    open fun startBody(lines: List<String>): Triple<String, String, String> {
        val HEADER = 0
        val HEADER_FOOTER = 1
        val HEADER_BODY_FOOTER = 2

        val countWhitespaces = lines.count { it.isBlank() }

        var header = ""
        var body = ""
        var footer = ""
        when (countWhitespaces) {
            HEADER -> {
                header = extractHeader(lines)
            }
            HEADER_FOOTER -> {
                header = extractHeader(lines)
                footer = extractBody(lines)
            }
            HEADER_BODY_FOOTER -> {
                header = extractHeader(lines)
                body = extractBody(lines)
                footer = extractFooter(lines)
            }
        }
        return Triple(
            header,
            body,
            footer
        )
    }

    private fun extractHeader(lines: List<String>): String {
        return lines.first().trim()
    }

    private fun extractBody(lines: List<String>): String {
        val indexBody = lines.indexOfFirst { it.isBlank() }
        val indexLastWhitespace = lines.indexOfLast { it.isBlank() }
        val indexEnd = if (indexBody == indexLastWhitespace) {
            lines.size
        } else {
            indexLastWhitespace
        }
        return lines.subList(indexBody, indexEnd)
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .joinToString(separator = "\n")
    }

    private fun extractFooter(lines: List<String>): String {
        val indexFooter = lines.indexOfLast { it.isBlank() }
        return lines.subList(indexFooter, lines.size).joinToString("\n") { it.trim() }
    }
}