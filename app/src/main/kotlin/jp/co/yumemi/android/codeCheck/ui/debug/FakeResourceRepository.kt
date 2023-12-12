package jp.co.yumemi.android.codeCheck.ui.debug

import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository

class FakeResourceRepository : ResourceRepository {
    override fun getString(id: Int): String = "[fake]"

    override fun getString(id: Int, vararg formatArgs: Any): String = "[fake]"
}
