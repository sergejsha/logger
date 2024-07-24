/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.wasmjs

import de.halfbit.logger.*
import kotlin.test.Test

private const val TAG = "JavaScriptLogger"

class ConsoleLogSinkTest {

    @Test
    fun test() {
        initializeLogger {
            registerConsoleLogSink()
        }

        d(TAG) { "hello debug" }
        i(TAG) { "hello info" }
        w(TAG) { "hello warning" }
        w(TAG, Exception()) { "hello warning with exception" }
        e(TAG) { "hello error" }
        e(TAG, Exception()) { "hello error with exception" }

        // any way to apply assertions here?
    }
}