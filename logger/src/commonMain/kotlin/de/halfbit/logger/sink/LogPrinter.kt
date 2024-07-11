/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink

import de.halfbit.logger.LogLevel
import kotlinx.datetime.Instant

public fun interface LogPrinter {
    public operator fun invoke(
        level: LogLevel,
        tag: String,
        timestamp: Instant,
        message: String?,
        err: Throwable?
    ): String

    public companion object {
        public val Short: LogPrinter = LogPrinter { level, tag, timestamp, message, err ->
            val iso8601String = timestamp.toString() // 2023-01-02T23:40:57.120Z
            val timeString = if (iso8601String.length == 24) iso8601String.substring(11, 23) else iso8601String
            buildString {
                append(timeString)
                append(' ')
                append(level.short)
                append(' ')
                appendTag(tag, 20)
                append(" | ")
                if (message != null) {
                    append(message)
                }
                if (err != null) {
                    append(", exception: \n")
                    append(err.stackTraceToString())
                }
            }
        }
    }
}

private const val DOTS = ".."

private fun StringBuilder.appendTag(tag: String, maxLength: Int) {
    // see LogPrinterTest.shortLogPrinter()

    if (tag.length <= maxLength) {
        append(tag.padStart(maxLength, ' '))
        return
    }

    val comp = if (tag.length % 2 == 0) 0 else 1
    val leftIndex = tag.length / 2 - DOTS.length + comp
    val leftString = tag.substring(0, leftIndex)
    val rightLength = maxLength - leftIndex - DOTS.length
    val rightString = tag.substring(tag.length - rightLength)

    append(leftString)
    append(DOTS)
    append(rightString)
}
