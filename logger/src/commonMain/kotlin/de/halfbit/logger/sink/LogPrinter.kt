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
        public val ShortNoTime: LogPrinter = PrettyLogPrinter(
            PrettyLogPrinter.Layout(
                timestamp = PrettyLogPrinter.Timestamp.NoTime,
                tagMaxLength = 22,
            )
        )

        public val Short: LogPrinter = PrettyLogPrinter(
            PrettyLogPrinter.Layout(
                timestamp = PrettyLogPrinter.Timestamp.Time,
                tagMaxLength = 22,
            )
        )
    }
}

private class PrettyLogPrinter(
    private val layout: Layout,
) : LogPrinter {

    enum class Timestamp {
        NoTime, Time
    }

    data class Layout(
        val timestamp: Timestamp,
        val tagMaxLength: Int,
    )

    override fun invoke(level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?): String =
        buildString {
            if (layout.timestamp == Timestamp.Time) {
                val iso8601String = timestamp.toString() // 2023-01-02T23:40:57.120Z
                val timeString = if (iso8601String.length >= 24) iso8601String.substring(11, 23) else iso8601String
                append(timeString)
                append(' ')
            }
            appendTag(tag, layout.tagMaxLength)
            append(' ')
            append(level.short)
            append(' ')
            if (message != null) {
                append(message)
            }
            if (err != null) {
                val timeLength = if (layout.timestamp == Timestamp.Time) 12 else 0
                val logLevelLength = 1
                val separatorLength = 1
                val indent = timeLength + separatorLength + layout.tagMaxLength +
                        separatorLength + logLevelLength + separatorLength
                append('\n')
                appendException(err, indent)
            }
        }
}

private fun StringBuilder.appendException(err: Throwable, indent: Int) {
    val stack = err.stackTraceToString().lines()
    stack.forEachIndexed { index, line ->
        append(" ".repeat(indent))
        append(line)
        if (index != stack.lastIndex) {
            append('\n')
        }
    }
}

private const val DOTS = ".."
private fun StringBuilder.appendTag(tag: String, maxLength: Int) {
    // see LogPrinterTest

    if (tag.length <= maxLength) {
        append(tag.padStart(maxLength, ' '))
        return
    }

    val leftIndex = maxLength / 2 - DOTS.length + 1
    val leftString = tag.substring(0, leftIndex)
    val rightLength = maxLength - leftIndex - DOTS.length
    val rightString = tag.substring(tag.length - rightLength)

    append(leftString)
    append(DOTS)
    append(rightString)
}
