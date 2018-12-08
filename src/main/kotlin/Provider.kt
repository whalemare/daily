/**
 * @since 2018
 * @author Anton Vlasov - whalemare
 */
interface Provider<T> {
    fun provide(): T
}