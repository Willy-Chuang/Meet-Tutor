package com.willy.metu.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.willy.metu.MainViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentEditProfileBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.pair.QuestionSpinnerAdapter
import com.willy.metu.util.Logger
import java.util.*


class EditProfileFragment : Fragment() {

    private val viewModel by viewModels<EditProfileViewModel> { getVmFactory() }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        // Initialized viewModel for layout
        binding.viewModel = viewModel

        // Call mainViewModel to observe if the save button is pressed
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Set variables for content
        val cityIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_city)
        val districtIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_district)
        val defaultContent = MeTuApplication.instance.resources.getStringArray(R.array.default_array)
        val cityContent = MeTuApplication.instance.resources.getStringArray(R.array.city_array)
        val taipeiContent = MeTuApplication.instance.resources.getStringArray(R.array.taipei_array)
        val newTaipeiContent = MeTuApplication.instance.resources.getStringArray(R.array.new_taipei_array)
        val taoyuanContent = MeTuApplication.instance.resources.getStringArray(R.array.taoyuan_array)
        val taichungContent = MeTuApplication.instance.resources.getStringArray(R.array.taichung_array)
        val kaohsiungContent = MeTuApplication.instance.resources.getStringArray(R.array.kaohsiung_array)


        // Setup chip group for tag selection
        val chipGroup = binding.chipGroup
        chipGroup.isSingleSelection = false
        val types = MeTuApplication.instance.resources.getStringArray(R.array.all_tag_array)

        for (type in types) {
            val chip = LayoutInflater.from(requireContext()).inflate(R.layout.chip_layout, chipGroup, false) as Chip
            chip.text = type

            chip.setOnCheckedChangeListener { c, isChecked ->
                if (isChecked) {

                    // Restrict max chip selection
                    if (viewModel.itemList.size > 2) {
                        c.isChecked = false
                        Toast.makeText(requireContext(), getString(R.string.reminder_subject_max), Toast.LENGTH_SHORT).show()
                        return@setOnCheckedChangeListener
                    }

                    // Check if the list already contains the tag, if not then add to list
                    if (viewModel.itemList.contains(c.text.toString())) {
                        Logger.d("Has Been Added")
                    } else {
                        viewModel.itemList.add(c.text.toString())
                        viewModel.selectedTags.value = viewModel.itemList
                    }

                    // Remove tag from list when uncheck
                } else {
                    viewModel.itemList.remove(c.text.toString())
                    viewModel.selectedTags.value = viewModel.itemList
                }
            }
            chipGroup.addView(chip)
        }

        // Setup Radio button (Gender, Identity)
        binding.radioGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_male -> viewModel.selectedGender.value = "Male"
                R.id.radio_female -> viewModel.selectedGender.value = "Female"
            }
        }

        binding.radioIdentity.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_tutor -> viewModel.selectedIdentity.value = "Tutor"
                R.id.radio_student -> viewModel.selectedIdentity.value = "Student"
            }
        }

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
                            1 -> binding.spinnerDistrict.adapter = QuestionSpinnerAdapter(taipeiContent, districtIndicator)
                            2 -> binding.spinnerDistrict.adapter = QuestionSpinnerAdapter(newTaipeiContent, districtIndicator)
                            3 -> binding.spinnerDistrict.adapter = QuestionSpinnerAdapter(taoyuanContent, districtIndicator)
                            4 -> binding.spinnerDistrict.adapter = QuestionSpinnerAdapter(taichungContent, districtIndicator)
                            5 -> binding.spinnerDistrict.adapter = QuestionSpinnerAdapter(kaohsiungContent, districtIndicator)
                        }
                        if (parent != null && pos != 0) {
                            viewModel.selectedCity.value = parent.selectedItem.toString()
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
                        }
                    }
                }


        // Preload info if user has already filled in advance
        viewModel.personalInfo.observe(viewLifecycleOwner, Observer {

            // Preload Gender
            when (it.gender.toLowerCase(Locale.ROOT)) {
                "male" -> binding.radioGender.check(R.id.radio_male)
                "female" -> binding.radioGender.check(R.id.radio_female)
                else -> binding.radioIdentity.clearCheck()
            }

            // Preload Identity
            when (it.identity.toLowerCase(Locale.ROOT)) {
                "student" -> binding.radioIdentity.check(R.id.radio_student)
                "tutor" -> binding.radioIdentity.check(R.id.radio_tutor)
                else -> binding.radioIdentity.clearCheck()
            }

            // Preload Introduction
            binding.editIntroduction.setText(it.introduction)

            // Preload Experience
            binding.editExperience.setText(it.experience)


        })


        //Observers for editable components
        viewModel.selectedTags.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
        })

        viewModel.selectedGender.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
        })
        viewModel.selectedIdentity.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
        })
        viewModel.selectedCity.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
        })
        viewModel.selectedDistrict.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
        })
        viewModel.introduction.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
        })
        viewModel.experience.observe(viewLifecycleOwner, Observer {
            Logger.i(it.toString())
        })

        // Observer for save button, when pressed send update user info (With empty handel)
        mainViewModel.saveIsPressed.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (viewModel.checkIfComplete()) {
                    viewModel.updateUser(viewModel.getUser())
                    findNavController().navigate(NavigationDirections.navigateToProfile())
                    mainViewModel.saveIsPressed.value = false
                } else {
                    Toast.makeText(MeTuApplication.appContext, getString(R.string.reminder_finish_user_info), Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }
}