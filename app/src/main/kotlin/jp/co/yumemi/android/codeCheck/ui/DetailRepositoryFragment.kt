/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import coil.load
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import jp.co.yumemi.android.codeCheck.databinding.FragmentDetailRepositoryBinding

/**
 * GitHubリポジトリ詳細画面
 */
class DetailRepositoryFragment : Fragment(R.layout.fragment_detail_repository) {

    private val args: DetailRepositoryFragmentArgs by navArgs()

    // 本当はDIで依存性注入したいがissueが先なので一旦は無理やり
    private val viewModel: GithubRepositoryViewModel by navGraphViewModels(R.id.nav_graph) {
        GithubRepositoryFactory(ResourceRepository(requireActivity().applicationContext))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lastSearchDate.observe(viewLifecycleOwner) { lastSearchDate ->
            Log.d("検索した日時", lastSearchDate.toString())
        }

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
