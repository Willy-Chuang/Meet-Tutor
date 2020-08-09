package com.willy.metu.talentpool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.willy.metu.R
import com.willy.metu.databinding.FragmentArticleBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortByType
import com.willy.metu.util.Logger

class AllArticlesFragment : Fragment() {

    private val viewModel by viewModels<TalentPoolViewModel> { getVmFactory() }
    lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArticleBinding.inflate(inflater, container, false)
        val adapter = AllArticleAdapter(viewModel)
        binding.recyclerArticle.adapter = adapter
        binding.viewModel = viewModel


        val allType = binding.chipAll
        val studyGroup = binding.chipStudy
        val student = binding.chipStudent
        val tutor = binding.chipTutor

        allType.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setType(allType.text.toString())
            }
        }

        studyGroup.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setType(studyGroup.text.toString())
            }
        }

        student.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setType(student.text.toString())
            }
        }

        tutor.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setType(tutor.text.toString())

            }
        }


        viewModel.allLiveArticles.observe(viewLifecycleOwner, Observer {

            setupRecyclerAnimation(binding.recyclerArticle)

            viewModel.selectedType.observe(viewLifecycleOwner, Observer { type ->

                if (type == "All Type") {
                    articleValueVisibility(true)
                    adapter.submitList(it)
                } else {

                    if (it.sortByType(type).isEmpty()) {
                        articleValueVisibility(false)
                        adapter.submitList(it.sortByType(type))
                    } else {
                        articleValueVisibility(true)
                        adapter.submitList(it.sortByType(type))
                    }
                }

            })

            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        // Feedback action when is checked or unchecked
        viewModel.checked.observe(viewLifecycleOwner, Observer {
            Logger.d(it.toString())
            if (it == true) {
                snack(binding.layoutBottomnav, getString(R.string.snack_follow_add))
            } else {
                snack(binding.layoutBottomnav, getString(R.string.snack_follow_remove))
            }
        })


        return binding.root

    }

    private fun snack(baseView: View, content: String) {
        Snackbar.make(baseView, content, Snackbar.LENGTH_SHORT).apply {
            view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                setMargins(24, topMargin, 24, 24)
            }
            view.background = context.getDrawable(R.drawable.bg_all_round_r8_black)
        }.show()
    }

    private fun articleValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValue.visibility = View.GONE
            binding.noValueImage.visibility = View.GONE
        } else {
            binding.noValue.visibility = View.VISIBLE
            binding.noValueImage.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerAnimation(recyclerView: RecyclerView) {
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_fade_in_animation)
    }


}