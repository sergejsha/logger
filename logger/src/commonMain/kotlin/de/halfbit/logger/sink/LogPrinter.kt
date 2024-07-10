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
}
