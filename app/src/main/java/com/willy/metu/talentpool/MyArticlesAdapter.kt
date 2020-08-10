package com.willy.metu.talentpool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.data.Article
import com.willy.metu.databinding.ItemMyArticleBinding
import com.willy.metu.login.UserManager

class MyArticleAdapter(val viewModel: TalentPoolViewModel) : ListAdapter<Article, RecyclerView.ViewHolder>(AllArticleAdapter) {

    class ArticleViewHolder(private var binding: ItemMyArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, viewModel: TalentPoolViewModel) {

            binding.article = article


            binding.imageDelete.setOnClickListener {

                val alertDialogBuilder = AlertDialog.Builder(itemView.rootView.context)
                alertDialogBuilder.setCancelable(true)
                alertDialogBuilder.setTitle(MeTuApplication.appContext.getString(R.string.dialog_title_delete))
                alertDialogBuilder.setMessage(MeTuApplication.appContext.getString(R.string.dialog_delete_content))
                alertDialogBuilder.setPositiveButton(MeTuApplication.appContext.getString(R.string.dialog_btn_pos)) { _, _ ->
                    viewModel.delete(article)
                    viewModel.passDeletedTitle(article.title)
                }
                alertDialogBuilder.setNegativeButton(MeTuApplication.appContext.getString(R.string.dialog_btn_neg)) { which, _ -> which.dismiss() }
                alertDialogBuilder.show()

            }

            binding.layoutUserInfo.setOnClickListener {
                if (article.creatorEmail == UserManager.user.email) {
                    Navigation.createNavigateOnClickListener(NavigationDirections.navigateToProfile()).onClick(binding.layoutUserInfo)
                } else {
                    Navigation.createNavigateOnClickListener(NavigationDirections.navigateToUserDetail(article.creatorEmail)).onClick(binding.layoutUserInfo)
                }
            }

            binding.imageCreatorImage.setOnClickListener {
                if (article.creatorEmail == UserManager.user.email) {
                    Navigation.createNavigateOnClickListener(NavigationDirections.navigateToProfile()).onClick(binding.layoutUserInfo)
                } else {
                    Navigation.createNavigateOnClickListener(NavigationDirections.navigateToUserDetail(article.creatorEmail)).onClick(binding.layoutUserInfo)
                }
            }

            binding.layoutText.setOnClickListener {
                binding.textDetail.maxLines = 99
                binding.buttonCollapse.visibility = View.VISIBLE
            }

            binding.textDetail.setOnClickListener {
                binding.textDetail.maxLines = 99
                binding.buttonCollapse.visibility = View.VISIBLE
            }


            binding.buttonCollapse.setOnClickListener {
                binding.textDetail.maxLines = 2
                binding.buttonCollapse.visibility = View.GONE
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

        private const val ITEM_VIEW_TYPE_ARTICLE = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ARTICLE -> ArticleViewHolder(ItemMyArticleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ArticleViewHolder -> {
                holder.bind((getItem(position) as Article), viewModel)

            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_ARTICLE
    }


}