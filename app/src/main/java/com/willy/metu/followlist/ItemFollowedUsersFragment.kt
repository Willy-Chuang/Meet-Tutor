package com.willy.metu.followlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.ItemFollowedUsersBinding
import com.willy.metu.ext.getVmFactory

class ItemFollowedUsersFragment : Fragment() {

    private val viewModel by viewModels<FollowUserViewModel> { getVmFactory() }

    lateinit var binding: ItemFollowedUsersBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = ItemFollowedUsersBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val studentAdapter = StudentListAdapter()
        val tutorAdapter = TutorListAdapter()
        binding.recyclerStudent.adapter = studentAdapter
        binding.recyclerTutor.adapter = tutorAdapter


        // Observers
        viewModel.allFollowedList.observe(viewLifecycleOwner, Observer {

            // Split follow list into 2 sections
            viewModel.createListOfStudents(it)
            viewModel.createListOfTutors(it)

        })

        viewModel.followedStudents.observe(viewLifecycleOwner, Observer {

            setupRecyclerAnimation(binding.recyclerStudent)

            if (it.isEmpty()) {
                studentNoValueVisibility(false)
            } else {
                studentNoValueVisibility(true)
                studentAdapter.submitList(it)
            }

        })

        viewModel.followedTutors.observe(viewLifecycleOwner, Observer {

            setupRecyclerAnimation(binding.recyclerTutor)

            if (it.isEmpty()) {
                tutorNoValueVisibility(false)
            } else {
                tutorNoValueVisibility(true)
                tutorAdapter.submitList(it)
            }

        })


        return binding.root
    }

    private fun setupRecyclerAnimation (recyclerView: RecyclerView) {
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_animation)
    }

    private fun studentNoValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValueStudent.visibility = View.GONE
            binding.noValueStudentButton.visibility = View.GONE
        } else {
            binding.noValueStudent.visibility = View.VISIBLE
            binding.noValueStudentButton.visibility = View.VISIBLE
            binding.noValueStudentButton.setOnClickListener {
                findNavController().navigate(NavigationDirections.navigateToPairingFragment())
            }
        }
    }

    private fun tutorNoValueVisibility(withValue: Boolean) {
        if (withValue) {
            binding.noValueTutor.visibility = View.GONE
            binding.noValueTutorButton.visibility = View.GONE
        } else {
            binding.noValueTutor.visibility = View.VISIBLE
            binding.noValueTutorButton.visibility = View.VISIBLE
            binding.noValueTutorButton.setOnClickListener {
                findNavController().navigate(NavigationDirections.navigateToPairingFragment())
            }
        }
    }
}