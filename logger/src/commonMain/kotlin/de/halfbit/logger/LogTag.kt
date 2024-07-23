/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

public abstract class LogTag {
    public abstract val name: String
    public abstract val loggableLevel: LoggableLevel
}

// todo: as opposite to autoTag(), where tag name can be auto-detected
public fun namedTag(name: String, loggableLevel: LoggableLevel = LoggableLevel.Everything): LogTag =
    object : LogTag() {
        override val name: String get() = name
        override val loggableLevel: LoggableLevel get() = loggableLevel
    }
