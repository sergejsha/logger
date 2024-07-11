/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.LogLevel.*
import kotlinx.datetime.Clock

public inline fun d(tag: String, getMessage: () -> String) {
    if (logger.level <= Debug) {
        log(Debug, tag, getMessage(), null)
    }
}

public inline fun i(tag: String, getMessage: () -> String) {
    if (logger.level <= Info) {
        log(Info, tag, getMessage(), null)
    }
}

public inline fun w(tag: String, getMessage: () -> String) {
    if (logger.level <= Warning) {
        log(Warning, tag, getMessage(), null)
    }
}

public inline fun w(tag: String, err: Throwable, getMessage: () -> String) {
    if (logger.level <= Warning) {
        log(Warning, tag, getMessage(), err)
    }
}

public inline fun e(tag: String, err: Throwable, getMessage: () -> String) {
    if (logger.level <= Error) {
        log(Error, tag, getMessage(), err)
    }
}

public fun e(tag: String, err: Throwable) {
    if (logger.level <= Error) {
        log(Error, tag, null, err)
    }
}

@PublishedApi
internal fun log(level: LogLevel, tag: String, message: String?, err: Throwable?) {
    val timestamp = Clock.System.now()
    logger.sinks.forEach { sink ->
        sink.log(level, tag, timestamp, message, err)
    }
}
