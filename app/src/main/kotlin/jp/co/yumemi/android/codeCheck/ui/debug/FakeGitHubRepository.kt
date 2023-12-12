package jp.co.yumemi.android.codeCheck.ui.debug

import jp.co.yumemi.android.codeCheck.data.models.GitHubFileModel
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.data.models.Owner
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository

/**
 * テスト用GitHubRepoModel作成
 * @param itemNumber 個数
 * @return テスト用GitHubRepoModel配列
 */
fun createGitHubRepoModel(itemNumber: Int): List<GitHubRepoModel> = buildList {
    for (index in 1..itemNumber) {
        add(
            GitHubRepoModel(
                "ACCOUNT/REPOSITORY$index",
                Owner("https://example.com/avatar.jpg$index"),
                "LANGUAGE$index",
                index.toLong(),
                index.toLong(),
                index.toLong(),
                index.toLong(),
                "https://github.com/yumemi-inc$index"
            )
        )
    }
}

/**
 * テスト用GitHubFileModel作成
 * @param itemNumber 個数
 * @return テスト用GitHubFileModel配列
 */
fun createGitHubFileModel(itemNumber: Int): List<GitHubFileModel> = buildList {
    for (index in 1..itemNumber) {
        add(
            GitHubFileModel(
                "name$index",
                "path$index",
                "sha$index",
                index,
                "url$index",
                "htmlUrl$index",
                "gitUrl$index",
                "downloadUrl$index",
                "type$index",
            )
        )
    }
}

class FakeGitHubRepository : GitHubRepository {
    @Suppress("MagicNumber")
    override suspend fun searchRepository(inputText: String): List<GitHubRepoModel> =
        createGitHubRepoModel(3)

    @Suppress("MagicNumber")
    override suspend fun getContents(fullName: String, path: String): List<GitHubFileModel> =
        createGitHubFileModel(3)
}
