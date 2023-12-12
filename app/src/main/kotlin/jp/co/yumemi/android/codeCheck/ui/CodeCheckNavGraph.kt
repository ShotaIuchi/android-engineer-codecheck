package jp.co.yumemi.android.codeCheck.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.co.yumemi.android.codeCheck.ui.detail.DetailRepoHome
import jp.co.yumemi.android.codeCheck.ui.search.SearchRepoHome
import org.koin.androidx.compose.koinViewModel

/**
 * ナビゲーション（ルーティング）
 * @param viewModel GitHubリポジトリデータ用ViewModel
 * @param navController ナビゲーション
 */
@Composable
fun CodeCheckNavGraph(
    viewModel: GithubRepoViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = "search") {
        // リスト画面
        composable(route = "search") {
            SearchRepoHome(viewModel) { route ->
                navController.navigate(route)
            }
        }
        // 詳細画面（ルート）
        composable(route = "detail") {
            DetailRepoHome(viewModel, "") {
                navController.navigate("detail/$it")
            }
        }
        // 詳細画面（ルート以外）
        composable(route = "detail/{path}") { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path") ?: ""
            DetailRepoHome(viewModel, path) {
                val encodedPath = Uri.encode(it)
                navController.navigate("detail/$encodedPath")
            }
        }
    }
}
