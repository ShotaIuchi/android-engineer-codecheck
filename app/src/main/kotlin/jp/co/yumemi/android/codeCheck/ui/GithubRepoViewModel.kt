/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck.ui

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.ContentConvertException
import io.ktor.utils.io.errors.IOException
import java.util.Date
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import jp.co.yumemi.android.codeCheck.ui.models.DataBindingGitHubRepoModel
import kotlinx.coroutines.launch

/**
 * GitHubリポジトリデータ用ViewModel
 *  @see SearchRepoFragment
 *  @see DetailRepoFragment
 *  @param resourceRepository リソース用リポジトリ
 */
class GithubRepoViewModel(
    private val resourceRepository: ResourceRepository,
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    /**
     * リポジトリリスト
     */
    private val _repositoryList = MutableLiveData<List<GitHubRepoModel>>()
    val repositoryList: LiveData<List<GitHubRepoModel>>
        get() = _repositoryList

    /**
     * 検索エラー
     */
    private val _searchException = MutableLiveData<Result<Unit>>()
    val searchException: LiveData<Result<Unit>>
        get() = _searchException

    /**
     * 最終検索日時
     */
    private val _lastSearchDate = MutableLiveData<Date>()
    val lastSearchDate: LiveData<Date>
        get() = _lastSearchDate

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     */
    fun onSearch(query: String, action: Int): Boolean {
        if (EditorInfo.IME_ACTION_SEARCH != action) {
            return false
        }
        search(query)
        return true
    }

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     */
    private fun search(inputText: String) = viewModelScope.launch {
        // 検索日時更新
        _lastSearchDate.postValue(Date())

        // 検索
        val gitHubRepository = try {
            gitHubRepository.searchRepository(inputText)
        } catch (e: ClientRequestException) {
            val message = resourceRepository.getString(R.string.error_network)
            _searchException.postValue(Result.failure(RuntimeException(message, e)))
            return@launch
        } catch (e: ServerResponseException) {
            val message = resourceRepository.getString(R.string.error_server)
            _searchException.postValue(Result.failure(RuntimeException(message, e)))
            return@launch
        } catch (e: HttpRequestTimeoutException) {
            val message = resourceRepository.getString(R.string.error_timeout)
            _searchException.postValue(Result.failure(RuntimeException(message, e)))
            return@launch
        } catch (e: ContentConvertException) {
            val message = resourceRepository.getString(R.string.error_converter)
            _searchException.postValue(Result.failure(RuntimeException(message, e)))
            return@launch
        } catch (e: IOException) {
            val message = resourceRepository.getString(R.string.error_unexpected)
            _searchException.postValue(Result.failure(RuntimeException(message, e)))
            return@launch
        }

        // 取得失敗時は空リストを登録
        _repositoryList.postValue(gitHubRepository)
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
}
