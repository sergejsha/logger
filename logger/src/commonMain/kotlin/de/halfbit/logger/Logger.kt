/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import de.halfbit.logger.sink.println.PrintlnSink
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@PublishedApi
internal var currentLogger: Logger = createInitialLogger()

@PublishedApi
internal data class Logger(
    val sinks: List<LogSink>,
    val loggableLevel: LoggableLevel,
    val getClockNow: () -> Instant,
    val initialized: Boolean,
)

internal fun createInitialLogger(): Logger =
    Logger(
        sinks = listOf(PrintlnSink(LogPrinter.Short)),
        loggableLevel = LoggableLevel.Everything,
        getClockNow = Clock.System::now,
        initialized = false,
    )
