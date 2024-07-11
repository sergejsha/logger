/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink

import de.halfbit.logger.LogLevel
import kotlinx.datetime.Instant

public fun interface LogPrinter {
    public operator fun invoke(
        level: LogLevel,
        tag: String,
        timestamp: Instant,
        message: String?,
        err: Throwable?
    ): String

    public companion object {
        public val Short: LogPrinter = LogPrinter { level, tag, timestamp, message, err ->
            val iso8601String = timestamp.toString() // 2023-01-02T23:40:57.120Z
            val timeString = if (iso8601String.length == 24) iso8601String.substring(11, 23) else iso8601String
            buildString {
                append(timeString)
                append(' ')
                append(level.short)
                append(' ')
                append(tag.padStart(16, padChar = ' ').takeLast(16))
                append(" | ")
                if (message != null) {
                    append(message)
                }
                if (err != null) {
                    append(", exception: \n")
                    append(err.stackTraceToString())
                }
            }
        }
    }
}
