package jp.co.yumemi.android.codeCheck.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * コンポーネントのエントリポイント
 * @param viewModel GitHubリポジトリデータ用ViewModel
 */
@Composable
fun CodeCheckApp(viewModel: GithubRepoViewModel = koinViewModel()) {
    CodeCheckTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CodeCheckNavGraph(viewModel = viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    CodeCheckApp(viewModel)
}
