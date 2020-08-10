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
import com.willy.metu.databinding.FragmentQuestionnaireTwoBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger

class QuestionnaireTwoFragment : Fragment() {

    private val viewModel by viewModels<QuestionnaireTwoViewModel> {
        getVmFactory(
                QuestionnaireTwoFragmentArgs.fromBundle(requireArguments()).selectedAnswers
        )
    }
    lateinit var binding: FragmentQuestionnaireTwoBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireTwoBinding.inflate(inflater, container, false)
        val cityIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_city)
        val districtIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_district)
        val defaultContent = MeTuApplication.instance.resources.getStringArray(R.array.default_array)
        val cityContent = MeTuApplication.instance.resources.getStringArray(R.array.city_array)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //Setup Spinner
        binding.spinnerCity.adapter = QuestionSpinnerAdapter(cityContent, cityIndicator)
        binding.spinnerDistrict.adapter = QuestionSpinnerAdapter(defaultContent, districtIndicator)

        //When city is selected, change the related content of district
        binding.spinnerCity.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                    ) {
                        setupDistrictSpinner(pos)

                        if (parent != null && pos != 0) {
                            viewModel.setupCity(parent.selectedItem.toString())
                        }

                    }
                }

        binding.spinnerDistrict.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            viewModel.setupDistrict(parent.selectedItem.toString())
                        }
                    }
                }

        // Setup leave sequence with a dialog
        binding.actionLeave.setOnClickListener {
            setDialog()
        }

        // Setup next button to navigate to the next question, passing the selected answers along
        binding.buttonNext.setOnClickListener {
            if (isFinished()) {
                navigateToNext()
            }
        }


        // Observers
        viewModel.selectedCity.observe(viewLifecycleOwner, Observer {
            Logger.d(it.toString())
        })
        viewModel.selectedDistrict.observe(viewLifecycleOwner, Observer {
            Logger.d(it.toString())
        })
        viewModel.navigateToQuestionThree.observe(viewLifecycleOwner, Observer {
            Logger.d(it.toString())
        })



        return binding.root
    }


    private fun setDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setTitle(getString(R.string.dialog_leave_title))
        alertDialogBuilder.setMessage(getString(R.string.dialog_leave_content))
        alertDialogBuilder.setPositiveButton(getString(R.string.dialog_btn_pos)) { _, _ -> findNavController().navigate(NavigationDirections.navigateToHomeFragment()) }
        alertDialogBuilder.setNegativeButton(getString(R.string.dialog_btn_neg)) { which, _ -> which.cancel() }
        alertDialogBuilder.show()

    }

    private fun setSpinnerContent(array: Int): SpinnerAdapter {
        return QuestionSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(array), MeTuApplication.instance.resources.getString(R.string.spinner_select_district))
    }


    fun setupDistrictSpinner(pos: Int) {
        binding.spinnerDistrict.adapter = setSpinnerContent(
                when (pos) {
                    1 -> R.array.taipei_array
                    2 -> R.array.new_taipei_array
                    3 -> R.array.taoyuan_array
                    4 -> R.array.taichung_array
                    5 -> R.array.kaohsiung_array
                    else -> R.array.default_array

                }
        )
    }

    private fun isFinished(): Boolean {

        return when {
            viewModel.selectedCity.value == null || viewModel.selectedCity.value == "" -> {
                Toast.makeText(MeTuApplication.appContext, getString(R.string.reminder_select_city), Toast.LENGTH_SHORT).show()
                false
            }
            viewModel.selectedDistrict.value == null || viewModel.selectedDistrict.value == "" -> {
                Toast.makeText(MeTuApplication.appContext, getString(R.string.reminder_select_district), Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }

    }

    private fun navigateToNext() {

        viewModel.navigateToQuestionThree.value?.let {
            findNavController().navigate(NavigationDirections.navigateToQuestionThree(it.apply {
                city = viewModel.selectedCity.value.toString()
                district = viewModel.selectedDistrict.value.toString()
            }
            ))
        }

    }
}