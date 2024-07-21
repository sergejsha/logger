package de.halfbit.logger.sink

import de.halfbit.logger.LogLevel
import de.halfbit.logger.LoggerDsl
import de.halfbit.logger.sink.PrettyLogPrinter.Layout
import kotlinx.datetime.Instant

public fun buildLogPrinter(
    block: LogPrinterBuilder.() -> Unit
): LogPrinter =
    LogPrinterBuilder()
        .also(block)
        .build()

@LoggerDsl
public class LogPrinterBuilder
internal constructor() {
    private var timestamp: Layout.Timestamp? = null
    private var tag: Layout.Tag? = null
    private var logLevel: Layout.LogLevel? = null

    public fun timestamp(block: LogTimestampBuilder.() -> Unit) {
        timestamp = LogTimestampBuilder().also(block).timestamp
    }

    public fun tag(block: LogTagBuilder.() -> Unit) {
        tag = LogTagBuilder().also(block).tag
    }

    public fun logLevel(block: LogLevelBuilder.() -> Unit) {
        logLevel = LogLevelBuilder().also(block).logLevel
    }

    internal fun build(): LogPrinter =
        PrettyLogPrinter(
            layout = Layout(
                timestamp ?: defaultTimestamp(),
                tag ?: defaultTag(),
                logLevel ?: defaultLogLevel(),
            )
        )
}

@LoggerDsl
public class LogTimestampBuilder
internal constructor() {
    internal var timestamp: Layout.Timestamp? = null

    public fun none() {
        timestamp = Layout.Timestamp.None
    }

    public fun timeOnly() {
        timestamp = Layout.Timestamp.TimeOnly
    }
}

@LoggerDsl
public class LogTagBuilder
internal constructor() {
    internal var tag: Layout.Tag? = null

    /** Fixed-width tag pre-padded by a given character. Example: `...... TagName` */
    public fun padded(block: PaddedLogTagBuilder.() -> Unit) {
        tag = PaddedLogTagBuilder().also(block).build()
    }

    /** Tag with max length. Example: `TagName`  */
    public fun wrapped(block: WrappedLogTagBuilder.() -> Unit) {
        tag = WrappedLogTagBuilder().also(block).build()
    }
}

@LoggerDsl
public class PaddedLogTagBuilder
internal constructor() {
    private var length: Int = DEFAULT_TAG_LENGTH
    private var useSquareBrackets: Boolean = DEFAULT_USE_SQUARE_BRACKETS
    private var paddingChar: Char = DEFAULT_PADDING_CHAR

    public fun length(length: Int) {
        this.length = length
    }

    public fun useSquareBrackets(useSquareBrackets: Boolean) {
        this.useSquareBrackets = useSquareBrackets
    }

    public fun paddingChar(paddingChar: Char) {
        this.paddingChar = paddingChar
    }

    internal fun build(): Layout.Tag.Padded =
        Layout.Tag.Padded(length, useSquareBrackets, paddingChar)
}

@LoggerDsl
public class WrappedLogTagBuilder
internal constructor() {
    private var maxLength: Int = DEFAULT_TAG_LENGTH
    private var useSquareBrackets: Boolean = DEFAULT_USE_SQUARE_BRACKETS

    public fun length(maxLength: Int) {
        this.maxLength = maxLength
    }

    public fun useSquareBrackets(useSquareBrackets: Boolean) {
        this.useSquareBrackets = useSquareBrackets
    }

    internal fun build(): Layout.Tag.Wrapped =
        Layout.Tag.Wrapped(maxLength, useSquareBrackets)
}

@LoggerDsl
public class LogLevelBuilder
internal constructor() {
    internal var logLevel: Layout.LogLevel? = null

    public fun none() {
        logLevel = Layout.LogLevel.None
    }

    public fun short() {
        logLevel = Layout.LogLevel.Short
    }

    public fun long() {
        logLevel = Layout.LogLevel.Long
    }
}

private const val DOTS = ".."
private const val DEFAULT_TAG_LENGTH = 22
private const val DEFAULT_PADDING_CHAR = '.'
private const val DEFAULT_USE_SQUARE_BRACKETS = false

private fun defaultTimestamp() = Layout.Timestamp.TimeOnly
private fun defaultTag() =
    Layout.Tag.Padded(DEFAULT_TAG_LENGTH, DEFAULT_USE_SQUARE_BRACKETS, DEFAULT_PADDING_CHAR)

private fun defaultLogLevel() = Layout.LogLevel.Short

// see LogPrinterTest
internal class PrettyLogPrinter(
    private val layout: Layout,
) : LogPrinter {

    internal data class Layout(
        val timestamp: Timestamp,
        val tag: Tag,
        val logLevel: LogLevel,
    ) {
        enum class Timestamp { None, TimeOnly }
        enum class LogLevel { None, Short, Long }

        sealed interface Tag {
            val useSquareBrackets: Boolean

            data class Wrapped(
                var maxLength: Int,
                override val useSquareBrackets: Boolean,
            ) : Tag

            data class Padded(
                var length: Int,
                override val useSquareBrackets: Boolean,
                val paddingChar: Char,
            ) : Tag
        }
    }

    override fun invoke(
        level: LogLevel, tag: String, timestamp: Instant, message: String?, err: Throwable?
    ): String = buildString {
        var indent = 0
        indent += appendTimestamp(timestamp)
        indent += appendTag(tag)
        indent += appendLogLevel(level)
        appendMessage(message)
        appendError(err, indent)
    }

    private fun StringBuilder.appendMessage(message: String?) {
        if (message != null) {
            append(message)
        }
    }

    private fun StringBuilder.appendError(err: Throwable?, indent: Int) {
        if (err != null) {
            append('\n')
            val stack = err.stackTraceToString().lines()
            stack.forEachIndexed { index, line ->
                append(" ".repeat(indent))
                append(line)
                if (index != stack.lastIndex) {
                    append('\n')
                }
            }
        }
    }

    private fun StringBuilder.appendLogLevel(logLevel: LogLevel): Int =
        when (layout.logLevel) {
            Layout.LogLevel.None -> 0
            Layout.LogLevel.Short -> {
                append(logLevel.short)
                append(' ')
                2
            }

            Layout.LogLevel.Long -> {
                append(logLevel.long)
                append(' ')
                logLevel.long.length + 1
            }
        }

    private fun StringBuilder.appendTimestamp(timestamp: Instant): Int =
        when (layout.timestamp) {
            Layout.Timestamp.None -> 0
            Layout.Timestamp.TimeOnly -> {
                val iso8601String = timestamp.toString() // 2023-01-02T23:40:57.120Z
                val timeString = if (iso8601String.length >= 24) iso8601String.substring(
                    11,
                    23
                ) else iso8601String
                append(timeString)
                append(' ')
                timeString.length + 1
            }
        }

    private fun StringBuilder.appendTag(tag: String): Int {
        if (layout.tag.useSquareBrackets) {
            append('[')
        }

        val tagLength = when (val tagLayout = layout.tag) {
            is Layout.Tag.Padded -> {
                if (tag.length <= tagLayout.length) {
                    val indent = tagLayout.length - tag.length
                    if (indent > 1) {
                        append(tagLayout.paddingChar.toString().repeat(indent - 1))
                        append(' ')
                        append(tag)
                    } else {
                        append(tag.padStart(tagLayout.length, ' '))
                    }
                } else {
                    val leftIndex = tagLayout.length / 2 - DOTS.length + 1
                    val leftString = tag.substring(0, leftIndex)
                    val rightLength = tagLayout.length - leftIndex - DOTS.length
                    val rightString = tag.substring(tag.length - rightLength)

                    append(leftString)
                    append(DOTS)
                    append(rightString)
                }
                tagLayout.length
            }

            is Layout.Tag.Wrapped -> {
                if (tag.length > tagLayout.maxLength) {
                    append(tag.substring(0, tagLayout.maxLength))
                    tagLayout.maxLength
                } else {
                    append(tag)
                    tag.length
                }
            }
        }

        if (layout.tag.useSquareBrackets) {
            append(']')
        }
        append(' ')

        val squareBracketsLength = if (layout.tag.useSquareBrackets) 2 else 0
        return tagLength + squareBracketsLength + 1
    }
}
