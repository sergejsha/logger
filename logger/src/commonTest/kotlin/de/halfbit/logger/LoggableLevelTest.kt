package de.halfbit.logger

import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import kotlin.test.Test
import kotlin.test.assertContentEquals

private const val TAG = "Tag"

class LoggableLevelTest {

    private lateinit var memoryRingSink: MemoryRingSink

    @Test
    fun debugLevelLogsDebugInfoWarnAndError() {

        // given
        initializeLogger {
            loggableLevel = LogLevel.Debug
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, _, _, message, _ -> "${level.short} $message" }
            )
        }

        // when
        d(TAG) { "message 1" }
        i(TAG) { "message 2" }
        w(TAG) { "message 3" }
        e(TAG, Exception()) { "message 4" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "D message 1",
            "I message 2",
            "W message 3",
            "E message 4",
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun infoLevelLogsInfoWarnAndError() {

        // given
        initializeLogger {
            loggableLevel = LogLevel.Info
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, _, _, message, _ -> "${level.short} $message" }
            )
        }

        // when
        d(TAG) { "message 1" }
        i(TAG) { "message 2" }
        w(TAG) { "message 3" }
        e(TAG, Exception()) { "message 4" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "I message 2",
            "W message 3",
            "E message 4",
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun warnLevelLogsWarnAndError() {

        // given
        initializeLogger {
            loggableLevel = LogLevel.Warning
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, _, _, message, _ -> "${level.short} $message" }
            )
        }

        // when
        d(TAG) { "message 1" }
        i(TAG) { "message 2" }
        w(TAG) { "message 3" }
        e(TAG, Exception()) { "message 4" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "W message 3",
            "E message 4",
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun errorLevelLogsErrorOnly() {

        // given
        initializeLogger {
            loggableLevel = LogLevel.Error
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, _, _, message, _ -> "${level.short} $message" }
            )
        }

        // when
        d(TAG) { "message 1" }
        i(TAG) { "message 2" }
        w(TAG) { "message 3" }
        e(TAG, Exception()) { "message 4" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "E message 4",
        )
        assertContentEquals(expected, actual)
    }
}