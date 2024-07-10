/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.memory

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerBuilder
import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

public interface MemoryRingSink {
    public fun getLogEntries(): List<String>

    public companion object {
        public val DefaultLogPrinter: LogPrinter = LogPrinter { level, tag, timestamp, message, err ->
            val time = Clock.System.now().toLocalDateTime(TimeZone.UTC).time
            val hour = time.hour.toString().padStart(2, '0')
            val minute = time.minute.toString().padStart(2, '0')
            val second = time.second.toString().padStart(2, '0')
            val millis = (time.nanosecond / 1000000).toString().padStart(3, '0')
            val timeString = "$hour:$minute:$second.$millis"
            val entry = "$timeString ${level.short} [$tag]"
            when (message) {
                null -> when (err) {
                    null -> entry
                    else -> "$entry ${err.stackTraceToString()}"
                }
                else -> when (err) {
                    null -> "$entry $message"
                    else -> "$entry $message, exception:\n${err.stackTraceToString()}"
                }
            }
        }
    }
}

public fun LoggerBuilder.registerMemoryRingSink(
    maxEntriesCount: Int = 128,
    logPrinter: LogPrinter = MemoryRingSink.DefaultLogPrinter,
): MemoryRingSink {
    val sink = DefaultMemoryRingSink(maxEntriesCount, logPrinter)
    replaceSink(sink)
    return sink
}

@PublishedApi
internal class DefaultMemoryRingSink(
    maxEntriesCount: Int,
    private val logPrinter: LogPrinter,
) : MemoryRingSink, LogSink {
    private val entries: RingArray<String> = RingArray(maxEntriesCount)

    override fun log(level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?) {
        entries.add(logPrinter(level, tag, timestamp, message, err))
    }

    override fun getLogEntries(): List<String> =
        entries.toList()
}
