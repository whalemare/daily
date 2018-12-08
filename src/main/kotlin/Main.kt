import picocli.CommandLine

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class Main : Runnable {
    override fun run() {

    }

    fun main(args: Array<String>) {
        CommandLine.run(Main(), *args)
    }
}