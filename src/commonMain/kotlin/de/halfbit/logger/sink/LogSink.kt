/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink

import de.halfbit.logger.LogLevel
import kotlin.time.Instant

public interface LogSink {
    public fun log(level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?)
}