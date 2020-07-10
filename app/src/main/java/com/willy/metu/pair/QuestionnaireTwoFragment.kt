package com.willy.metu.pair

import android.content.DialogInterface
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
import com.willy.metu.databinding.FragmentQuestionnaireTwoBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger

class QuestionnaireTwoFragment : Fragment(){

    private val viewModel by viewModels<QuestionnaireTwoViewModel> {
        getVmFactory(
                QuestionnaireTwoFragmentArgs.fromBundle(requireArguments()).selectedAnswers
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQuestionnaireTwoBinding.inflate(inflater, container, false)
        val cityIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_city)
        val districtIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_district)
        val defaultContent = MeTuApplication.instance.resources.getStringArray(R.array.default_array)
        val cityContent = MeTuApplication.instance.resources.getStringArray(R.array.city_array)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.navigateToQuestionThree.value = viewModel.previousAnswers

        //Setup Spinner
        binding.spinnerCity.adapter = QuestionSpinnerAdapter(cityContent, cityIndicator)
        binding.spinnerDistrict.adapter = QuestionSpinnerAdapter(defaultContent, districtIndicator)

        //When city is selected, change the related content of district
        binding.spinnerCity.onItemSelectedListener =
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
                            1 -> binding.spinnerDistrict.adapter = setSpinnerContent(R.array.taipei_array)
                            2 -> binding.spinnerDistrict.adapter = setSpinnerContent(R.array.new_taipei_array)
                            3 -> binding.spinnerDistrict.adapter = setSpinnerContent(R.array.taoyuan_array)
                            4 -> binding.spinnerDistrict.adapter = setSpinnerContent(R.array.taichung_array)
                            5 -> binding.spinnerDistrict.adapter = setSpinnerContent(R.array.kaohsiung_array)
                        }
                        if (parent != null && pos != 0) {
                            viewModel.selectedCity.value = parent.selectedItem.toString()
                            viewModel.navigateToQuestionThree.value?.city = parent.selectedItem.toString()
                        }

                    }
                }

        binding.spinnerDistrict.onItemSelectedListener =
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
                            viewModel.selectedDistrict.value = parent.selectedItem.toString()
                            viewModel.navigateToQuestionThree.value?.district = parent.selectedItem.toString()
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

        binding.buttonNext.setOnClickListener { view ->
            if (viewModel.selectedCity.value == null) {
                Toast.makeText(MeTuApplication.appContext,"Please select a city",Toast.LENGTH_SHORT).show()
            } else if (viewModel.selectedDistrict.value== null) {
                Toast.makeText(MeTuApplication.appContext,"Please select a district",Toast.LENGTH_SHORT).show()
            } else {
            findNavController().navigate(NavigationDirections.navigateToQuestionThree(viewModel.navigateToQuestionThree.value!!.apply {
                    city = viewModel.selectedCity.value.toString()
                    district = viewModel.selectedDistrict.value.toString() }
            ))
            }

        }


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

    fun setSpinnerContent(array: Int): SpinnerAdapter {
        val spinner = MinorSubjectSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(array))
        return spinner
    }

    fun setDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setTitle("Sure To Leave?")
        alertDialogBuilder.setMessage("Leaving will lead to losing record of the questionnaire")
        alertDialogBuilder.setPositiveButton("sure", DialogInterface.OnClickListener { which, i -> findNavController().navigate(NavigationDirections.navigateToHomeFragment()) })
        alertDialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { which, i -> which.cancel() })
        alertDialogBuilder.show()

    }
}