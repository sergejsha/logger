package de.halfbit.logger

public abstract class LoggerTag {
    public abstract val name: String
    public abstract val enabled: Boolean
}

// todo: as opposite to autoTag(), where tag name can be auto-detected
public fun namedTag(name: String, enabled: Boolean = true): LoggerTag =
    object : LoggerTag() {
        override val name: String get() = name
        override val enabled: Boolean get() = enabled
    }
