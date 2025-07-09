/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink

import de.halfbit.logger.LogLevel
import kotlin.time.Instant

public fun interface LogPrinter {
    public operator fun invoke(
        level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?
    ): String

    public companion object {
        public val Default: LogPrinter = buildLogPrinter {
            timestamp { timeOnly() }
            tag {
                padded {
                    length(22)
                    useSquareBrackets(false)
                    paddingChar('.')
                }
            }
            logLevel { short() }
        }
    }
}
