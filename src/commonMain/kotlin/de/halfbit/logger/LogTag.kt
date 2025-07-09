/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.LoggableLevel.Everything
import kotlin.reflect.KClass

// public

public sealed interface LogTag {
    public val name: String
    public val loggableLevel: LoggableLevel
}

public fun namedTag(name: String, loggableLevel: LoggableLevel = Everything): LogTag =
    DefaultLogTag(name, loggableLevel)

public fun classTag(clazz: KClass<*>, loggableLevel: LoggableLevel = Everything): LogTag =
    namedTag(name = clazz.simpleName ?: "", loggableLevel = loggableLevel)

public inline fun <reified T : Any> classTag(loggableLevel: LoggableLevel = Everything): LogTag =
    classTag(clazz = T::class, loggableLevel = loggableLevel)

// private

private class DefaultLogTag(
    override val name: String,
    override val loggableLevel: LoggableLevel
) : LogTag

