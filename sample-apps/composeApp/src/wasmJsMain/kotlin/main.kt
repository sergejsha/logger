import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import de.halfbit.logger.d
import de.halfbit.logger.initializeLogger
import de.halfbit.logger.sink.wasmjs.registerConsoleLogSink
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    initializeLogger {
        registerConsoleLogSink()
    }

    d("kaboom") { "data !!!" }

    ComposeViewport(document.body!!) {
        App()
    }
}