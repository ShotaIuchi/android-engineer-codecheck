package jp.co.yumemi.android.codeCheck.data.repository

import android.content.Context
import androidx.annotation.StringRes

/**
 * リソース用リポジトリ
 * 参照：
 *  @see jp.co.yumemi.android.codeCheck.ui.GithubRepositoryViewModel
 *  @param context ApplicationContext
 */
class ResourceRepository(private val context: Context) {

    /**
     * stringリソース取得
     * @param id stringのリソースID(R.string.xxx)
     * @return 文字列
     */
    fun getString(@StringRes id: Int): String = context.getString(id)

    /**
     * stringリソース取得
     * @param id stringのリソースID(R.string.xxx)
     * @param formatArgs フォーマット引数
     * @return 文字列
     */
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String =
        context.getString(id, *formatArgs)
}
