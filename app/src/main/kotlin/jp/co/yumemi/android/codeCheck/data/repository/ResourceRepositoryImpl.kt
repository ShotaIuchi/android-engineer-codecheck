package jp.co.yumemi.android.codeCheck.data.repository

import android.content.Context
import androidx.annotation.StringRes

/**
 * リソース用リポジトリ
 *  @see jp.co.yumemi.android.codeCheck.ui.GithubRepoViewModel
 *  @param context ApplicationContext
 */
class ResourceRepositoryImpl(private val context: Context) : ResourceRepository {

    /**
     * stringリソース取得
     * @param id stringのリソースID(R.string.xxx)
     * @return 文字列
     */
    override fun getString(@StringRes id: Int): String =
        context.getString(id)

    /**
     * stringリソース取得
     * @param id stringのリソースID(R.string.xxx)
     * @param formatArgs フォーマット引数
     * @return 文字列
     */
    override fun getString(@StringRes id: Int, vararg formatArgs: Any): String =
        context.getString(id, *formatArgs)
}
