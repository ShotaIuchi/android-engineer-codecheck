/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepoModel
import jp.co.yumemi.android.codeCheck.databinding.FragmentSearchRepoBinding
import jp.co.yumemi.android.codeCheck.databinding.LayoutItemBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

typealias SearchRepoData = GitHubRepoModel

/**
 * GitHubリポジトリ検索画面
 */
class SearchRepoFragment : Fragment() {

    // GitHubリポジトリデータ用ViewModel
    private val viewModel: GithubRepoViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSearchRepoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_repo, container, false)
        binding.viewModel = viewModel
        binding.recyclerView.apply {
            val layoutManager = LinearLayoutManager(requireContext())
            this.layoutManager = layoutManager
            addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
            adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
                override fun itemClick(item: SearchRepoData) {
                    gotoRepositoryFragment(item)
                }
            })
        }
        binding.lifecycleOwner = viewLifecycleOwner

        // 検索実行時にIMEを閉じる
        viewModel.lastSearchDate.observe(viewLifecycleOwner) {
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                requireView().windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        // 検索エラー
        viewModel.searchException.observe(viewLifecycleOwner) { result ->
            result.onFailure { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                Log.e("SearchRepositoryFragment", error.stackTraceToString())
            }
        }
        return binding.root
    }

    fun gotoRepositoryFragment(item: SearchRepoData) {
        val direction = SearchRepoFragmentDirections
            .actionSearchRepositoryFragmentToDetailRepositoryFragment(item)
        findNavController().navigate(direction)
    }
}

private class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<SearchRepoData, CustomAdapter.ViewHolder>(diffUtil) {

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<SearchRepoData>() {
            override fun areItemsTheSame(
                oldItem: SearchRepoData,
                newItem: SearchRepoData
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: SearchRepoData,
                newItem: SearchRepoData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(
        private val binding: LayoutItemBinding,
        private val itemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchRepoData) {
            binding.repository = item
            binding.root.setOnClickListener { itemClickListener.itemClick(item) }
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun itemClick(item: SearchRepoData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_item,
                parent,
                false
            ),
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/**
 * リストデータバインディング
 */
@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, items: List<GitHubRepoModel>?) {
    (recyclerView.adapter as? CustomAdapter)?.submitList(items)
}
