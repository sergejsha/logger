package de.halfbit.logger.sink

import de.halfbit.logger.LogLevel
import kotlinx.datetime.Instant

public interface LogSink {
    public fun log(level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?)
}