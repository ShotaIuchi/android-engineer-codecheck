package jp.co.yumemi.android.codeCheck.data.repository

import jp.co.yumemi.android.codeCheck.data.models.GitHubFileModel
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

    /**
     * ファイル情報を取得
     * @param fullName リポジトリ名（名前/リポジトリ名）
     * @param path リポジトリを起点とするパス
     * @return GitHubから取得したファイル情報
     */
    suspend fun getContents(fullName: String, path: String = ""): List<GitHubFileModel>
}
