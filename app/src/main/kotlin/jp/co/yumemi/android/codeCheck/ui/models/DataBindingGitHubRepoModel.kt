package jp.co.yumemi.android.codeCheck.ui.models

data class DataBindingGitHubRepoModel(
    val name: String,
    val ownerUrl: String,
    val language: String,
    val stargazersCount: String,
    val watchersCount: String,
    val forksCount: String,
    val openIssuesCount: String,
    val htmlUrl: String
)
