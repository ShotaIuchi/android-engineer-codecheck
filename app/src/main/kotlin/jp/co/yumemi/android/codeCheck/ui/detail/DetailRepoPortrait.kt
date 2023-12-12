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
 * 詳細画面（縦画面）
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param navigate ナビゲーションコールバック
 */
@Suppress("DestructuringDeclarationWithTooManyEntries")
@Composable
fun DetailRepoPortrait(
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
            size = 64.dp,
            modifier = Modifier.constrainAs(image) {
                top.linkTo(parent.top)
                bottom.linkTo(filer.top)
                start.linkTo(parent.start)
            }
        )

        RepoTitle(
            viewModel = viewModel,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(image.end)
                end.linkTo(parent.end)
            }
        )

        RepoInfo(
            viewModel = viewModel,
            modifier = Modifier.constrainAs(info) {
                top.linkTo(title.bottom)
                end.linkTo(parent.end)
            }
        )

        RepoFiler(
            viewModel = viewModel,
            navigate = navigate,
            modifier = Modifier
                .border(1.dp, Color.Black)
                .padding(appStyle.padding.textBox)
                .constrainAs(filer) {
                    top.linkTo(info.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PreviewDark() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(true) {
        DetailRepoPortrait(viewModel) {}
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PreviewLight() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(false) {
        DetailRepoPortrait(viewModel) {}
    }
}
