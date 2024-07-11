/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.LogLevel.*

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
