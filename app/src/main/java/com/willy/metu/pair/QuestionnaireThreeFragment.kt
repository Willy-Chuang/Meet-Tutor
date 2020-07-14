package com.willy.metu.pair

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


        //Setup gender selection

        //Set initial state to false
        viewModel.isPressed.value = ""

        binding.buttonMale.setOnClickListener {
            if (viewModel.isPressed.value == "male"){
                viewModel.isPressed.value = ""
            }else{ viewModel.isPressed.value = "male"}
        }
        binding.buttonFemale.setOnClickListener {
            if(viewModel.isPressed.value == "female") {
                viewModel.isPressed.value = ""
            }else{ viewModel.isPressed.value = "female"}
        }

        // Setup leave sequence with a dialog
        binding.actionLeave.setOnClickListener {
            setDialog()
        }

        //Setup Finish button
        binding.buttonFinish.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToPairingResultFragment())
        }


        //Observe boolean liveData to set bg color for indication
        viewModel.isPressed.observe(viewLifecycleOwner, Observer {
            if (it == "male") {
                binding.buttonMale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimaryDark))
                binding.buttonFemale.setBackgroundColor(MeTuApplication.instance.getColor(R.color.colorPrimary))
            } else if (it == "female"){
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