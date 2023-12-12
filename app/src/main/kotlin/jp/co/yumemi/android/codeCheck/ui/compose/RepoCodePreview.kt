package jp.co.yumemi.android.codeCheck.ui.compose

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

/**
 * リポジトリファイル表示
 * @param url 表示するファイルのURL
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RepoCodePreview(url: String) {
    val htmlContent = """
        <html>
        <head>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.65.0/codemirror.min.css">
            <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.65.0/codemirror.min.js"></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.65.0/mode/javascript/javascript.min.js"></script>
        </head>
        <body>
            <textarea id="code" name="code"></textarea>
            <script>
                var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                    lineNumbers: true,
                    mode: "javascript"
                });
                fetch("$url").then(response => response.text()).then(data => {
                    editor.setValue(data);
                    // 行数計算
                    var lines = data.split('\n');
                    var lineCount = lines.length;
                    // 行数指定
                    editor.setSize('100%', 'calc(1.3em * ' + lineCount + ')');
                });
            </script>
        </body>
        </html>
    """.trimIndent()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    RepoCodePreview(
        "https://raw.githubusercontent.com/yumemi-inc/android-engineer-codecheck/main/README.md"
    )
}
