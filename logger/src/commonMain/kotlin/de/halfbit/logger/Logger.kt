/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import de.halfbit.logger.sink.println.PrintlnSink
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@PublishedApi
internal var logger: Logger = Logger()

@PublishedApi
internal data class Logger(
    val sinks: List<LogSink> = listOf(PrintlnSink(logPrinter = LogPrinter.Short)),
    val loggableLevel: LoggableLevel = LoggableLevel.Everything,
    val getClockNow: () -> Instant = Clock.System::now,
    val initialized: Boolean = false,
)
