package jp.co.yumemi.android.codeCheck

import android.app.Application
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.data.models.Owner
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

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
 * テスト用GitHubRepository作成
 * @param リポジトリのスタブデータ
 */
class TestGitHubRepository(private val resultList: List<GitHubRepoModel>) : GitHubRepository {
    override suspend fun searchRepository(inputText: String): List<GitHubRepoModel> {
        return resultList
    }
}

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestApplication)
            modules(
                coreModule,
                module {
                    val resultList = createGitHubRepoModel(3)
                    single<GitHubRepository> { TestGitHubRepository(resultList) }
                },
            )
        }
    }
}
