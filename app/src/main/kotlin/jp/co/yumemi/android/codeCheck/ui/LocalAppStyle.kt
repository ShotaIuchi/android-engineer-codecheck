package jp.co.yumemi.android.codeCheck.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

/**
 * パディング定義
 */
object AppPadding {
    val window = 8.dp
    val card = 1.dp
    val image = 8.dp
    val textBox = 4.dp
}

/**
 * パディング定義
 */
object AppMargin {
    val card = 8.dp
}

/**
 * 余白定義
 */
object AppSpace {
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
}

/**
 * その他サイズ定義
 */
object AppSize {
    val shape = 2.dp
}

/**
 * ローカルスタイル
 */
data class LocalAppStyleSetting(
    val padding: AppPadding = AppPadding,
    val margin: AppMargin = AppMargin,
    val space: AppSpace = AppSpace,
    val size: AppSize = AppSize,
)

/**
 * ローカルスタイル定義
 */
val LocalAppStyle = staticCompositionLocalOf { LocalAppStyleSetting() }
