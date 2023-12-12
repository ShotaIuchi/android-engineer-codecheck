package jp.co.yumemi.android.codeCheck.ui.detail

import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.debug.FakePreview
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * 詳細画面
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param path ファイルパス
 * @param navigate ナビゲーションコールバック
 */
@Composable
fun DetailRepoHome(
    viewModel: GithubRepoViewModel,
    path: String = "",
    navigate: (route: String) -> Unit,
) {
    LaunchedEffect(path) {
        viewModel.open(path)
    }

    LaunchedEffect(viewModel.lastSearchDate) {
        Log.d("DetailRepo", "最終検索日時: ${viewModel.lastSearchDate.value}")
    }

    val isVertical = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    if (isVertical) {
        DetailRepoPortrait(viewModel, navigate)
    } else {
        DetailRepoLandscape(viewModel, navigate)
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PreviewDark() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(true) {
        DetailRepoHome(viewModel) {}
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PreviewLight() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(false) {
        DetailRepoHome(viewModel) {}
    }
}
