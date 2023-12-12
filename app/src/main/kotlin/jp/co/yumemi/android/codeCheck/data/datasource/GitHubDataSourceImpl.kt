package jp.co.yumemi.android.codeCheck.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModelList

/**
 * GitHubリソースへのアクセス
 */
class GitHubDataSourceImpl(private val client: HttpClient) : GitHubDataSource {

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     * @throws io.ktor.client.plugins.ClientRequestException クライアント側のリクエストエラーが発生した場合
     * @throws io.ktor.client.plugins.HttpRequestTimeoutException サーバーからの応答エラーが発生した場合
     * @throws io.ktor.client.plugins.ServerResponseException HTTPリクエストがタイムアウトした場合
     * @throws io.ktor.utils.io.errors.IOException 入出力エラーが発生した場合
     */
    override suspend fun searchRepository(inputText: String): List<GitHubRepoModel> = client.get {
        api("search/repositories")
        parameter("q", inputText)
    }.body<GitHubRepoModelList>().items

    /**
     * GitHub用API生成
     * @param path APIパス
     * @return API
     */
    private fun HttpRequestBuilder.api(path: String) = url {
        takeFrom("https://api.github.com/")
        header("Accept", "application/vnd.github.v3+json")
        encodedPath = path
    }
}
