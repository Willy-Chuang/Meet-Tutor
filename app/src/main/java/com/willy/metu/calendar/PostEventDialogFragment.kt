package com.willy.metu.calendar

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.willy.metu.MainViewModel
import com.willy.metu.R
import com.willy.metu.databinding.DialogPostEventBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.Logger
import kotlinx.android.synthetic.main.dialog_post_event.*
import java.util.*

class PostEventDialogFragment : AppCompatDialogFragment() {

    private val viewModel by viewModels<PostEventDialogViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AddEventDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogPostEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dialog = this
        binding.viewModel = viewModel

        //Setup switch - show time if all-day disabled (default: true)
        val AlldaySwitch = binding.switchAllDay
        AlldaySwitch.setOnClickListener {
            if (AlldaySwitch.isChecked) {
                binding.textStartTime.visibility = View.GONE
                binding.textEndTime.visibility = View.GONE
            } else {
                binding.textStartTime.visibility = View.VISIBLE
                binding.textEndTime.visibility = View.VISIBLE
            }
        }

        //Setup Time Picker
        binding.textStartTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(activity, {
                    _, hour, minute->
                binding.textStartTime.text = "$hour : $minute"
            }, hour, minute, true).show()
        }

        binding.textEndTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(activity, {
                    _, hour, minute->
                binding.textEndTime.text = "$hour : $minute"
            }, hour, minute, true).show()
        }

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let { needRefresh ->
                if (needRefresh) {
                    ViewModelProvider(requireActivity()).get(MainViewModel::class.java).apply {
                        refresh()
                    }
                }
                findNavController().navigateUp()
                viewModel.onLeft()
            }
        })

        viewModel.title.observe(viewLifecycleOwner, Observer {
            Logger.i(it)
        })


        return binding.root
    }
}
