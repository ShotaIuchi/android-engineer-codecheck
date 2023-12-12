package jp.co.yumemi.android.codeCheck.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.LocalAppStyle
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * リポジトリスト
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param navigate ナビゲーションコールバック
 */
@Composable
fun RepoList(
    viewModel: GithubRepoViewModel,
    navigate: (route: String) -> Unit,
) {
    val repoList by viewModel.repositoryList.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(repoList) { repo ->
            RepoItem(repo) {
                viewModel.selectRepository(repo)
                // viewModel.getDirectory()
                navigate("detail")
            }
        }
    }
}

@Composable
private fun RepoItem(
    repo: GitHubRepoModel,
    onClick: () -> Unit
) {
    val appStyle = LocalAppStyle.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(appStyle.padding.card)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(appStyle.size.shape),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(appStyle.margin.card)
        ) {
            val (image, title) = createRefs()
            AsyncImage(
                model = repo.owner.avatarUrl,
                placeholder = painterResource(R.drawable.jetbrains),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = appStyle.padding.image)
                    .size(32.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
                contentScale = ContentScale.Crop
            )

            Text(
                text = repo.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(image.end)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    RepoList(viewModel) {}
}
