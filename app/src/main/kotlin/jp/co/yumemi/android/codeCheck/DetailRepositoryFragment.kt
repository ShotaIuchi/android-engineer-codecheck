/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.codeCheck.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.codeCheck.databinding.FragmentDetailRepositoryBinding

/**
 * GitHubリポジトリ詳細画面
 */
class DetailRepositoryFragment : Fragment(R.layout.fragment_detail_repository) {

    private val args: DetailRepositoryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        FragmentDetailRepositoryBinding.bind(view).let { binding ->
            val item = args.item
            binding.ownerIconView.load(item.ownerIconUrl)
            binding.nameView.text = item.name
            binding.languageView.text = item.language
            binding.starsView.text = "${item.stargazersCount} stars"
            binding.watchersView.text = "${item.watchersCount} watchers"
            binding.forksView.text = "${item.forksCount} forks"
            binding.openIssuesView.text = "${item.openIssuesCount} open issues"
        }
    }
}