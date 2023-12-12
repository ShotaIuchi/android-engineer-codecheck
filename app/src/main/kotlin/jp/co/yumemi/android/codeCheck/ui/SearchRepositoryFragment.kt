/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codeCheck.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codeCheck.R
import jp.co.yumemi.android.codeCheck.data.repository.ResourceRepository
import jp.co.yumemi.android.codeCheck.databinding.FragmentSearchRepositoryBinding

/**
 * GitHubリポジトリ検索画面
 */
class SearchRepositoryFragment : Fragment(R.layout.fragment_search_repository) {

    // 本当はDIで依存性注入したいがissueが先なので一旦は無理やり
    private val viewModel: GithubRepositoryViewModel by navGraphViewModels(R.id.nav_graph) {
        GithubRepositoryFactory(ResourceRepository(requireActivity().applicationContext))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchRepositoryBinding.bind(view)

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: GitHubRepository) {
                gotoRepositoryFragment(item)
            }
        })

        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    // 検索実行時にIMEを閉じる
                    val imm = requireContext()
                        .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(
                        editText.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )

                    // 入力ワードで検索
                    editText.text.toString().let {
                        viewModel.searchResults(it).observe(viewLifecycleOwner) { result ->
                            adapter.submitList(result)
                        }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }

    fun gotoRepositoryFragment(item: GitHubRepository) {
        val direction = SearchRepositoryFragmentDirections
            .actionSearchRepositoryFragmentToDetailRepositoryFragment(item)
        findNavController().navigate(direction)
    }
}

val diff_util = object : DiffUtil.ItemCallback<GitHubRepository>() {
    override fun areItemsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean {
        return oldItem == newItem
    }
}

class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<GitHubRepository, CustomAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(item: GitHubRepository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.repositoryNameView).text = item.name
        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }
    }
}
