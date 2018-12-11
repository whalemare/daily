import command.ReleaseCommand
import command.ReportCommand
import picocli.CommandLine

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
@CommandLine.Command(
    name = "ash", aliases = ["ashe"],
    subcommands = [ReportCommand::class, ReleaseCommand::class]
)
class Main : Runnable {
    override fun run() {

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val commands = mutableListOf<String>()
            commands.addAll(args)
            parse(commands)
        }

        fun parse(args: List<String>) {
            CommandLine.run(Main(), *args.toTypedArray())
        }
    }
}