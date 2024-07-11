/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        val stackTrace = memoryRingSink.getLogEntries().first().lines()
        assertTrue(stackTrace.size > 3)

        val actual = stackTrace.first()
        val expected = "23:40:57.120      LogExceptionsTest E Error message"
        assertEquals(expected, actual)
    }
}
