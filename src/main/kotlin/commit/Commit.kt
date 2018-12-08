package commit

import java.util.*

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
data class Commit(
    var hash: String = "",
    var author: String = "",
    var date: Calendar = Calendar.getInstance(),
    var head: String = "",
    var body: String = "",
    var footer: String = ""
)