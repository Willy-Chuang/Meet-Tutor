package com.willy.metu.talentpool

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.willy.metu.R
import com.willy.metu.data.Article
import com.willy.metu.databinding.ItemArticleBinding
import com.willy.metu.login.UserManager
import kotlinx.android.synthetic.main.item_article.view.*


class TalentPoolAdapter (val viewModel: TalentPoolViewModel) : ListAdapter<Article, RecyclerView.ViewHolder>(TalentPoolAdapter){

    class ArticleViewHolder(private var binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article, viewModel: TalentPoolViewModel){

            binding.article = article
            val bookmarkIcon = binding.imageBookmark

            binding.imageBookmark.setOnClickListener {
                viewModel.addArticlesToWishlist(article, UserManager.user.email)
                bookmarkIcon.isSelected = !bookmarkIcon.isSelected
            }


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

        private const val ITEM_VIEW_TYPE_EVENT = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EVENT -> ArticleViewHolder(ItemArticleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val article = getItem(position)

        viewModel.savedArticles.value?.forEach {

            if(article.id == it.id) {
                holder.itemView.image_bookmark.isSelected = true
                holder.itemView.image_bookmark.setOnClickListener {
                    viewModel.addArticlesToWishlist(article, UserManager.user.email)
                }
            } else {
                holder.itemView.image_bookmark.isSelected = false
                holder.itemView.image_bookmark.setOnClickListener {
                    viewModel.addArticlesToWishlist(article, UserManager.user.email)
                }
            }

        }


        when(holder) {
            is ArticleViewHolder -> {
                holder.bind((getItem(position) as Article),viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_EVENT
    }



}