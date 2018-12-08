
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class for handling a most common subset of ISO 8601 strings
 * (in the following format: "2008-03-01T13:00:00+01:00"). It supports
 * parsing the "Z" timezone, but many other less-used features are
 * missing.
 */
object ISO8601 {
    /** Transform Calendar to ISO 8601 string.  */
    fun fromCalendar(calendar: Calendar): String {
        val date = calendar.time
        val formatted = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .format(date)
        return formatted.substring(0, 22) + ":" + formatted.substring(22)
    }

    /** Get current date and time formatted as ISO 8601 string.  */
    fun now(): String {
        return fromCalendar(GregorianCalendar.getInstance())
    }

    /** Transform ISO 8601 string to Calendar.  */
    @Throws(ParseException::class)
    fun toCalendar(iso8601string: String): Calendar {
        val calendar = GregorianCalendar.getInstance()
        var s = iso8601string.replace("Z", "+00:00")
        try {
            s = s.substring(0, 22) + s.substring(23)  // to get rid of the ":"
        } catch (e: IndexOutOfBoundsException) {
            throw ParseException("Invalid length", 0)
        }

        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s)
        calendar.time = date
        return calendar
    }
}