package jp.co.yumemi.android.codeCheck.data.repository

import androidx.annotation.StringRes

/**
 * リソース用リポジトリ
 */
interface ResourceRepository {

    /**
     * stringリソース取得
     * @param id stringのリソースID(R.string.xxx)
     * @return 文字列
     */
    fun getString(@StringRes id: Int): String

    /**
     * stringリソース取得
     * @param id stringのリソースID(R.string.xxx)
     * @param formatArgs フォーマット引数
     * @return 文字列
     */
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String
}
