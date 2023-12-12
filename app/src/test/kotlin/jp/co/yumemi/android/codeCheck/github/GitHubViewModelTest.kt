package jp.co.yumemi.android.codeCheck.github

import android.view.inputmethod.EditorInfo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.data.models.Owner
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class TestResourceRepositoryImpl : ResourceRepository {
    override fun getString(id: Int): String = id.toString()
    override fun getString(id: Int, vararg formatArgs: Any): String =
        "${id}${formatArgs.joinToString("")}"
}

class TestGitHubRepository(private val resultList: List<GitHubRepoModel>) : GitHubRepository {
    override suspend fun searchRepository(inputText: String): List<GitHubRepoModel> {
        return resultList
    }
}

class GitHubViewModelTest : KoinTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    @OptIn(ExperimentalCoroutinesApi::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * APIコール結果を引数にKtorのMockEngineを作成
     * 正常系試験用
     * @param resultList APIコール結果
     */
    private fun initKoin(resultList: List<GitHubRepoModel>) {
        if (GlobalContext.getOrNull() != null) {
            stopKoin()
        }
        startKoin {
            modules(
                module {
                    single<ResourceRepository> { TestResourceRepositoryImpl() }
                    single<GitHubRepository> { TestGitHubRepository(resultList) }
                    viewModel { GithubRepoViewModel(get(), get()) }
                }
            )
        }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `searchRepository returns list of empty`() = runTest {
        initKoin(buildList {})
        val githubRepoViewModel by inject<GithubRepoViewModel>()
        githubRepoViewModel.onSearch("testQuery", EditorInfo.IME_ACTION_SEARCH)
        advanceUntilIdle()
        assertNotNull(githubRepoViewModel.repositoryList.value)
        assertTrue(githubRepoViewModel.repositoryList.value!!.isEmpty())
        stopKoin()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `searchRepository returns list of single`() = runTest {
        initKoin(createGitHubRepoModel(1))
        val githubRepoViewModel by inject<GithubRepoViewModel>()
        githubRepoViewModel.onSearch("testQuery", EditorInfo.IME_ACTION_SEARCH)
        advanceUntilIdle()
        githubRepoViewModel.repositoryList.value?.let {
            assertTrue(it.isNotEmpty())
            assertGitHubRepoModel(it)
        }
        stopKoin()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `searchRepository returns list of multiple`() = runTest {
        initKoin(createGitHubRepoModel(3))
        val githubRepoViewModel by inject<GithubRepoViewModel>()
        githubRepoViewModel.onSearch("testQuery", EditorInfo.IME_ACTION_SEARCH)
        advanceUntilIdle()
        githubRepoViewModel.repositoryList.value?.let {
            assertTrue(it.isNotEmpty())
            assertGitHubRepoModel(it)
        }
        stopKoin()
    }

    @Test
    fun convertBindingData() {
        initKoin(buildList {})
        val githubRepoViewModel by inject<GithubRepoViewModel>()
        val dataBinding = githubRepoViewModel.convertBindingData(
            GitHubRepoModel(
                "ACCOUNT/REPOSITORY",
                Owner("https://example.com/avatar.jpg"),
                "LANGUAGE",
                12345,
                67890,
                54321,
                9876,
                "https://github.com/yumemi-inc"
            )
        )
        dataBinding.let {
            assertEquals("ACCOUNT/REPOSITORY", it.name)
            assertEquals("https://example.com/avatar.jpg", it.ownerUrl)
            assertEquals("${R.string.written_language}LANGUAGE", it.language)
            assertEquals("${R.string.stargazers_count}${12345}", it.stargazersCount)
            assertEquals("${R.string.watchers_count}${67890}", it.watchersCount)
            assertEquals("${R.string.forks_count}${54321}", it.forksCount)
            assertEquals("${R.string.open_issues_count}${9876}", it.openIssuesCount)
            assertEquals("https://github.com/yumemi-inc", it.htmlUrl)
        }
        stopKoin()
    }
}
