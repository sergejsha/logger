/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.android

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerBuilder
import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ptr
import kotlin.time.Instant
import platform.darwin.*

public fun LoggerBuilder.registerIosLogSink(
    logPrinter: LogPrinter = LogPrinter.Default
) {
    replaceSink(IosLogSink(logPrinter))
}

private class IosLogSink(
    private val logPrinter: LogPrinter,
) : LogSink {

    @OptIn(ExperimentalForeignApi::class)
    override fun log(
        level: LogLevel,
        tag: String,
        timestamp: Instant,
        message: String?,
        err: Throwable?
    ) {
        val iosLogType = when (level) {
            LogLevel.Debug -> OS_LOG_TYPE_DEBUG
            LogLevel.Info -> OS_LOG_TYPE_INFO
            LogLevel.Warning -> OS_LOG_TYPE_ERROR
            LogLevel.Error -> OS_LOG_TYPE_FAULT
        }
        _os_log_internal(
            __dso_handle.ptr,
            OS_LOG_DEFAULT,
            iosLogType,
            logPrinter(level, tag, timestamp, message, err),
        )
    }
}
