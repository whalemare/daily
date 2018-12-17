package converter

import commit.Since
import picocli.CommandLine

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class SinceConverter(val default: Since): CommandLine.ITypeConverter<Since> {
    override fun convert(value: String?): Since {
        return if (value.isNullOrBlank()) {
            default
        } else {
            Since.Raw(value)
        }
    }
}