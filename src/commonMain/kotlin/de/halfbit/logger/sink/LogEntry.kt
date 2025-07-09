/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink

import de.halfbit.logger.LogLevel
import kotlin.time.Instant

internal data class LogEntry(
    val level: LogLevel,
    val tag: String,
    val timestamp: Instant,
    val message: String?,
    val err: Throwable?,
)
