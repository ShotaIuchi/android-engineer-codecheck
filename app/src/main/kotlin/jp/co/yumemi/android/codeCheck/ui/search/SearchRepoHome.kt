package jp.co.yumemi.android.codeCheck.ui.search

import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.compose.RepoList
import jp.co.yumemi.android.codeCheck.ui.compose.RepoSearchBar
import jp.co.yumemi.android.codeCheck.ui.debug.FakePreview
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel

/**
 * リスト画面
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param navigate ナビゲーションコールバック
 */
@Composable
fun SearchRepoHome(
    viewModel: GithubRepoViewModel,
    navigate: (route: String) -> Unit,
) {
    val context = LocalContext.current
    val searchException by viewModel.searchException.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        RepoSearchBar(
            onSearch = { query -> viewModel.onSearch(query, EditorInfo.IME_ACTION_SEARCH) }
        )

        searchException?.let { result ->
            when {
                result.isSuccess -> {
                    RepoList(viewModel, navigate)
                }
                result.isFailure -> {
                    LaunchedEffect(result) {
                        Toast.makeText(
                            context,
                            result.exceptionOrNull()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(
                            "SearchRepositoryFragment",
                            result.exceptionOrNull()?.stackTraceToString() ?: "Error"
                        )
                    }
                }
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PreviewDark() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(true) {
        SearchRepoHome(viewModel) {}
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PreviewLight() {
    val viewModel = fakeCreateViewModel(LocalContext.current)
    FakePreview(false) {
        SearchRepoHome(viewModel) {}
    }
}
