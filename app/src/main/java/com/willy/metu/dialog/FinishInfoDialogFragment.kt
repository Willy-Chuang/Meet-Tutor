package com.willy.metu.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.text.bold
import androidx.navigation.fragment.findNavController
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.DialogFinishInfoBinding

class FinishInfoDialogFragment : AppCompatDialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val binding = DialogFinishInfoBinding.inflate(inflater, container, false)

        val string = SpannableStringBuilder()
                .bold { append("Finish your profile") }
                .append(" to have a full experience of the platform!")

        binding.layoutDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_fade_in))
        binding.textContent.text = string

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.buttonLater.setOnClickListener {
            dismiss()
        }
        binding.buttonGo.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToEditProfileFragment())
        }


        return binding.root

    }
}