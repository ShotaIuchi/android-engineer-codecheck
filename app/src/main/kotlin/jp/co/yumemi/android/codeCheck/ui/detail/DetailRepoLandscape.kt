package jp.co.yumemi.android.codeCheck.ui.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.LocalAppStyle
import jp.co.yumemi.android.codeCheck.ui.compose.RepoFiler
import jp.co.yumemi.android.codeCheck.ui.compose.RepoImage
import jp.co.yumemi.android.codeCheck.ui.compose.RepoInfo
import jp.co.yumemi.android.codeCheck.ui.compose.RepoTitle
import jp.co.yumemi.android.codeCheck.ui.debug.FakePreview
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * 詳細画面（横画面）
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param navigate ナビゲーションコールバック
 */
@Suppress("DestructuringDeclarationWithTooManyEntries")
@Composable
fun DetailRepoLandscape(
    viewModel: GithubRepoViewModel,
    navigate: (route: String) -> Unit
) {
    val appStyle = LocalAppStyle.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(appStyle.padding.window)
    ) {
        val (image, title, info, filer) = createRefs()

        RepoImage(
            viewModel = viewModel,
            modifier = Modifier.constrainAs(image) {}
        )

        RepoTitle(
            viewModel = viewModel,
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(image.end)
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                }
        )

        RepoFiler(
            viewModel = viewModel,
            navigate = navigate,
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color.Black)
                .padding(appStyle.padding.textBox)
                .constrainAs(filer) {
                    top.linkTo(image.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(image.start)
                    end.linkTo(info.start)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

        RepoInfo(
            viewModel = viewModel,
            modifier = Modifier
                .padding(start = appStyle.padding.textBox)
                .constrainAs(info) {
                    top.linkTo(image.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(widthDp = 1200, heightDp = 540)
@Composable
private fun PreviewDark() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(true) {
        DetailRepoLandscape(viewModel) {}
    }
}

@Suppress("UnusedPrivateMember")
@Preview(widthDp = 1200, heightDp = 540)
@Composable
private fun PreviewLight() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(false) {
        DetailRepoLandscape(viewModel) {}
    }
}
