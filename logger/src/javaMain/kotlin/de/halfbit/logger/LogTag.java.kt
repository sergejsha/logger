package de.halfbit.logger

import de.halfbit.logger.LoggableLevel.Everything

/**
 * ⚠️ Experimental automatic detection of tag's name based on class or
 * file the tag is created in. The method is rather slow, and it relies
 * on a platform-specific parsing of stack tracks which might fail at
 * runtime.
 *
 * It is recommended to use `namedTag()` or `classTag()` instead.
 */
public fun autoTag(loggableLevel: LoggableLevel = Everything): LogTag =
    namedTag(name = detectTag(), loggableLevel = loggableLevel)

private fun detectTag(): String {
    val element = Thread.currentThread().stackTrace.getOrNull(4)
        ?: throwAutoTagFailed()
    return element.cleanClassName
}

private val StackTraceElement.cleanClassName: String
    get() {
        val cleanClassName =
            if (methodName == "<clinit>" && !className.endsWith("Kt.kt")) {
                className.removeSuffix("Kt")
            } else {
                className
            }
        return cleanClassName
            .removePackage()
            .replace('$', '.')
    }

internal fun String.removePackage(): String {
    val packageIndex = lastIndexOf('.')
    return if (packageIndex < 0) this else substring(packageIndex + 1)
}

internal fun throwAutoTagFailed(): Nothing =
    error("autoTag() failed to detect a tag. Use namedTag() or classTag() instead.")
