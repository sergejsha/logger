/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogSink
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.datetime.Instant

@DslMarker
public annotation class LoggerDsl

private val lock = SynchronizedObject()

/**
 * Initializes a new logger instance. Call this method when the app is created,
 * but before the logger is used.
 */
public fun initializeLogger(block: LoggerBuilder.() -> Unit) {
    initializeCurrentLogger(createInitialLogger(), block)
}

/**
 * Updates the current logger instance. Call this method when the app needs to
 * register more sinks outside the app's initialization method.
 */
public fun updateLogger(block: LoggerBuilder.() -> Unit) {
    initializeCurrentLogger(currentLogger, block)
}

private fun initializeCurrentLogger(
    logger: Logger,
    block: LoggerBuilder.() -> Unit
) {
    synchronized(lock) {
        val builder = LoggerBuilder(
            sinks = logger.sinks,
            loggableLevel = logger.loggableLevel,
            getClockNow = logger.getClockNow,
            removeDefaultSinks = !logger.initialized,
        )
        block(builder)
        currentLogger = Logger(
            sinks = builder.sinks,
            loggableLevel = builder.loggableLevel,
            getClockNow = builder.getClockNow,
            initialized = true,
        )
    }
}

@LoggerDsl
public data class LoggerBuilder(
    var sinks: List<LogSink>,
    var loggableLevel: LoggableLevel,
    internal var getClockNow: () -> Instant,
    private val removeDefaultSinks: Boolean,
) {
    private var defaultSinksRemoved = false

    public inline fun <reified S : LogSink> replaceSink(sink: S) {
        maybeRemoveDefaultSinks()
        sinks = sinks.filter { it !is S } + sink
    }

    public inline fun <reified S : LogSink> addSink(sink: S) {
        maybeRemoveDefaultSinks()
        sinks += sink
    }

    public fun removeAllSinks() {
        sinks = emptyList()
    }

    @PublishedApi
    internal fun maybeRemoveDefaultSinks() {
        if (removeDefaultSinks && !defaultSinksRemoved) {
            defaultSinksRemoved = true
            sinks = emptyList()
        }
    }
}
