package jp.co.yumemi.android.codeCheck.data.repository

import jp.co.yumemi.android.codeCheck.data.datasource.GitHubDataSource
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel

/**
 * GitHubリソースリポジトリ
 * @param gitHubDataSource GitHubリソースへのアクセス
 */
class GitHubRepositoryImpl(private val gitHubDataSource: GitHubDataSource) : GitHubRepository {

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     * @throws io.ktor.client.plugins.ClientRequestException クライアント側のリクエストエラーが発生した場合
     * @throws io.ktor.client.plugins.HttpRequestTimeoutException サーバーからの応答エラーが発生した場合
     * @throws io.ktor.client.plugins.ServerResponseException HTTPリクエストがタイムアウトした場合
     * @throws io.ktor.utils.io.errors.IOException 入出力エラーが発生した場合
     */
    override suspend fun searchRepository(inputText: String): List<GitHubRepoModel> =
        gitHubDataSource.searchRepository(inputText)
}
