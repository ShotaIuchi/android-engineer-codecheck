package jp.co.yumemi.android.codeCheck.github

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.ContentConvertException
import io.ktor.utils.io.ByteReadChannel
import jp.co.yumemi.android.codeCheck.data.datasource.GitHubDataSource
import jp.co.yumemi.android.codeCheck.data.datasource.GitHubDataSourceImpl
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

class GitHubDataSourceTest : KoinTest {

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
                }
            )
        }
    }

    /**
     * ステータスコードを引数にKtorのMockEngineを作成
     * 異常系(APIコール失敗)用
     * @param statusCode ステータスコード
     */
    private fun initKoinException(statusCode: HttpStatusCode) {
        if (GlobalContext.getOrNull() != null) {
            stopKoin()
        }
        startKoin {
            modules(
                httpClientModule,
                module {
                    single<HttpClientEngine> {
                        MockEngine {
                            respondError(statusCode)
                        }
                    }
                    single<GitHubDataSource> { GitHubDataSourceImpl(get()) }
                }
            )
        }
    }

    @Test(expected = ContentConvertException::class)
    fun `searchRepository returns list of null`() = runBlocking {
        initKoinJson(null)
        val gitHubDataSource by inject<GitHubDataSource>()
        gitHubDataSource.searchRepository("testQuery")
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
        val gitHubDataSource by inject<GitHubDataSource>()
        gitHubDataSource.searchRepository("testQuery").let {
            assertTrue(it.isNotEmpty())
            assertGitHubRepoModel(it)
        }
        stopKoin()
    }

    @Test
    fun `searchRepository returns list of multiple`() = runBlocking {
        initKoinJson("github_repository_multiple.json")
        val gitHubDataSource by inject<GitHubDataSource>()
        gitHubDataSource.searchRepository("testQuery").let {
            assertTrue(it.isNotEmpty())
            assertGitHubRepoModel(it)
        }
        stopKoin()
    }

    @Test(expected = ClientRequestException::class)
    fun `searchRepository Client Error`() = runBlocking {
        initKoinException(HttpStatusCode.NotFound)
        val gitHubDataSource by inject<GitHubDataSource>()
        gitHubDataSource.searchRepository("testQuery")
        stopKoin()
    }

    @Test(expected = ServerResponseException::class)
    fun `searchRepository Server Error`() = runBlocking {
        initKoinException(HttpStatusCode.InternalServerError)
        val gitHubDataSource by inject<GitHubDataSource>()
        gitHubDataSource.searchRepository("testQuery")
        stopKoin()
    }
}
