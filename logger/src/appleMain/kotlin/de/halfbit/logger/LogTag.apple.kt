/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

import de.halfbit.logger.LoggableLevel.Everything
import platform.Foundation.NSThread

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
    val callstack = NSThread.callStackSymbols
    val element = callstack.getOrNull(3)?.toString() ?: throwAutoTagFailed()
    return element.cleanClassName
}

private val String.cleanClassName: String
    get() {
        val endIndex = indexOf('#')
        if (endIndex == -1) {
            throwAutoTagFailed()
        }

        var cleanClassName =
            substring(0, endIndex)
                .removeSuffix(".Companion")

        if (cleanClassName.endsWith(".\$init_global")) {
            cleanClassName = cleanClassName.removeSuffix(".\$init_global")
            val startIndex = cleanClassName.indexOf(':')
            if (startIndex == -1) {
                throwAutoTagFailed()
            }
            cleanClassName = cleanClassName.substring(startIndex + 1)
        } else {
            cleanClassName = cleanClassName.removePackage()
        }

        return cleanClassName
    }

private fun String.removePackage(): String {
    val packageIndex = lastIndexOf('.')
    return if (packageIndex < 0) this else substring(packageIndex + 1)
}

private fun throwAutoTagFailed(): Nothing =
    error("autoTag() failed to detect a tag. Use namedTag() or classTag() instead.")
