/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.LoggableLevel.Everything
import kotlin.reflect.KClass

public abstract class LogTag {
    public abstract val name: String
    public abstract val loggableLevel: LoggableLevel
}

public fun namedTag(name: String, loggableLevel: LoggableLevel = Everything): LogTag =
    object : LogTag() {
        override val name: String get() = name
        override val loggableLevel: LoggableLevel get() = loggableLevel
    }

public fun classTag(clazz: KClass<*>, loggableLevel: LoggableLevel = Everything): LogTag =
    namedTag(name = clazz.simpleName ?: "", loggableLevel = loggableLevel)

public inline fun <reified T : Any> classTag(loggableLevel: LoggableLevel = Everything): LogTag =
    classTag(clazz = T::class, loggableLevel = loggableLevel)
