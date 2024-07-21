/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.LogLevel.*
import de.halfbit.logger.sink.LogSink
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.datetime.Instant

public inline fun d(tag: String, getMessage: () -> String) {
    if (currentLogger.loggableLevel.weight <= Debug.weight) {
        log(Debug, tag, getMessage(), null)
    }
}

public inline fun i(tag: String, getMessage: () -> String) {
    if (currentLogger.loggableLevel.weight <= Info.weight) {
        log(Info, tag, getMessage(), null)
    }
}

public inline fun w(tag: String, getMessage: () -> String) {
    if (currentLogger.loggableLevel.weight <= Warning.weight) {
        log(Warning, tag, getMessage(), null)
    }
}

public inline fun w(tag: String, err: Throwable, getMessage: () -> String) {
    if (currentLogger.loggableLevel.weight <= Warning.weight) {
        log(Warning, tag, getMessage(), err)
    }
}

public inline fun e(tag: String, getMessage: () -> String) {
    if (currentLogger.loggableLevel.weight <= Error.weight) {
        log(Error, tag, getMessage(), null)
    }
}

public inline fun e(tag: String, err: Throwable, getMessage: () -> String) {
    if (currentLogger.loggableLevel.weight <= Error.weight) {
        log(Error, tag, getMessage(), err)
    }
}

public fun e(tag: String, err: Throwable) {
    if (currentLogger.loggableLevel.weight <= Error.weight) {
        log(Error, tag, null, err)
    }
}

@PublishedApi
internal fun log(level: LogLevel, tag: String, message: String?, err: Throwable?) {
    val timestamp = currentLogger.getClockNow()
    currentLogger.sinks.forEach { sink ->
        sink.log(level, tag, timestamp, message, err)
    }
}

private val lock = SynchronizedObject()

/**
 * Initializes a new logger instance. Call this method when the app is created,
 * but before the logger is used.
 */
public fun initializeLogger(builder: LoggerBuilder.() -> Unit) {
    initializeCurrentLogger(createInitialLogger(), builder)
}

/**
 * Updates the current logger instance. Call this method when the app needs to
 * register more sinks outside the app's initialization method.
 */
public fun updateLogger(builder: LoggerBuilder.() -> Unit) {
    initializeCurrentLogger(currentLogger, builder)
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

@DslMarker
internal annotation class LoggerDsl

@LoggerDsl
public data class LoggerBuilder
internal constructor(
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
