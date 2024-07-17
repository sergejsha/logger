/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.memory

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerBuilder
import de.halfbit.logger.sink.LogEntry
import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import kotlinx.datetime.Instant

public interface MemoryRingSink {
    public fun getLogEntries(): List<String>
}

public fun LoggerBuilder.registerMemoryRingSink(
    logPrinter: LogPrinter = LogPrinter.Short,
    maxEntriesCount: Int = 128,
): MemoryRingSink {
    val sink = DefaultMemoryRingSink(maxEntriesCount, logPrinter)
    replaceSink(sink)
    return sink
}

private class DefaultMemoryRingSink(
    maxEntriesCount: Int,
    private val logPrinter: LogPrinter,
) : MemoryRingSink, LogSink {
    private val entries: RingArray<LogEntry> = RingArray(maxEntriesCount)

    override fun log(level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?) {
        val entry = LogEntry(level, tag, timestamp, message, err)
        entries.add(entry)
    }

    override fun getLogEntries(): List<String> =
        entries
            .map { logPrinter(it.level, it.tag, it.timestamp, it.message, it.err) }
            .toList()
}
