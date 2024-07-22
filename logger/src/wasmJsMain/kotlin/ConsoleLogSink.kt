/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.wasmjs

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

private class ConsoleLogSink(
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
            LogLevel.Debug -> wasmLog(msg)
            LogLevel.Info -> wasmInfo(msg)
            LogLevel.Warning -> wasmWarn(msg)
            LogLevel.Error -> wasmError(msg)
        }
    }
}

private fun wasmLog(message: String): Unit = js("console.log(message)")
private fun wasmInfo(message: String): Unit = js("console.info(message)")
private fun wasmWarn(message: String): Unit = js("console.warn(message)")
private fun wasmError(message: String): Unit = js("console.error(message)")
