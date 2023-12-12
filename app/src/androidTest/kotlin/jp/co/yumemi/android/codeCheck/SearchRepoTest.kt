package jp.co.yumemi.android.codeCheck

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import jp.co.yumemi.android.codeCheck.ui.compose.RepoList
import jp.co.yumemi.android.codeCheck.ui.compose.RepoSearchBar
import jp.co.yumemi.android.codeCheck.ui.debug.FakeGithubRepoViewModel
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel
import jp.co.yumemi.android.codeCheck.ui.search.SearchRepoHome
import org.junit.Rule
import org.junit.Test

class SearchRepoTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `検索バー`() {
        composeTestRule.setContent {
            RepoSearchBar {}
        }

        composeTestRule.onNodeWithTag("testCodeCheckSearchBar").assertExists()
    }

    @Test
    fun `検索結果表示`() {
        composeTestRule.setContent {
            val viewModel = fakeCreateViewModel(LocalContext.current)
            RepoList(viewModel) {}
        }

        for (index in 1..3) {
            composeTestRule.onNodeWithText("ACCOUNT/REPOSITORY$index").assertExists()
        }
    }

    @Test
    fun `検索画面`() {
        composeTestRule.setContent {
            val viewModel = FakeGithubRepoViewModel(null)
            SearchRepoHome(viewModel) {}
        }

        composeTestRule.onNodeWithTag("testCodeCheckSearchBar").performTextInput("query")
        composeTestRule.onNodeWithTag("testCodeCheckSearchBar").performImeAction()

        for (index in 1..3) {
            composeTestRule.onNodeWithText("ACCOUNT/REPOSITORY$index").assertExists()
        }
    }
}
