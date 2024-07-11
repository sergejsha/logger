/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogSink
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@DslMarker
public annotation class LoggerDsl

private val lock = SynchronizedObject()

@LoggerDsl
public fun initializeLogger(
    block: LoggerBuilder.() -> Unit
) {
    synchronized(lock) {
        val builder = LoggerBuilder(
            sinks = logger.sinks,
            logLevel = logger.logLevel,
            getClockNow = Clock.System::now,
            removeDefaultSinks = !logger.initialized
        )

        block(builder)

        logger = Logger(
            sinks = builder.sinks,
            logLevel = builder.logLevel,
            getClockNow = builder.getClockNow,
        )
    }
}

public data class LoggerBuilder(
    var sinks: List<LogSink>,
    var logLevel: LogLevel,
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

    @PublishedApi
    internal fun maybeRemoveDefaultSinks() {
        if (removeDefaultSinks && !defaultSinksRemoved) {
            defaultSinksRemoved = true
            sinks = emptyList()
        }
    }
}
