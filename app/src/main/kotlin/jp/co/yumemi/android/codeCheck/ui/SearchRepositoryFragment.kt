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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.models.GitHubRepository
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import jp.co.yumemi.android.codeCheck.databinding.FragmentSearchRepositoryBinding
import jp.co.yumemi.android.codeCheck.databinding.LayoutItemBinding

/**
 * GitHubリポジトリ検索画面
 */
class SearchRepositoryFragment : Fragment() {

    // 本当はDIで依存性注入したいがissueが先なので一旦は無理やり
    private val viewModel: GithubRepositoryViewModel by navGraphViewModels(R.id.nav_graph) {
        GithubRepositoryFactory(ResourceRepository(requireActivity().applicationContext))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSearchRepositoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_repository, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: GitHubRepository) {
                gotoRepositoryFragment(item)
            }
        })

        viewModel.searchException.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "取得失敗", Toast.LENGTH_SHORT).show()
                Log.e("SearchRepositoryFragment", "${error.message}")
            }
        }

        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (EditorInfo.IME_ACTION_SEARCH != action) {
                return@setOnEditorActionListener false
            }

            // 検索実行時にIMEを閉じる
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            // 入力ワードで検索
            editText.text.toString().let {
                viewModel.search(it)
            }
            return@setOnEditorActionListener true
        }

        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            this.addItemDecoration(dividerItemDecoration)
            this.adapter = adapter
        }

        return binding.root
    }

    fun gotoRepositoryFragment(item: GitHubRepository) {
        val direction = SearchRepositoryFragmentDirections
            .actionSearchRepositoryFragmentToDetailRepositoryFragment(item)
        findNavController().navigate(direction)
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<GitHubRepository>() {
    override fun areItemsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean {
        return oldItem == newItem
    }
}

private class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<GitHubRepository, CustomAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(
        private val binding: LayoutItemBinding,
        private val itemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GitHubRepository) {
            binding.repository = item
            binding.root.setOnClickListener { itemClickListener.itemClick(item) }
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun itemClick(item: GitHubRepository)
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
fun setItems(recyclerView: RecyclerView, items: List<GitHubRepository>?) {
    (recyclerView.adapter as? CustomAdapter)?.submitList(items)
}
