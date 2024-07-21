/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.js

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerBuilder
import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import de.halfbit.logger.sink.buildLogPrinter
import kotlinx.datetime.Instant

public fun LoggerBuilder.registerConsoleLogSink(
    logPrinter: LogPrinter = browserLogPrinter
) {
    replaceSink(ConsoleLogSink(logPrinter))
}

private val browserLogPrinter = buildLogPrinter {
    timestamp { none() }
    tag {
        wrapped {
            length(30)
            useSquareBrackets(true)
        }
    }
    logLevel { none() }
}

internal class ConsoleLogSink(
    private val logPrinter: LogPrinter,
) : LogSink {

    override fun log(
        level: LogLevel,
        tag: String,
        timestamp: Instant,
        message: String?,
        err: Throwable?
    ) {
        val msg = logPrinter(level, tag, timestamp, message, err)
        when (level) {
            LogLevel.Debug -> console.log(msg)
            LogLevel.Info -> console.info(msg)
            LogLevel.Warning -> console.warn(msg)
            LogLevel.Error -> console.error(msg)
        }
    }
}
