import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.halfbit.composeapp.generated.resources.Res
import de.halfbit.composeapp.generated.resources.compose_multiplatform
import de.halfbit.logger.d
import de.halfbit.logger.e
import de.halfbit.logger.i
import de.halfbit.logger.w
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val TAG = "SampleApp"

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }

            Button(onClick = { d("${TAG}Debug") { "debug message" } }) {
                Text("Debug")
            }

            Button(onClick = { i("${TAG}Info") { "info message" } }) {
                Text("Info")
            }

            Button(onClick = { w("${TAG}Warning") { "warning message" } }) {
                Text("Warning")
            }

            Button(onClick = { e("${TAG}Error") { "error message" } }) {
                Text("Error")
            }

            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}