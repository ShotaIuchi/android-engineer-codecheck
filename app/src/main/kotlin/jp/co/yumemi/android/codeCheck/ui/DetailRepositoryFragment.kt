/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
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
class DetailRepositoryFragment : Fragment() {

    private val args: DetailRepositoryFragmentArgs by navArgs()

    // 本当はDIで依存性注入したいがissueが先なので一旦は無理やり
    private val viewModel: GithubRepositoryViewModel by navGraphViewModels(R.id.nav_graph) {
        GithubRepositoryFactory(ResourceRepository(requireActivity().applicationContext))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDetailRepositoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_repository, container, false)
        binding.repository = args.item
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.lastSearchDate.observe(viewLifecycleOwner) { lastSearchDate ->
            Log.d("検索した日時", lastSearchDate.toString())
        }
        return binding.root
    }
}

/**
 * 画像リソースバインディング
 */
@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.load(url)
}
