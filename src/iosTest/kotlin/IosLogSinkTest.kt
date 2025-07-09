/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sink.ios

import de.halfbit.logger.d
import de.halfbit.logger.e
import de.halfbit.logger.i
import de.halfbit.logger.initializeLogger
import de.halfbit.logger.sink.android.registerIosLogSink
import de.halfbit.logger.w
import kotlin.test.Test

private const val TAG = "IosLogger"

class IosLogSinkTest {

    @Test
    fun test() {
        initializeLogger {
            registerIosLogSink()
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
