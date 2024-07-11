/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

private const val TAG = "LogExceptionsTest"

class LogExceptionsTest {

    private lateinit var memoryRingSink: MemoryRingSink

    @Test
    fun logException() {
        // given
        initializeLogger {
            memoryRingSink = registerMemoryRingSink(LogPrinter.Short)
            getClockNow = { Instant.parse("2024-06-18T23:40:57.120Z") }
        }

        // when
        e(TAG, Exception()) { "Error message" }

        // then
        val actual = memoryRingSink.getLogEntries().first().lines().take(3).removeSourceReference()
        val expected = listOf(
            "23:40:57.120      LogExceptionsTest E Error message",
            "                                      java.lang.Exception",
            "                                      \tat de.halfbit.logger.LogExceptionsTest.logException"
        )
        assertEquals(expected, actual)
    }
}

private fun List<String>.removeSourceReference(): List<String> =
    map { line ->
        val startIndex = line.indexOf('(')
        if (startIndex > 0) line.substring(0, startIndex) else line
    }