package de.halfbit.logger

import de.halfbit.logger.sink.LogSink
import de.halfbit.logger.sink.println.PrintlnSink

@PublishedApi
internal var logger: Logger = Logger()

@PublishedApi
internal data class Logger(
    val sinks: List<LogSink> = listOf(PrintlnSink),
    val level: LogLevel = LogLevel.Debug,
    val initialized: Boolean = false,
)
