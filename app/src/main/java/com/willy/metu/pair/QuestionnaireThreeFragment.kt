package com.willy.metu.pair

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentQuestionnaireThreeBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger

class QuestionnaireThreeFragment : Fragment() {
    private val viewModel by viewModels<QuestionnaireThreeViewModel> {
        getVmFactory(
                QuestionnaireThreeFragmentArgs.fromBundle(requireArguments()).selectedAnswers
        )
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentQuestionnaireThreeBinding.inflate(inflater, container, false)

        // Initialize answers from the previous page
        viewModel.navigateToResult.value = viewModel.previousAnswers


        // Setup gender selection
        // Set initial state to false
        viewModel.isPressed.value = ""

        binding.buttonMale.setOnClickListener {
            if (viewModel.isPressed.value == "Male"){
                viewModel.isPressed.value = ""
            }else{ viewModel.isPressed.value = "Male"}
        }
        binding.buttonFemale.setOnClickListener {
            if(viewModel.isPressed.value == "Female") {
                viewModel.isPressed.value = ""
            }else{ viewModel.isPressed.value = "Female"}
        }

        // Setup leave sequence with a dialog
        binding.actionLeave.setOnClickListener {
            setDialog()
        }

        // Setup Finish button
        binding.buttonFinish.setOnClickListener {
            if (viewModel.isPressed.value == ""){
                Toast.makeText(MeTuApplication.appContext,"Please select a city", Toast.LENGTH_SHORT).show()
            } else{
                Logger.w("viewModel.navigateToResult.value=${viewModel.navigateToResult.value!!.apply {
                    gender = viewModel.isPressed.value.toString()
                }}")
                findNavController().navigate(NavigationDirections.navigateToPairingResultFragment(viewModel.navigateToResult.value!!.apply {
                    gender = viewModel.isPressed.value.toString()
                }))
            }
        }

        // Setup Skip button
        binding.buttonSkip.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToPairingResultFragment(viewModel.navigateToResult.value!!))
        }


        //Observe boolean liveData to set bg color for indication
        viewModel.isPressed.observe(viewLifecycleOwner, Observer {
            if (it == "Male") {
                binding.buttonMale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimaryDark))
                binding.buttonFemale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimary))
            } else if (it == "Female"){
                binding.buttonMale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimary))
                binding.buttonFemale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimaryDark))
            } else {
                binding.buttonMale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimary))
                binding.buttonFemale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimary))
            }
        })



        return binding.root

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