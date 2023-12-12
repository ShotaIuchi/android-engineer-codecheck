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
import coil.load
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.databinding.FragmentDetailRepoBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * GitHubリポジトリ詳細画面
 */
class DetailRepoFragment : Fragment() {

    // GitHubリポジトリデータ用ViewModel
    private val viewModel: GithubRepoViewModel by activityViewModel()

    // Fragment引数
    private val args: DetailRepoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDetailRepoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_repo, container, false)
        binding.repository = viewModel.convertBindingData(args.item)
        binding.lifecycleOwner = viewLifecycleOwner

        // 検索日時出力
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
