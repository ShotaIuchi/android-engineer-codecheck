package jp.co.yumemi.android.codeCheck.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubFileModel(
    val name: String = "",
    val path: String = "",
    val sha: String = "",
    val size: Int = 0,
    val url: String = "",
    @SerialName("html_url")
    val htmlUrl: String = "",
    @SerialName("git_url")
    val gitUrl: String = "",
    @SerialName("download_url")
    val downloadUrl: String? = null,
    val type: String = "dir"
) : java.io.Serializable
