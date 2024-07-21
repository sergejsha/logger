/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.println

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerBuilder
import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import kotlinx.datetime.Instant

public fun LoggerBuilder.registerPrintlnSink(
    logPrinter: LogPrinter = LogPrinter.Default,
) {
    replaceSink(PrintlnSink(logPrinter))
}

internal class PrintlnSink(
    private val logPrinter: LogPrinter,
) : LogSink {

    override fun log(level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?) {
        println(logPrinter(level, tag, timestamp, message, err))
    }
}
