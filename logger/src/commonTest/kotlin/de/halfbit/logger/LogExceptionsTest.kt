/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

private const val TAG = "LogExceptionsTest"

class LogExceptionsTest {

    private lateinit var memoryRingSink: MemoryRingSink

    @Test
    fun logExceptionAsError() {
        // given
        initializeLogger {
            memoryRingSink = registerMemoryRingSink(LogPrinter.Default)
            getClockNow = { Instant.parse("2024-06-18T23:40:57.120Z") }
        }

        // when
        e(TAG, Exception()) { "Error message" }

        // then
        val stackTrace = memoryRingSink.getLogEntries().first().lines()
        assertTrue(stackTrace.size > 3)

        val actualMessage = stackTrace[0]
        val expectedMessage = "23:40:57.120 .... LogExceptionsTest E Error message"
        assertEquals(expectedMessage, actualMessage)

        assertTrue("Cannot find exception in: $stackTrace") {
            stackTrace[1].contains("Exception") ||
                    stackTrace[1].contains("captureStack") ||
                    stackTrace[2].contains("Exception")
        }
    }

    @Test
    fun logExceptionAsWarning() {
        // given
        initializeLogger {
            memoryRingSink = registerMemoryRingSink(LogPrinter.Default)
            getClockNow = { Instant.parse("2024-06-18T23:40:57.120Z") }
        }

        // when
        w(TAG, Exception()) { "Warning message" }

        // then
        val stackTrace = memoryRingSink.getLogEntries().first().lines()
        assertTrue(stackTrace.size > 3)

        val actualMessage = stackTrace[0]
        val expectedMessage = "23:40:57.120 .... LogExceptionsTest W Warning message"
        assertEquals(expectedMessage, actualMessage)

        assertTrue("Cannot find exception in: $stackTrace") {
            stackTrace[1].contains("Exception") ||
                    stackTrace[1].contains("captureStack") ||
                    stackTrace[2].contains("Exception")
        }
    }
}
