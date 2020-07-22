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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.willy.metu.MainActivity
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.data.Answers
import com.willy.metu.databinding.FragmentQuestionnaireOneBinding
import com.willy.metu.databinding.FragmentStartPairingBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger

class QuestionnaireOneFragment : Fragment() {

    private val viewModel by viewModels<QuestionnaireOneViewModel> { getVmFactory() }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQuestionnaireOneBinding.inflate(inflater, container, false)
        val majorIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_category)
        val minorIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_subject)
        val defaultContent = MeTuApplication.instance.resources.getStringArray(R.array.default_array)
        val majorContent = MeTuApplication.instance.resources.getStringArray(R.array.major_subject_array)

        //Setup Spinner
        binding.spinnerSubjectMajor.adapter = QuestionSpinnerAdapter(majorContent, majorIndicator)
        binding.spinnerSubjectMinor.adapter = QuestionSpinnerAdapter(defaultContent, minorIndicator)

        //When main category is selected, change the related content of subject
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
                            viewModel.navigateToQuestionTwo.value?.category = parent.selectedItem.toString()
                        }

                    }
                }

        //Passing Value to Subject livedata
        binding.spinnerSubjectMinor.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            viewModel.selectedMinorSubject.value = parent.selectedItem.toString()
                            viewModel.navigateToQuestionTwo.value?.subject = parent.selectedItem.toString()
                            Toast.makeText(
                                    MeTuApplication.appContext,
                                    parent.selectedItem.toString(),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


        // Setup leave sequence with a dialog
        binding.actionLeave.setOnClickListener {
            setDialog()
        }

        viewModel.selectedMajorSubject.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })
        viewModel.selectedMinorSubject.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        viewModel.navigateToQuestionTwo.observe(viewLifecycleOwner, Observer {
            Logger.d(it.category)

        })

        //Setup next button to navigate to the next question, passing the selected answers along
        binding.buttonNext.setOnClickListener { view ->
            if (viewModel.selectedMajorSubject.value == null) {
                Toast.makeText(MeTuApplication.appContext,"Please select a category",Toast.LENGTH_SHORT).show()
            } else if (viewModel.selectedMinorSubject.value== null) {
                Toast.makeText(MeTuApplication.appContext,"Please select a subject",Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(NavigationDirections.navigateToQuestionTwo(Answers(
                        category = viewModel.selectedMajorSubject.value.toString(),
                        subject = viewModel.selectedMinorSubject.value.toString()
                )))
            }
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
        alertDialogBuilder.setMessage("Leaving will lead to losing record of the questionnaire")
        alertDialogBuilder.setPositiveButton("Sure", DialogInterface.OnClickListener { which, i -> findNavController().navigate(NavigationDirections.navigateToHomeFragment()) })
        alertDialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { which, i -> which.cancel() })
        alertDialogBuilder.show()

    }


}