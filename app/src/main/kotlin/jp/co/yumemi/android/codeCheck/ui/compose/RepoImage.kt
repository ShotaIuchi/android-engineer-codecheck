package jp.co.yumemi.android.codeCheck.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.LocalAppStyle
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * リポジトリイメージ
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param modifier 表示設定
 * @param size 画像サイズ
 */
@Composable
fun RepoImage(
    viewModel: GithubRepoViewModel,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
) {
    val repoData = viewModel.repositorySelectBindingData()

    Box(modifier) {
        AsyncImage(
            model = repoData.ownerUrl,
            placeholder = painterResource(R.drawable.jetbrains),
            contentDescription = null,
            modifier = Modifier
                .padding(LocalAppStyle.current.padding.image)
                .size(size)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    RepoImage(viewModel)
}
