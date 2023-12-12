/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck.ui

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.ContentConvertException
import io.ktor.utils.io.errors.IOException
import java.util.Date
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.models.GitHubFileModel
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import jp.co.yumemi.android.codeCheck.ui.models.DataBindingGitHubRepoModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * GitHubリポジトリデータ用ViewModel
 * @param resourceRepository リソース用リポジトリ
 * @param gitHubRepository GitHubリポジトリ
 */
open class GithubRepoViewModel(
    private val resourceRepository: ResourceRepository,
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    /**
     * 詳細情報用ViewModel
     * @param gitHubRepository GitHubリポジトリ
     */
    class GitHubRepoDetail(
        private val gitHubRepository: GitHubRepository,
    ) : ViewModel() {

        /**
         * リポジトリ情報
         */
        private val _detail = MutableStateFlow(GitHubRepoModel())
        val detail: StateFlow<GitHubRepoModel> = _detail.asStateFlow()

        /**
         * リポジトリパス情報
         */
        private val _fileList = MutableStateFlow<List<GitHubFileModel>>(emptyList())
        val fileList: StateFlow<List<GitHubFileModel>> = _fileList.asStateFlow()

        /**
         * リポジトリファイル情報
         */
        private val _fileUrl = MutableStateFlow("")
        val fileUrl: StateFlow<String> = _fileUrl.asStateFlow()

        /**
         * データクリア
         */
        fun clear() {
            _detail.value = GitHubRepoModel()
            _fileList.value = emptyList()
            _fileUrl.value = ""
        }

        /**
         * リポジトリ選択
         * @param repository 選択されたリポジトリ
         */
        fun setDetail(repository: GitHubRepoModel) {
            _detail.value = repository
        }

        /**
         * ファイル情報を取得
         * @param path リポジトリを起点とするパス
         * @return GitHubから取得したファイル情報
         */
        fun open(path: String = "") = viewModelScope.launch {
            _fileList.value = emptyList()
            _fileUrl.value = ""

            // コンテンツ情報取得
            val fileList = gitHubRepository.getContents(detail.value.name, path)
            if (fileList.singleOrNull() != null && fileList.first().downloadUrl != null) {
                _fileUrl.value = fileList.first().downloadUrl!!
            } else {
                _fileList.value = fileList
            }
        }
    }

    /**
     * リポジトリリスト
     */
    private val _repositoryList = MutableStateFlow<List<GitHubRepoModel>>(emptyList())
    val repositoryList: StateFlow<List<GitHubRepoModel>> = _repositoryList.asStateFlow()

    /**
     * リポジトリ情報
     */
    val repositoryDetail: GitHubRepoDetail = GitHubRepoDetail(gitHubRepository)

    /**
     * 検索エラー
     */
    private val _searchException = MutableStateFlow<Result<Unit>?>(null)
    val searchException: StateFlow<Result<Unit>?> = _searchException.asStateFlow()

    /**
     * 最終検索日時
     */
    private val _lastSearchDate = MutableStateFlow<Date?>(null)
    val lastSearchDate: StateFlow<Date?> = _lastSearchDate.asStateFlow()

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     */
    fun onSearch(query: String, action: Int): Job? {
        if (EditorInfo.IME_ACTION_SEARCH != action) {
            return null
        }
        return search(query)
    }

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     */
    @Suppress("TooGenericExceptionCaught")
    private fun search(inputText: String): Job = viewModelScope.launch {
        // 検索日時更新
        _lastSearchDate.value = Date()

        // 検索
        val gitHubRepository = try {
            gitHubRepository.searchRepository(inputText)
        } catch (e: Exception) {
            val message = handleException(e)
            _searchException.value = Result.failure(java.lang.RuntimeException(message, e))
            return@launch
        }

        // 取得失敗時は空リストを登録
        _repositoryList.value = gitHubRepository

        // 成功時
        _searchException.value = Result.success(Unit)
    }

    /**
     * リポジトリ選択
     * @param repository 選択されたリポジトリ
     */
    fun selectRepository(repository: GitHubRepoModel) {
        repositoryDetail.clear()
        repositoryDetail.setDetail(repository)
    }

    /**
     * ファイル情報を取得
     * @param path リポジトリを起点とするパス
     * @return GitHubから取得したファイル情報
     */
    @Suppress("TooGenericExceptionCaught")
    fun open(path: String = ""): Job = viewModelScope.launch {
        try {
            repositoryDetail.open(path)
        } catch (e: Exception) {
            val message = handleException(e)
            _searchException.value = Result.failure(java.lang.RuntimeException(message, e))
            return@launch
        }
    }

    /**
     * DataBinding用Model型に変換
     *  @param model GitHubリポジトリモデルの変換
     *  @return DataBinding用Model型
     */
    fun convertBindingData(model: GitHubRepoModel): DataBindingGitHubRepoModel =
        DataBindingGitHubRepoModel(
            model.name,
            model.owner.avatarUrl,
            resourceRepository.getString(R.string.written_language, model.language ?: ""),
            resourceRepository.getString(R.string.stargazers_count, model.stargazersCount),
            resourceRepository.getString(R.string.watchers_count, model.watchersCount),
            resourceRepository.getString(R.string.forks_count, model.forksCount),
            resourceRepository.getString(R.string.open_issues_count, model.openIssuesCount),
            model.htmlUrl
        )

    /**
     * DataBinding用Model型に変換
     *  @return DataBinding用Model型
     */
    fun repositorySelectBindingData(): DataBindingGitHubRepoModel =
        convertBindingData(repositoryDetail.detail.value)

    /**
     * エクセプションハンドリング
     * @param e エクセプション
     * @return エクセプションに対応する文字列
     */
    private fun handleException(e: Throwable): String {
        return when (e) {
            is ClientRequestException -> resourceRepository.getString(R.string.error_network)
            is ServerResponseException -> resourceRepository.getString(R.string.error_server)
            is HttpRequestTimeoutException -> resourceRepository.getString(R.string.error_timeout)
            is ContentConvertException -> resourceRepository.getString(R.string.error_converter)
            is IOException -> resourceRepository.getString(R.string.error_unexpected)
            else -> resourceRepository.getString(R.string.error_unexpected)
        }
    }
}
