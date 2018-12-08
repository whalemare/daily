import org.zeroturnaround.exec.ProcessExecutor

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class CommitProvider : Provider<Commit> {
    override fun provide(): Commit {
        val text = ProcessExecutor().command("git", "log")
            .readOutput(true)
            .execute()
            .outputUTF8()
        return Commit(text, text, text)
    }
}