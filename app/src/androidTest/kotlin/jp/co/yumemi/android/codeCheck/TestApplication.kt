package jp.co.yumemi.android.codeCheck

import android.app.Application
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository
import jp.co.yumemi.android.codeCheck.ui.debug.FakeGitHubRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestApplication)
            modules(
                coreModule,
                module {
                    single<GitHubRepository> { FakeGitHubRepository() }
                },
            )
        }
    }
}
