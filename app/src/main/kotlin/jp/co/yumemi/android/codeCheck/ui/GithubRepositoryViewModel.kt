/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import java.util.Date
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepository
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * GitHubリポジトリデータ用ViewModel
 * 利用箇所：
 *  @see SearchRepositoryFragment
 *  @see DetailRepositoryFragment
 *  @param resourceRepository リソース用リポジトリ
 */
class GithubRepositoryViewModel(private val resourceRepository: ResourceRepository) : ViewModel() {

    /**
     * リポジトリリスト
     */
    private val _repositoryList = MutableLiveData<List<GitHubRepository>>()
    val repositoryList: LiveData<List<GitHubRepository>>
        get() = _repositoryList

    /**
     * 検索エラー
     */
    private val _searchException = MutableLiveData<Exception>()
    val searchException: LiveData<Exception>
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
    fun search(inputText: String) = viewModelScope.launch {
        val client = HttpClient(Android)

        val response: HttpResponse = try {
            client.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", inputText)
            }
        } catch (e: ClientRequestException) {
            _searchException.postValue(e)
            return@launch
        } catch (e: ServerResponseException) {
            _searchException.postValue(e)
            return@launch
        } catch (e: HttpRequestTimeoutException) {
            _searchException.postValue(e)
            return@launch
        } catch (e: IOException) {
            _searchException.postValue(e)
            return@launch
        }

        val jsonBody = JSONObject(response.receive<String>())

        val jsonItems = jsonBody.optJSONArray("items")

        val items = mutableListOf<GitHubRepository>()

        // 取得したデータを検索結果リストに詰める
        jsonItems?.let { jsonArray ->
            for (i in 0 until jsonArray.length()) {
                val jsonItem = jsonArray.optJSONObject(i)

                val name = jsonItem.optString("full_name")
                val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url") ?: ""
                val language = jsonItem.optString("language")
                val stargazersCount = jsonItem.optLong("stargazers_count")
                val watchersCount = jsonItem.optLong("watchers_count")
                val forksCount = jsonItem.optLong("forks_count")
                val openIssuesCount = jsonItem.optLong("open_issues_count")

                items.add(
                    GitHubRepository(
                        name = name,
                        ownerIconUrl = ownerIconUrl,
                        language = resourceRepository
                            .getString(R.string.written_language, language),
                        stargazersCount = stargazersCount,
                        watchersCount = watchersCount,
                        forksCount = forksCount,
                        openIssuesCount = openIssuesCount
                    )
                )
            }
        }

        // 取得失敗時も日時更新
        _lastSearchDate.postValue(Date())

        // 取得失敗時は空リストを登録
        _repositoryList.postValue(items)
    }
}

/**
 * GitHubリポジトリデータViewModel用Factory
 */
class GithubRepositoryFactory(private val resourceRepository: ResourceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubRepositoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubRepositoryViewModel(resourceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
