package jp.co.yumemi.android.codeCheck.data.repository

import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel

/**
 * GitHubリソースリポジトリ
 */
interface GitHubRepository {

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     */
    suspend fun searchRepository(inputText: String): List<GitHubRepoModel>
}
