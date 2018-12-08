import picocli.CommandLine

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class Main : Runnable {
    override fun run() {
        print("Hello from Main")
    }

    fun main(args: Array<String>) {
        CommandLine.run(Main(), *args)
    }
}