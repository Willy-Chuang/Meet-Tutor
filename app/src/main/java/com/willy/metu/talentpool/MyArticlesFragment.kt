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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.willy.metu.R
import com.willy.metu.databinding.FragmentTalentpoolBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortArticleToMyArticle
import com.willy.metu.ext.sortByType
import com.willy.metu.login.UserManager
import com.willy.metu.util.Logger

class MyArticlesFragment : Fragment() {

    private val viewModel by viewModels<TalentPoolViewModel> { getVmFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentTalentpoolBinding.inflate(inflater, container, false)
        val adapter = MyArticleAdapter(viewModel)
        binding.recyclerArticle.adapter = adapter
        binding.recyclerArticle.layoutManager = LinearLayoutManager(context)
        binding.viewModel = viewModel


        val allType = binding.chipAll
        val studyGroup = binding.chipStudy
        val student = binding.chipStudent
        val tutor = binding.chipTutor

        allType.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.selectedType.value = allType.text.toString()
            }
        }

        studyGroup.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.selectedType.value = studyGroup.text.toString()
            }
        }

        student.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.selectedType.value = student.text.toString()
            }
        }

        tutor.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.selectedType.value = tutor.text.toString()
            }
        }




        viewModel.allLiveArticles.observe(viewLifecycleOwner, Observer {

            val myArticle = it.sortArticleToMyArticle(UserManager.user.email)
            binding.recyclerArticle.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_animation)

            viewModel.selectedType.observe(viewLifecycleOwner, Observer { type ->

                if (type == "All Type") {
                    binding.noValue.visibility = View.GONE
                    binding.noValueImage.visibility = View.GONE
                    adapter.submitList(myArticle)
                } else {

                    if (myArticle.sortByType(type).isEmpty()) {
                        binding.noValue.visibility = View.VISIBLE
                        binding.noValueImage.visibility = View.VISIBLE
                        adapter.submitList(myArticle.sortByType(type))
                    } else {
                        binding.noValue.visibility = View.GONE
                        binding.noValueImage.visibility = View.GONE
                        adapter.submitList(myArticle.sortByType(type))
                    }
                }

            })

            adapter.submitList(myArticle)
            adapter.notifyDataSetChanged()
        })

        viewModel.checked.observe(viewLifecycleOwner, Observer {
            Logger.d(it.toString())
            if (it == true) {
                snack(binding.layoutBottomnav, "Add to follow list")
            } else {
                snack(binding.layoutBottomnav, "Remove from follow list")
            }
        })

        viewModel.isAdded.observe(viewLifecycleOwner, Observer {
            Logger.d(it.toString())
        })

        return binding.root

    }

    fun snack(baseView: View, content: String) {
        Snackbar.make(baseView, content, Snackbar.LENGTH_SHORT).apply {
            view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                setMargins(24, topMargin, 24, 24)
            }
            view.background = context.getDrawable(R.drawable.bg_all_round_r8_black)
        }.show()
    }

}