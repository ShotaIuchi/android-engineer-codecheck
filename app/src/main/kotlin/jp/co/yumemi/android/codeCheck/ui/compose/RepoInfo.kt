package jp.co.yumemi.android.codeCheck.ui.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.LocalAppStyle
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * リポジト詳細情報
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param modifier 表示設定
 */
@Composable
fun RepoInfo(
    viewModel: GithubRepoViewModel,
    modifier: Modifier = Modifier
) {
    val repoData = viewModel.repositorySelectBindingData()
    val appStyle = LocalAppStyle.current

    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = repoData.language,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(appStyle.space.small))

            Text(
                text = repoData.stargazersCount,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = repoData.watchersCount,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = repoData.forksCount,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = repoData.openIssuesCount,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    RepoInfo(viewModel = viewModel, modifier = Modifier.fillMaxSize())
}
