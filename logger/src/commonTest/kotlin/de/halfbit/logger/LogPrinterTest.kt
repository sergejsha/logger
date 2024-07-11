/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogPrinter
import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertContentEquals

class LogPrinterTest {

    private lateinit var memoryRingSink: MemoryRingSink

    @Test
    fun shortLogPrinter() {
        // given
        initializeLogger {
            memoryRingSink = registerMemoryRingSink(logPrinter = LogPrinter.Short)
            getClockNow = { Instant.parse("2024-06-18T23:40:57.120Z") }
        }

        // when
        d("Reader") { "Reading data" }
        i("Messenger") { "Sending message" }
        w("ReadableChannel") { "Sending message" }
        d("DefaultReadableChannel") { "Reading more data" }
        d("SimpleReadableChannel") { "Reading more data" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "23:40:57.120 D               Reader | Reading data",
            "23:40:57.120 I            Messenger | Sending message",
            "23:40:57.120 W      ReadableChannel | Sending message",
            "23:40:57.120 D DefaultRe..leChannel | Reading more data",
            "23:40:57.120 D SimpleRea..leChannel | Reading more data",
        )
        assertContentEquals(expected, actual)
    }
}
