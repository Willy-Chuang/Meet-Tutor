package com.willy.metu.pair

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.willy.metu.MainActivity
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentQuestionnaireOneBinding
import com.willy.metu.databinding.FragmentStartPairingBinding
import com.willy.metu.ext.getVmFactory

class QuestionnaireOneFragment : Fragment() {

    private val viewModel by viewModels<QuestionnaireOneViewModel> { getVmFactory() }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQuestionnaireOneBinding.inflate(inflater, container, false)
        binding.spinnerSubjectMajor.adapter = MajorSubjectSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(R.array.major_subject_array))
        binding.spinnerSubjectMinor.adapter = MinorSubjectSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(R.array.default_array))

        binding.spinnerSubjectMajor.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                    ) {
                        when (pos) {
                            1 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.language_array)
                            2 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.curriculum_array)
                            3 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.music_array)
                            4 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.art_array)
                            5 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.sport_array)
                            6 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.exam_array)
                        }
                        if (parent != null && pos != 0) {
                            viewModel.selectedMajorSubject.value = parent.selectedItem.toString()
                        }

                    }
                }

        binding.buttonNext.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToQuestionTwo())
        }
        binding.actionLeave.setOnClickListener {
            setDialog()

        }




        return binding.root
    }

    fun setSpinnerContent(array: Int): SpinnerAdapter {
        val spinner = MinorSubjectSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(array))
        return spinner
    }

    fun setDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setTitle("Sure To Leave?")
        alertDialogBuilder.setMessage("Leaving will lead to losing the record of the questionnaire")
        alertDialogBuilder.setPositiveButton("sure", DialogInterface.OnClickListener { which, i -> findNavController().navigate(NavigationDirections.navigateToHomeFragment()) })
        alertDialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { which, i -> which.cancel() })
        alertDialogBuilder.show()

    }


}