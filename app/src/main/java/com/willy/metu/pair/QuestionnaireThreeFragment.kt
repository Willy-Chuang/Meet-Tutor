package com.willy.metu.pair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.willy.metu.databinding.FragmentQuestionnaireThreeBinding
import com.willy.metu.ext.getVmFactory

class QuestionnaireThreeFragment : Fragment() {
    private val viewModel by viewModels<QuestionnaireThreeViewModel> {
        getVmFactory(
                QuestionnaireThreeFragmentArgs.fromBundle(requireArguments()).selectedAnswers
        )
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentQuestionnaireThreeBinding.inflate(inflater, container, false)

        return binding.root

    }

}