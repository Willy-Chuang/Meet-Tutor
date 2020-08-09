package com.willy.metu.pair

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
import com.willy.metu.util.GenderType

class QuestionnaireThreeFragment : Fragment() {
    private val viewModel by viewModels<QuestionnaireThreeViewModel> {
        getVmFactory(
                QuestionnaireThreeFragmentArgs.fromBundle(requireArguments()).selectedAnswers
        )
    }
    lateinit var binding: FragmentQuestionnaireThreeBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionnaireThreeBinding.inflate(inflater, container, false)


        // Setup gender selection
        binding.buttonMale.setOnClickListener {
            viewModel.setupMaleButton()
        }
        binding.buttonFemale.setOnClickListener {
            viewModel.setupFemaleButton()
        }

        // Setup leave sequence with a dialog
        binding.actionLeave.setOnClickListener {
            setDialog()
        }

        // Setup Finish button
        binding.buttonFinish.setOnClickListener {

            if (isFinished()) {
                navigateToNext()
            }
        }

        // Setup Skip button
        binding.buttonSkip.setOnClickListener { _ ->
            viewModel.navigateToResult.value?.let {
                findNavController().navigate(NavigationDirections.navigateToPairingResultFragment(it))
            }
        }


        //Observe liveData to set bg drawable for indication
        viewModel.isPressed.observe(viewLifecycleOwner, Observer {
            setupDrawable(it)
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

    private fun isFinished(): Boolean {
        return when (viewModel.isPressed.value) {
            "" -> {
                Toast.makeText(MeTuApplication.appContext, getString(R.string.reminder_select_gender), Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun navigateToNext() {
        findNavController().navigate(NavigationDirections.navigateToPairingResultFragment(viewModel.navigateToResult.value!!.apply {
            gender = viewModel.isPressed.value.toString()
        }))
    }

    private fun setupDrawable(gender: String) {

        when (gender) {
            GenderType.TYPE_MALE.value -> {
                binding.buttonMale.background = MeTuApplication.instance.getDrawable(R.drawable.image_boy_picked)
                binding.buttonFemale.background = MeTuApplication.instance.getDrawable(R.drawable.image_girl)
            }
            GenderType.TYPE_FEMALE.value -> {
                binding.buttonMale.background = MeTuApplication.instance.getDrawable(R.drawable.image_boy)
                binding.buttonFemale.background = MeTuApplication.instance.getDrawable(R.drawable.image_girl_picked)
            }
            else -> {
                binding.buttonMale.background = MeTuApplication.instance.getDrawable(R.drawable.image_boy)
                binding.buttonFemale.background = MeTuApplication.instance.getDrawable(R.drawable.image_girl)
            }
        }


    }


}