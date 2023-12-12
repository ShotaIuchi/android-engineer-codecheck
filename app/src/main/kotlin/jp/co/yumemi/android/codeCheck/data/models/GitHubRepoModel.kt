package jp.co.yumemi.android.codeCheck.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepoModelList(
    val items: List<GitHubRepoModel>
) : java.io.Serializable

@Serializable
data class GitHubRepoModel(
    @SerialName("full_name")
    val name: String = "",
    val owner: Owner = Owner(),
    val language: String? = null,
    @SerialName("stargazers_count")
    val stargazersCount: Long = 0,
    @SerialName("watchers_count")
    val watchersCount: Long = 0,
    @SerialName("forks_count")
    val forksCount: Long = 0,
    @SerialName("open_issues_count")
    val openIssuesCount: Long = 0,
    @SerialName("html_url")
    val htmlUrl: String = ""
) : java.io.Serializable

@Serializable
data class Owner(
    @SerialName("avatar_url")
    val avatarUrl: String = ""
) : java.io.Serializable
