package jp.co.yumemi.android.codeCheck.ui.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jp.co.yumemi.android.codeCheck.ui.CodeCheckTheme

@Composable
fun FakePreview(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CodeCheckTheme(darkTheme) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            content()
        }
    }
}
