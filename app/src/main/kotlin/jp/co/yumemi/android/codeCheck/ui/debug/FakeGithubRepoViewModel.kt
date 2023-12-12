package jp.co.yumemi.android.codeCheck.ui.debug

import android.content.Context
import android.view.inputmethod.EditorInfo
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepositoryImpl
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import kotlinx.coroutines.runBlocking

class FakeGithubRepoViewModel(context: Context? = null) : GithubRepoViewModel(
    if (null != context) { ResourceRepositoryImpl(context) } else { FakeResourceRepository() },
    FakeGitHubRepository()
)

fun fakeCreateViewModel(context: Context): FakeGithubRepoViewModel {
    val viewModel = FakeGithubRepoViewModel(context)
    val searchJob = viewModel.onSearch("dummy", EditorInfo.IME_ACTION_SEARCH)
    runBlocking {
        searchJob?.join()
    }
    val selectedRepository = viewModel.repositoryList.value[1]
    viewModel.selectRepository(selectedRepository)
    return viewModel
}
