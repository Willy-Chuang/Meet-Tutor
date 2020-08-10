package com.willy.metu.followlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.data.Article
import com.willy.metu.databinding.ItemSavedArticleBinding

class ArticleListAdapter : ListAdapter<Article, RecyclerView.ViewHolder>(DiffCallback){
    class ArticleViewHolder(private var binding: ItemSavedArticleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article){
            binding.article = article

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_ARTICLE = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ARTICLE -> ArticleViewHolder(ItemSavedArticleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is ArticleViewHolder -> {
                holder.bind((getItem(position) as Article))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_ARTICLE
    }
}