package com.willy.metu.profile

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.chip.ChipDrawable
import com.willy.metu.MainViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentEditProfileBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.pair.QuestionSpinnerAdapter
import com.willy.metu.util.Logger


class EditProfileFragment : Fragment() {

    private val viewModel by viewModels<EditProfileViewModel> { getVmFactory() }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        //Initialized viewModel for layout
        binding.viewModel = viewModel

        //Call mainViewModel to observe if the save button is pressed
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        //Set variables for content
        val cityIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_city)
        val districtIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_district)
        val defaultContent = MeTuApplication.instance.resources.getStringArray(R.array.default_array)
        val cityContent = MeTuApplication.instance.resources.getStringArray(R.array.city_array)
        val taipeiContent = MeTuApplication.instance.resources.getStringArray(R.array.taipei_array)
        val newTaipeiContent = MeTuApplication.instance.resources.getStringArray(R.array.new_taipei_array)
        val taoyuanContent = MeTuApplication.instance.resources.getStringArray(R.array.taoyuan_array)
        val taichungContent = MeTuApplication.instance.resources.getStringArray(R.array.taichung_array)
        val kaohsiungContent = MeTuApplication.instance.resources.getStringArray(R.array.kaohsiung_array)


        //Setup chip group for tag selection
        val chipGroup = binding.chipGroup
        chipGroup.isSingleSelection = false
        val genres = MeTuApplication.instance.resources.getStringArray(R.array.all_tag_array)

        for (genre in genres) {
            val chip = LayoutInflater.from(requireContext()).inflate(R.layout.chip_layout, chipGroup,false) as Chip
            chip.text = genre
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){

                    //Check if the list already contains the tag, if not then add to list
                    if(viewModel.itemList.contains(chip.text.toString())){
                        Logger.d("Has Been Added")
                    }else {
                    viewModel.itemList.add(chip.text.toString())
                    viewModel.selectedTags.value = viewModel.itemList
                    }

                    // Remove tag from list when uncheck
                } else {
                    viewModel.itemList.remove(chip.text.toString())
                    viewModel.selectedTags.value = viewModel.itemList
                }
            }
            chipGroup.addView(chip)
        }




        // Setup Radio button (Gender, Identity)
        binding.radioGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_male -> viewModel.selectedGender.value = "Male"
                R.id.radio_female -> viewModel.selectedGender.value = "Female"
            }
        }

        binding.radioIdentity.setOnCheckedChangeListener { group, checkedId ->
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
                            Toast.makeText(
                                    MeTuApplication.appContext,
                                    parent.selectedItem.toString(),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }




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

        //Observer for save button, when pressed send update user info (With empty handel)
        mainViewModel.saveIsPressed.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                if(viewModel.checkIfComplete()){

                    viewModel.updateUser(viewModel.getUserInfo())
                    findNavController().navigate(NavigationDirections.navigateToProfile())
                    mainViewModel.saveIsPressed.value = false

                }else{
                    Toast.makeText(MeTuApplication.appContext,"Finishing the info would help when pairing",Toast.LENGTH_SHORT).show()

                }
            }
        })



        return binding.root

    }
}