package ext

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.BufferedReader
import java.io.InputStreamReader



/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */

fun clipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}

fun String.execute(): String {
    return executeBashCommand(this)
//    return ProcessExecutor()
//        .command(split(" "))
//        .readOutput(true)
//        .execute()
//        .outputUTF8()
}

/**
 * Execute a bash command. We can handle complex bash commands including
 * multiple executions (; | && ||), quotes, expansions ($), escapes (\), e.g.:
 * "cd /abc/def; mv ghi 'older ghi '$(whoami)"
 * @param command
 * @return true if bash got started, but your command may have failed.
 */
fun executeBashCommand(command: String): String {
    var success = false
    val r = Runtime.getRuntime()
    // Use bash -c so we can handle things like multi commands separated by ; and
    // things like quotes, $, |, and \. My tests show that command comes as
    // one argument to bash, so we do not need to quote it to make it one thing.
    // Also, exec may object if it does not have an executable file as the first thing,
    // so having bash here makes it happy provided bash is installed and in path.
    val commands = arrayOf("bash", "-c", command)
    try {
        val p = r.exec(commands)

        p.waitFor()
        val b = BufferedReader(InputStreamReader(p.inputStream))

//        var lines = mutableListOf<String>()
        var line = ""
        var nextCall: String? = b.readLine()
        while (nextCall != null) {
            line += nextCall + "\n"
            nextCall = b.readLine()
        }

        b.close()
        return line
    } catch (e: Exception) {
        System.err.println("Failed to execute bash with command: $command")
        e.printStackTrace()
        return e.toString()
    }
}