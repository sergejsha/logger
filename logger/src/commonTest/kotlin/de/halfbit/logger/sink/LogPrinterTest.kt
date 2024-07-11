/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink

import de.halfbit.logger.d
import de.halfbit.logger.i
import de.halfbit.logger.initializeLogger
import de.halfbit.logger.sink.memory.MemoryRingSink
import de.halfbit.logger.sink.memory.registerMemoryRingSink
import de.halfbit.logger.w
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertContentEquals

class LogPrinterTest {

    private lateinit var memoryRingSink: MemoryRingSink

    @Test
    fun shortLogPrinterNoTime() {
        // given
        initializeLogger {
            memoryRingSink = registerMemoryRingSink(logPrinter = LogPrinter.ShortNoTime)
        }

        // when
        d("Reader") { "Reading data" }
        i("Messenger") { "Sending message" }
        w("ReadableChannel") { "Sending message" }
        d("DefaultReadableRemoteChannel") { "Reading more data" }
        d("SimpleReadableRemoteChannel") { "Reading even more data" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "                Reader D Reading data",
            "             Messenger I Sending message",
            "       ReadableChannel W Sending message",
            "DefaultRea..oteChannel D Reading more data",
            "SimpleRead..oteChannel D Reading even more data",
        )
        assertContentEquals(expected, actual)
    }

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
        d("DefaultReadableRemoteChannel") { "Reading more data" }
        d("SimpleReadableRemoteChannel") { "Reading even more data" }

        // then
        val actual = memoryRingSink.getLogEntries()
        val expected = listOf(
            "23:40:57.120                 Reader D Reading data",
            "23:40:57.120              Messenger I Sending message",
            "23:40:57.120        ReadableChannel W Sending message",
            "23:40:57.120 DefaultRea..oteChannel D Reading more data",
            "23:40:57.120 SimpleRead..oteChannel D Reading even more data",
        )
        assertContentEquals(expected, actual)
    }
}
