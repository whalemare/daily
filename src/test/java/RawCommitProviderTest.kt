import commit.CommitProvider
import commit.RawCommitProvider
import org.junit.jupiter.api.Test

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class RawCommitProviderTest {

    val rawProvider = RawCommitProvider()
    val commitProvider = CommitProvider(rawProvider)

    @Test
    fun should() {
        val commit = rawProvider.provide()
        println(commit)
    }

    @Test
    fun should_commit() {
        val commits = commitProvider.provide()
    }

}