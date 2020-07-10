package com.willy.metu.pair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.willy.metu.databinding.FragmentQuestionnaireTwoBinding
import com.willy.metu.ext.getVmFactory

class QuestionnaireTwoFragment : Fragment(){

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQuestionnaireTwoBinding.inflate(inflater, container, false)



        return binding.root
    }
}