package de.halfbit.logger.sink.println

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerBuilder
import de.halfbit.logger.sink.LogSink
import kotlinx.datetime.Instant

public fun LoggerBuilder.registerPrintlnSink() {
    replaceSink(PrintlnSink)
}

internal object PrintlnSink : LogSink {
    override fun log(level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?) {
        if (message == null) {
            println("*** ${level.short} [$tag]")
        } else {
            println("*** ${level.short} [$tag] $message")
        }
        if (err != null) {
            val trace = err.stackTraceToString()
            println(trace)
        }
    }
}
