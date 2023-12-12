package jp.co.yumemi.android.codeCheck.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * リポジトリタイトル
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param modifier 表示設定
 */
@Composable
fun RepoTitle(
    viewModel: GithubRepoViewModel,
    modifier: Modifier = Modifier
) {
    val repoData = viewModel.repositorySelectBindingData()

    Column(modifier) {
        Text(
            text = repoData.name,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    RepoTitle(viewModel)
}
