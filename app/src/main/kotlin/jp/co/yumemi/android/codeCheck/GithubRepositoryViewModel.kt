/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import java.util.Date
import jp.co.yumemi.android.codeCheck.TopActivity.Companion.lastSearchDate
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

/**
 * GitHubリポジトリデータ用ViewModel
 * 利用箇所：
 *  @see SearchRepositoryFragment
 *  @see DetailRepositoryFragment
 */
class GithubRepositoryViewModel(
    val context: Context
) : ViewModel() {

    /**
     * {inputText}を入力にGitHubからリポジトリを検索＆取得する
     * @param inputText 検索文字列
     * @return GitHubから取得したリポジトリ一覧
     */
    fun searchResults(inputText: String): LiveData<List<GitHubRepository>> = liveData {
        val client = HttpClient(Android)

        val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", inputText)
        }

        val jsonBody = JSONObject(response.receive<String>())

        val jsonItems = jsonBody.optJSONArray("items")!!

        val items = mutableListOf<GitHubRepository>()

        // 取得したデータを検索結果リストに詰める
        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.optJSONObject(i)!!
            val name = jsonItem.optString("full_name")
            val ownerIconUrl = jsonItem.optJSONObject("owner")!!.optString("avatar_url")
            val language = jsonItem.optString("language")
            val stargazersCount = jsonItem.optLong("stargazers_count")
            val watchersCount = jsonItem.optLong("watchers_count")
            val forksCount = jsonItem.optLong("forks_conut")
            val openIssuesCount = jsonItem.optLong("open_issues_count")

            items.add(
                GitHubRepository(
                    name = name,
                    ownerIconUrl = ownerIconUrl,
                    language = context.getString(R.string.written_language, language),
                    stargazersCount = stargazersCount,
                    watchersCount = watchersCount,
                    forksCount = forksCount,
                    openIssuesCount = openIssuesCount
                )
            )
        }

        lastSearchDate = Date()
        emit(items)
    }
}

@Parcelize
data class GitHubRepository(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable
