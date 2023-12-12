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
    val name: String,
    val owner: Owner,
    val language: String?,
    @SerialName("stargazers_count")
    val stargazersCount: Long,
    @SerialName("watchers_count")
    val watchersCount: Long,
    @SerialName("forks_count")
    val forksCount: Long,
    @SerialName("open_issues_count")
    val openIssuesCount: Long,
    @SerialName("html_url")
    val htmlUrl: String
) : java.io.Serializable

@Serializable
data class Owner(
    @SerialName("avatar_url")
    val avatarUrl: String
) : java.io.Serializable
