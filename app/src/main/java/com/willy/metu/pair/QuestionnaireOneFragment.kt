package com.willy.metu.pair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.data.Answers
import com.willy.metu.databinding.FragmentQuestionnaireOneBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger

class QuestionnaireOneFragment : Fragment() {

    private val viewModel by viewModels<QuestionnaireOneViewModel> { getVmFactory() }

    lateinit var binding: FragmentQuestionnaireOneBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireOneBinding.inflate(inflater, container, false)
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
                            parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                    ) {
                        setupMinorSpinner(pos)

                        if (parent != null && pos != 0) {
                            viewModel.setupCategory(parent.selectedItem.toString())
                        }

                    }
                }

        //Passing Value to Subject live data
        binding.spinnerSubjectMinor.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            viewModel.setupMinorSubject(parent.selectedItem.toString())
                        }
                    }
                }


        // Setup leave sequence with a dialog
        binding.actionLeave.setOnClickListener {
            setDialog()
        }

        //Setup next button to navigate to the next question, passing the selected answers along
        binding.buttonNext.setOnClickListener {
            if (isFinished()) {
                navigateToNext()
            }
        }

        // Observers
        viewModel.selectedMajorSubject.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })
        viewModel.selectedMinorSubject.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })





        return binding.root
    }

    private fun setSpinnerContent(array: Int): SpinnerAdapter {
        return MinorSubjectSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(array))
    }

    private fun setDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setTitle(getString(R.string.dialog_title_leave))
        alertDialogBuilder.setMessage(getString(R.string.dialog_leave_content))
        alertDialogBuilder.setPositiveButton(getString(R.string.dialog_btn_pos)) { _, _ -> findNavController().navigate(NavigationDirections.navigateToHomeFragment()) }
        alertDialogBuilder.setNegativeButton(getString(R.string.dialog_btn_neg)) { which, _ -> which.cancel() }
        alertDialogBuilder.show()

    }

    fun setupMinorSpinner(pos: Int) {
        binding.spinnerSubjectMinor.adapter = setSpinnerContent(

                when (pos) {
                    1 -> R.array.language_array
                    2 -> R.array.curriculum_array
                    3 -> R.array.music_array
                    4 -> R.array.art_array
                    5 -> R.array.sport_array
                    6 -> R.array.exam_array
                    else -> R.array.default_array
                }

        )
    }

    private fun isFinished(): Boolean {

        return when {
            viewModel.selectedMajorSubject.value == null || viewModel.selectedMajorSubject.value == "" -> {
                Toast.makeText(MeTuApplication.appContext, getString(R.string.reminder_select_category), Toast.LENGTH_SHORT).show()
                false
            }
            viewModel.selectedMinorSubject.value == null || viewModel.selectedMinorSubject.value == "" -> {
                Toast.makeText(MeTuApplication.appContext, getString(R.string.reminder_select_subject), Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }

    }

    private fun navigateToNext() {

        findNavController().navigate(NavigationDirections.navigateToQuestionTwo(Answers(
                category = viewModel.selectedMajorSubject.value.toString(),
                subject = viewModel.selectedMinorSubject.value.toString()
        )))

    }

}