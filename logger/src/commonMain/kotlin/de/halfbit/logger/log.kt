/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.LogLevel.*

public inline fun d(tag: String, getMessage: () -> String) {
    if (logger.logLevel <= Debug) {
        log(Debug, tag, getMessage(), null)
    }
}

public inline fun i(tag: String, getMessage: () -> String) {
    if (logger.logLevel <= Info) {
        log(Info, tag, getMessage(), null)
    }
}

public inline fun w(tag: String, getMessage: () -> String) {
    if (logger.logLevel <= Warning) {
        log(Warning, tag, getMessage(), null)
    }
}

public inline fun w(tag: String, err: Throwable, getMessage: () -> String) {
    if (logger.logLevel <= Warning) {
        log(Warning, tag, getMessage(), err)
    }
}

public inline fun e(tag: String, err: Throwable, getMessage: () -> String) {
    if (logger.logLevel <= Error) {
        log(Error, tag, getMessage(), err)
    }
}

public fun e(tag: String, err: Throwable) {
    if (logger.logLevel <= Error) {
        log(Error, tag, null, err)
    }
}

@PublishedApi
internal fun log(level: LogLevel, tag: String, message: String?, err: Throwable?) {
    val timestamp = logger.getClockNow()
    logger.sinks.forEach { sink ->
        sink.log(level, tag, timestamp, message, err)
    }
}
