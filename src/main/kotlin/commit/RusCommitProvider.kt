package commit

import Provider

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class RusCommitProvider(rawProvider: Provider<String>) : CommitProvider(rawProvider) {

    override fun startBody(lines: List<String>): Triple<String, String, String> {
        val (header, _, footer) = super.startBody(lines)
        val body = lines.map { line ->
            line.replace(Regex("[A-Za-z]+"), "").trim()
        }.map { line ->
            val regex = Regex("^[^а-яА-Я]+\$")
            if (line.contains(regex)) {
                line.replace(regex, "")
            } else {
                line
            }
        }.filter { line ->
            line.isNotBlank()
        }.joinToString("\n")

        return Triple(header, body, footer)
    }
}