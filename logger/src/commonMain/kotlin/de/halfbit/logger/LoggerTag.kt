package de.halfbit.logger

public abstract class LoggerTag {
    public abstract val name: String
    public abstract val enabled: Boolean
}

// todo: as opposite to autoTag(), where tag name can be auto-detected
public fun namedTag(name: String, block: LoggerTagBuilder.() -> Unit): LoggerTag {
    return LoggerTagBuilder(name).also(block).build()
}

public class LoggerTagBuilder
internal constructor(
    private val name: String
) {
    private var enabled: Boolean = true

    public fun enabled() {
        enabled = true
    }

    public fun disabled() {
        enabled = false
    }

    internal fun build(): LoggerTag =
        object : LoggerTag() {
            override val name: String get() = this@LoggerTagBuilder.name
            override val enabled: Boolean get() = this@LoggerTagBuilder.enabled
        }
}
