package jp.co.yumemi.android.codeCheck.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.codeCheck.R

/**
 * リポジトリ検索バー
 * @param onSearch 検索リスナー
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RepoSearchBar(onSearch: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                contentDescription = null
            )
        },
        onValueChange = { text = it },
        placeholder = { Text(stringResource(id = R.string.searchInputText_hint)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(text)
                keyboardController?.hide()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("testCodeCheckSearchBar"),
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    RepoSearchBar {}
}
