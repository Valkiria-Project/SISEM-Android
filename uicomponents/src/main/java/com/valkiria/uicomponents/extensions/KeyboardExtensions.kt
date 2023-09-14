import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun HideKeyboard() {
    LocalSoftwareKeyboardController.current?.hide()
}

@Composable
fun ShowKeyboard() {
    LocalSoftwareKeyboardController.current?.show()
}
