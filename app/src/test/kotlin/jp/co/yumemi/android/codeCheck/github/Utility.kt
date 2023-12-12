package jp.co.yumemi.android.codeCheck.github

import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.data.models.Owner
import kotlin.test.assertEquals

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
 * テスト用GitHubRepoModelリストのアサート
 * @param items テスト対象のデータ
 */
fun assertGitHubRepoModel(items: List<GitHubRepoModel>) {
    for (index in 1..items.size) {
        val item = items[index - 1]
        assertEquals("ACCOUNT/REPOSITORY$index", item.name)
        assertEquals("https://example.com/avatar.jpg$index", item.owner.avatarUrl)
        assertEquals("LANGUAGE$index", item.language)
        assertEquals(index.toLong(), item.stargazersCount)
        assertEquals(index.toLong(), item.watchersCount)
        assertEquals(index.toLong(), item.forksCount)
        assertEquals(index.toLong(), item.openIssuesCount)
        assertEquals("https://github.com/yumemi-inc$index", item.htmlUrl)
    }
}
