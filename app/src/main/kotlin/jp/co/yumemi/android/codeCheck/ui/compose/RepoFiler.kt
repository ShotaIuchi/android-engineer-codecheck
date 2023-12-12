package jp.co.yumemi.android.codeCheck.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.LocalAppStyle
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * リポジトリファイラー
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param modifier 表示設定
 * @param navigate ナビゲーションコールバック
 */
@Composable
fun RepoFiler(
    viewModel: GithubRepoViewModel,
    modifier: Modifier = Modifier,
    navigate: (route: String) -> Unit,
) {
    val fileList by viewModel.repositoryDetail.fileList.collectAsState()
    val fileUrl by viewModel.repositoryDetail.fileUrl.collectAsState()

    val appStyle = LocalAppStyle.current

    Box(modifier.fillMaxSize()) {
        if (fileUrl.isNotEmpty()) {
            // ファイルプレビュー
            RepoCodePreview(fileUrl)
        } else {
            // ファイラー
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(fileList) { file ->
                    Card(
                        shape = RoundedCornerShape(appStyle.size.shape),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(appStyle.padding.card)
                            .clickable(
                                onClick = {
                                    navigate(file.path)
                                }
                            ),
                    ) {
                        Text(
                            text = file.path,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(appStyle.margin.card)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    RepoFiler(viewModel) {}
}
