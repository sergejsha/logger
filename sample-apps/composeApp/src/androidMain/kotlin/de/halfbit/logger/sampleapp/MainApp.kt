/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger.sampleapp

import android.app.Application
import de.halfbit.logger.initializeLogger
import de.halfbit.logger.sink.android.registerAndroidLogSink

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeLogger {
            registerAndroidLogSink()
        }
    }
}