import ru.whalemare.command.ReportCommand
import org.junit.jupiter.api.Test

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class ReportCommandTest {

    @Test
    fun start() {
        val command = ReportCommand()
        command.run()
    }

}