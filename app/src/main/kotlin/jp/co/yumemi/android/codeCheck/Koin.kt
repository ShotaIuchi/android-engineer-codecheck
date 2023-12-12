package jp.co.yumemi.android.codeCheck

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import jp.co.yumemi.android.codeCheck.data.datasource.GitHubDataSource
import jp.co.yumemi.android.codeCheck.data.datasource.GitHubDataSourceImpl
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepository
import jp.co.yumemi.android.codeCheck.data.repository.GitHubRepositoryImpl
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepositoryImpl
import jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * 通信タイムアウト
 */
const val HTTPCLIENT_TIMEOUT_MILLIS = 30000L

/**
 * Koin(DI)設定
 */
val coreModule = module {
    viewModel { GithubRepoViewModel(get(), get()) }

    single<GitHubDataSource> { GitHubDataSourceImpl(get()) }
    single<GitHubRepository> { GitHubRepositoryImpl(get()) }

    single<ResourceRepository> { ResourceRepositoryImpl(androidContext()) }

    single<HttpClientEngine> { Android.create() }
}

val httpClientModule = module {
    single {
        val client = HttpClient(get()) {
            // 例外ON
            expectSuccess = true

            // リソース種別
            install(ContentNegotiation) {
                json(
                    Json {
                        // JSONをフォーマット
                        prettyPrint = BuildConfig.DEBUG
                        // JSONを寛容モード
                        isLenient = true
                        // JSONでシリアライズする際にデータクラスに定義がない場合無視する
                        ignoreUnknownKeys = true
                    }
                )
            }

            // ログ設定
            if (BuildConfig.DEBUG) {
                install(Logging) {
                    this.logger = object : io.ktor.client.plugins.logging.Logger {
                        override fun log(message: String) {
                            Log.d("HttpClient", message)
                        }
                    }
                }
            }

            // タイムアウト設定
            install(HttpTimeout) {
                connectTimeoutMillis = HTTPCLIENT_TIMEOUT_MILLIS
                requestTimeoutMillis = HTTPCLIENT_TIMEOUT_MILLIS
                socketTimeoutMillis = HTTPCLIENT_TIMEOUT_MILLIS
            }
        }
        client
    }
}
