package de.halfbit.logger.sink.memory

import de.halfbit.logger.d
import de.halfbit.logger.initializeLogger
import kotlin.test.Test
import kotlin.test.assertContentEquals

private const val TAG = "MemoryRingSinkTest"

class MemoryRingSinkTest {

    private lateinit var memoryRingSink: MemoryRingSink

    @Test
    fun maxEntriesNumberKeptInMemory() {

        // given
        initializeLogger {
            memoryRingSink = registerMemoryRingSink(
                maxEntriesCount = 5,
                logPrinter = { level, _, _, message, _ -> "${level.short} ${message.toString()}" }
            )
        }

        // when
        d(TAG) { "message 1" }
        d(TAG) { "message 2" }
        d(TAG) { "message 3" }
        d(TAG) { "message 4" }
        d(TAG) { "message 5" }

        // then
        val entries = memoryRingSink.getLogEntries()
        assertContentEquals(entries, listOf("D message 1", "D message 2", "D message 3", "D message 4", "D message 5"))
    }

    @Test
    fun entriesAreRotatedInMemory() {

        // given
        initializeLogger {
            memoryRingSink = registerMemoryRingSink(
                maxEntriesCount = 3,
                logPrinter = { level, _, _, message, _ -> "${level.short} ${message.toString()}" }
            )
        }

        // when
        d(TAG) { "message 1" }
        d(TAG) { "message 2" }
        d(TAG) { "message 3" }
        d(TAG) { "message 4" }
        d(TAG) { "message 5" }

        // then
        val entries = memoryRingSink.getLogEntries()
        assertContentEquals(entries, listOf("D message 3", "D message 4", "D message 5"))
    }
}
