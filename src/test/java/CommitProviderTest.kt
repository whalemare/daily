import org.junit.jupiter.api.Test

/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
class CommitProviderTest {

    @Test
    fun should() {
        val provider = CommitProvider()
        val commit = provider.provide()
    }
}