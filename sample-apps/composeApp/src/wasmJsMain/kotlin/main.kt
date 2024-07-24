/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import de.halfbit.logger.initializeLogger
import de.halfbit.logger.sink.wasmjs.registerConsoleLogSink
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    initializeLogger {
        registerConsoleLogSink()
    }

    ComposeViewport(document.body!!) {
        App()
    }
}
