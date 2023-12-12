package jp.co.yumemi.android.codeCheck.github

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.ContentConvertException
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jp.co.yumemi.android.codeCheck.data.datasource.GitHubDataSource
import jp.co.yumemi.android.codeCheck.data.datasource.GitHubDataSourceImpl
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepositoryImpl
import jp.co.yumemi.android.codeCheck.httpClientModule
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class GitHubRepositoryTest : KoinTest {

    /**
     * JSONファイルを引数にKtorのMockEngineを作成
     * 正常系試験用
     * @param jsonResourcesFileName JSONファイルリソース名
     */
    private fun initKoinJson(jsonResourcesFileName: String?) {
        if (GlobalContext.getOrNull() != null) {
            stopKoin()
        }
        startKoin {
            modules(
                httpClientModule,
                module {
                    single<HttpClientEngine> {
                        var content = ""
                        jsonResourcesFileName?.let {
                            val loader = Thread.currentThread().contextClassLoader!!
                            val resource = loader.getResource(jsonResourcesFileName)
                            content = resource.readText()
                        }
                        MockEngine {
                            respond(
                                content = ByteReadChannel(content),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                    }
                    single<GitHubDataSource> { GitHubDataSourceImpl(get()) }
                    single<GitHubRepository> { GitHubRepositoryImpl(get()) }
                }
            )
        }
    }

    @Test(expected = ContentConvertException::class)
    fun `searchRepository returns list of null`() = runBlocking {
        initKoinJson(null)
        val gitHubRepository by inject<GitHubRepository>()
        gitHubRepository.searchRepository("testQuery")
        stopKoin()
    }

    @Test
    fun `searchRepository returns list of empty`() = runBlocking {
        initKoinJson("github_repository_empty.json")
        val gitHubDataSource by inject<GitHubDataSource>()
        assertTrue(gitHubDataSource.searchRepository("testQuery").isEmpty())
        stopKoin()
    }

    @Test
    fun `searchRepository returns list of single`() = runBlocking {
        initKoinJson("github_repository_single.json")
        val gitHubRepository by inject<GitHubRepository>()
        gitHubRepository.searchRepository("testQuery").let {
            assertTrue(it.isNotEmpty())
            assertGitHubRepoModel(it)
        }
        stopKoin()
    }

    @Test
    fun `searchRepository returns list of multiple`() = runBlocking {
        initKoinJson("github_repository_multiple.json")
        val gitHubRepository by inject<GitHubRepository>()
        gitHubRepository.searchRepository("testQuery").let {
            assertTrue(it.isNotEmpty())
            assertGitHubRepoModel(it)
        }
        stopKoin()
    }

    @Test
    fun `searchRepository calls data-source`() = runBlocking {
        val mock = mockk<GitHubDataSource>()
        coEvery { mock.searchRepository(any()) } returns buildList {}
        GitHubRepositoryImpl(mock).searchRepository("testQuery")
        coVerify { mock.searchRepository(any()) }
    }
}
