package ext

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */

fun clipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}