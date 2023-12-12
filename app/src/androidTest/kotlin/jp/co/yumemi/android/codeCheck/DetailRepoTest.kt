package jp.co.yumemi.android.codeCheck

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import jp.co.yumemi.android.codeCheck.ui.debug.fakeCreateViewModel
import jp.co.yumemi.android.codeCheck.ui.detail.DetailRepoHome
import org.junit.Rule
import org.junit.Test

class DetailRepoTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `表示内容確認_情報`() {
        composeTestRule.setContent {
            val viewModel = fakeCreateViewModel(LocalContext.current)
            DetailRepoHome(viewModel) {}
        }

        composeTestRule.onNodeWithText("ACCOUNT/REPOSITORY2").assertExists()
        composeTestRule.onNodeWithText("Written in LANGUAGE2").assertExists()
        composeTestRule.onNodeWithText("2 stars").assertExists()
        composeTestRule.onNodeWithText("2 watchers").assertExists()
        composeTestRule.onNodeWithText("2 forks").assertExists()
        composeTestRule.onNodeWithText("2 open issues").assertExists()
    }

    @Test
    fun `表示内容確認_リスト`() {
        composeTestRule.setContent {
            val viewModel = fakeCreateViewModel(LocalContext.current)
            DetailRepoHome(viewModel) {}
        }

        for (index in 1..3) {
            composeTestRule.onNodeWithText("path$index").assertExists()
        }
    }
}
