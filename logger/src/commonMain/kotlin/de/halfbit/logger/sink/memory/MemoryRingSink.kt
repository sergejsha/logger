/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.memory

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerBuilder
import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.LogSink
import kotlinx.datetime.Instant

public interface MemoryRingSink {
    public fun getLogEntries(): List<String>
}

public fun LoggerBuilder.registerMemoryRingSink(
    maxEntriesCount: Int = 128,
    logPrinter: LogPrinter = LogPrinter.Short,
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
