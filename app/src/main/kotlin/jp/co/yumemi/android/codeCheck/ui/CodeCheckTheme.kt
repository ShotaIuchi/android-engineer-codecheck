package jp.co.yumemi.android.codeCheck.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

/**
 * テーマ定義
 * @param darkTheme ダークテーマ有効/無効
 * @param content 子要素のコンポーネント
 */
@Composable
fun CodeCheckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // テーマの設定
    val colorScheme =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // 旧バーション
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        } else {
            val darkColors = darkColorScheme()
            val lightColors = lightColorScheme()
            if (darkTheme) {
                darkColors
            } else {
                lightColors
            }
        }

    // スタイルはDI(Koin)使わずスコープ以下にデータ提供
    CompositionLocalProvider(LocalAppStyle provides LocalAppStyleSetting()) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
