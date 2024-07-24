/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import kotlin.test.Test
import kotlin.test.assertContentEquals

class LogTagTest {

    private lateinit var memoryRingSink: MemoryRingSink

    @Test
    fun defaultLogTag_logsEverything() {
        // given
        initializeLogger {
            loggableLevel = LoggableLevel.Everything
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, tag, _, message, _ -> "$tag ${level.short} $message" }
            )
        }
        val tag = namedTag("tag")

        // when
        d(tag) { "message 1" }
        i(tag) { "message 2" }
        w(tag) { "message 3" }
        e(tag) { "message 4" }
        e(tag, Exception()) { "message 5" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "tag D message 1",
            "tag I message 2",
            "tag W message 3",
            "tag E message 4",
            "tag E message 5",
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun nothingLogTag_logsNothing() {
        // given
        initializeLogger {
            loggableLevel = LoggableLevel.Everything
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, tag, _, message, _ -> "$tag ${level.short} $message" }
            )
        }
        val tag = namedTag("tag", LoggableLevel.Nothing)

        // when
        d(tag) { "message 1" }
        i(tag) { "message 2" }
        w(tag) { "message 3" }
        e(tag) { "message 4" }
        e(tag, Exception()) { "message 5" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = emptyList<String>()
        assertContentEquals(expected, actual)
    }

    @Test
    fun errorsOnlyLogTag_logsErrorsOnly() {
        // given
        initializeLogger {
            loggableLevel = LoggableLevel.Everything
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, tag, _, message, _ -> "$tag ${level.short} $message" }
            )
        }
        val tag = namedTag("tag", LoggableLevel.ErrorsOnly)

        // when
        d(tag) { "message 1" }
        i(tag) { "message 2" }
        w(tag) { "message 3" }
        e(tag) { "message 4" }
        e(tag, Exception()) { "message 5" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "tag E message 4",
            "tag E message 5",
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun classTag_usesSimpleClassNameAsTag() {
        // given
        initializeLogger {
            loggableLevel = LoggableLevel.Everything
            memoryRingSink = registerMemoryRingSink(
                logPrinter = { level, tag, _, message, _ -> "$tag ${level.short} $message" }
            )
        }
        val tag = classTag<LogTagTest>()

        // when
        d(tag) { "message 1" }
        i(tag) { "message 2" }
        w(tag) { "message 3" }
        w(tag, Exception()) { "message 4" }
        e(tag) { "message 5" }
        e(tag, Exception()) { "message 6" }
        e(tag, Exception())

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "LogTagTest D message 1",
            "LogTagTest I message 2",
            "LogTagTest W message 3",
            "LogTagTest W message 4",
            "LogTagTest E message 5",
            "LogTagTest E message 6",
            "LogTagTest E null",
        )
        assertContentEquals(expected, actual)
    }
}
