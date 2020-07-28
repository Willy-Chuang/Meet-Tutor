package com.willy.metu.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.willy.metu.MainViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.databinding.DialogPostEventBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager
import com.willy.metu.util.Logger
import com.willy.metu.util.TimeUtil
import java.lang.String.format
import java.util.*

class PostEventDialogFragment : AppCompatDialogFragment() {

    private val viewModel by viewModels<PostEventDialogViewModel> {
        getVmFactory(
                PostEventDialogFragmentArgs.fromBundle(requireArguments()).selectedDate
        )
    }

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
                binding.textSelectStartTime.visibility = View.GONE
                binding.textSelectEndTime.visibility = View.GONE
                viewModel.isAllDay.value = true
            } else {
                binding.textSelectStartTime.visibility = View.VISIBLE
                binding.textSelectEndTime.visibility = View.VISIBLE
                viewModel.isAllDay.value = false
            }
        }

        //Set up initial date from safe arg

        binding.textDate.text = viewModel.date
        viewModel.eventTime.value = TimeUtil.dateToStamp(viewModel.date, Locale.TAIWAN)

        //Setup Time Picker

        binding.textSelectStartTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(activity, { _, hour, minute ->
                binding.textSelectStartTime.text = "$hour : $minute"
                Logger.i("$hour : $minute")
                val timeTimeStamp = TimeUtil.timeToStamp("$hour:$minute", Locale.TAIWAN)
                viewModel.startTime.value = timeTimeStamp
            }, hour, minute, true).show()

        }

        viewModel.startTime.observe(viewLifecycleOwner, Observer {
            Logger.i( "$it")
        })

        binding.textSelectEndTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(activity, { _, hour, minute ->
                binding.textSelectEndTime.text = "$hour : $minute"
                Logger.i("$hour : $minute")
                val timeTimeStamp = TimeUtil.timeToStamp("$hour:$minute", Locale.TAIWAN)
                viewModel.endTime.value = timeTimeStamp
            }, hour, minute, true).show()

        }


        //Setup Date Picker

        fun datePicker() {
            val calender = Calendar.getInstance()
            val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calender.set(year, month, day)
                format("yyyy-MM-dd")
                var newMonth = format("%02d", month + 1)
                var newDay = format("%02d", day)
                binding.textDate.text = "$year-$newMonth-$newDay"
                val dateTimestamp = TimeUtil.dateToStamp("$year-$newMonth-$newDay", Locale.TAIWAN)
                viewModel.eventTime.value = dateTimestamp
            }
            val selectedDate = viewModel.date.split("-")
            val year = selectedDate[0]
            val month = selectedDate[1]
            val date = selectedDate[2]
            activity?.let {
                DatePickerDialog(
                        it,
                        dateListener,
                        year.toInt(),
                        month.toInt() - 1,
                        date.toInt()
                ).show()
            }
        }

        binding.textDate.setOnClickListener {
            datePicker()
        }

        //Setup spinner
//        binding.spinnerAttendee.adapter =
//            SelectedUserSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(R.array.followed_users_array))

        binding.spinnerAttendee.onItemSelectedListener =
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
//                        viewModel.invitation.value = parent.selectedItem.toString()
                            viewModel.invitation.value = viewModel.userInfo.value?.followingEmail?.get(pos -1).toString()
                            Toast.makeText(
                                    MeTuApplication.appContext,
                                    viewModel.userInfo.value?.followingEmail?.get(pos - 1).toString(),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

        binding.spinnerType.adapter =
                SelectedTypeSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(R.array.tag_array))
        binding.spinnerType.onItemSelectedListener =
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
                            viewModel.type.value = parent.selectedItem.toString()
                            Toast.makeText(
                                    MeTuApplication.appContext,
                                    parent.selectedItem.toString(),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

//        val adapter = CalendarBottomSheetAdapter()


        // Setup post button with error handling

        binding.buttonSave.setOnClickListener {
            if (viewModel.checkIfComplete()) {
                val event = viewModel.getEvent(UserManager.user.email)
                Logger.d("$event")
                viewModel.post(event)
//                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(MeTuApplication.appContext,
                        "Please complete the form", Toast.LENGTH_SHORT).show()
            }
        }


        //Observers

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            binding.spinnerAttendee.adapter =
                    SelectedUserSpinnerAdapter(it.followingName)

        })

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

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            Logger.i("userInfo = "+ it.toString())
        })



        viewModel.title.observe(viewLifecycleOwner, Observer {
            Logger.i(it)
        })

        viewModel.type.observe(viewLifecycleOwner, Observer {
            Logger.i(it)
        })

        viewModel.location.observe(viewLifecycleOwner, Observer {
            Logger.i(it)
        })


        return binding.root
    }


}
