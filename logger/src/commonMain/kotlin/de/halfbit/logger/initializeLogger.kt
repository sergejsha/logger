/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.sink.LogSink
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

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
            logLevel = logger.level,
            removeDefaultSinks = !logger.initialized
        )

        block(builder)

        logger = Logger(
            sinks = builder.sinks,
            level = builder.logLevel,
        )
    }
}

public data class LoggerBuilder(
    var sinks: List<LogSink>,
    var logLevel: LogLevel,
    private val removeDefaultSinks: Boolean,
) {
    private var defaultSinksRemoved = false

    public inline fun <reified S : LogSink> addUniqueSink(sink: S) {
        maybeRemoveDefaultSinks()
        val alreadyRegisteredSink = sinks.find { it is S }
        check(alreadyRegisteredSink == null) {
            "A sink of given type is already registered:\n" +
                    "   Already registered sink: ${alreadyRegisteredSink}\n"
            "   Sink to register: $sink"
        }
        sinks += sink
    }

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
