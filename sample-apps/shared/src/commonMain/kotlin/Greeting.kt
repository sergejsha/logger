import de.halfbit.logger.d

private const val TAG = "Greetings"

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        d(TAG) { "greet method is called" }
        return "Hello, ${platform.name}!"
    }
}