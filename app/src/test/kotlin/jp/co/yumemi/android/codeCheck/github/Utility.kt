package jp.co.yumemi.android.codeCheck.github

import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import kotlin.test.assertEquals

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
