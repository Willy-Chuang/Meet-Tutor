package com.willy.metu.postevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.text.bold
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.willy.metu.MainViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Event
import com.willy.metu.databinding.DialogPostEventBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.login.UserManager
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.util.Logger
import com.willy.metu.util.TimeUtil
import kotlinx.android.synthetic.main.dialog_post_event.view.*
import java.lang.String.format
import java.util.*

class PostEventDialogFragment : AppCompatDialogFragment() {

    val TIME_PICKER_TYPE_START = 0x01
    val TIME_PICKER_TYPE_END = 0x02

    private val viewModel by viewModels<PostEventDialogViewModel> {
        getVmFactory(
                PostEventDialogFragmentArgs.fromBundle(requireArguments()).selectedDate
        )
    }

    lateinit var binding: DialogPostEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AddEventDialog)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DialogPostEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dialog = this
        binding.viewModel = viewModel

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        //Setup switch - show time if all-day disabled (default: true)
        binding.switchAllDay.setOnClickListener {
            if (it.switch_all_day.isChecked) {
                timeSectionVisibility(false)
                viewModel.setAllDay(true)
            } else {
                timeSectionVisibility(true)
                viewModel.setAllDay(false)
            }
        }

        // Set up initial date from safe arg
        binding.textDate.text = viewModel.date

        // Setup Time Picker - Start Time
        binding.textSelectStartTime.setOnClickListener {
            showTimePickerDialog(TIME_PICKER_TYPE_START,hour,minute)
        }

        // Setup Time Picker - End Time
        binding.textSelectEndTime.setOnClickListener {
            showTimePickerDialog(TIME_PICKER_TYPE_END,hour,minute)
        }

        // Setup Date Picker
        binding.textDate.setOnClickListener {
            datePicker()
        }


        binding.spinnerAttendee.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            viewModel.setInvitation(pos, parent.selectedItem.toString())
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
                            parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            val selectedType = parent.selectedItem.toString()
                            viewModel.setType(selectedType)

                        }
                    }
                }


        // Setup post button with error handling
        binding.buttonSave.setOnClickListener {
            if (viewModel.checkIfComplete()) {
                val event = viewModel.getEvent(UserManager.user.email)

                if (viewModel.startTime.value == -1L) {
                    MeTuApplication.instance.setWork(event.eventTime, getFullTimeEventContent(event).toString())
                } else {
                    MeTuApplication.instance.setWork(event.eventTime, getStartTimeEventContent(event).toString())
                }

                viewModel.post(event)
                Logger.d("$event")
            } else {
                Toast.makeText(MeTuApplication.appContext,
                        getString(R.string.reminder_post_event), Toast.LENGTH_SHORT).show()
            }
        }


        // Observers
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
            Logger.i("userInfo = $it")
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

        viewModel.startTime.observe(viewLifecycleOwner, Observer {
            Logger.i("$it")
        })

        viewModel.isAllDay.observe(viewLifecycleOwner, Observer {
            Logger.i("$it")
        })

        // Progress Bar
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE

                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    viewModel.leave()
                }
            }
        })


        return binding.root
    }

    private fun timeSectionVisibility(required: Boolean) {

        if (required) {
            binding.textSelectStartTime.visibility = View.VISIBLE
            binding.textSelectEndTime.visibility = View.VISIBLE
        } else {
            binding.textSelectStartTime.visibility = View.GONE
            binding.textSelectEndTime.visibility = View.GONE
        }
    }

    private fun getFullTimeEventContent(event: Event): SpannableStringBuilder {
        return SpannableStringBuilder()
                .append(event.title)
                .append(" with")
                .bold { append(viewModel.invitationName.value) }
                .append(" is starting at ")
                .bold { append(TimeUtil.stampToDate(event.eventTime)) }
    }

    private fun getStartTimeEventContent(event: Event): SpannableStringBuilder {
        return SpannableStringBuilder()
                .append(event.title)
                .append(" with")
                .bold { append(viewModel.invitationName.value) }
                .append(" is starting at tomorrow ")
                .bold { append(TimeUtil.stampToTime(event.startTime)) }
    }

    private fun datePicker() {
        val calender = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calender.set(year, month, day)
            format("yyyy-MM-dd")
            val newMonth = format("%02d", month + 1)
            val newDay = format("%02d", day)
            binding.textDate.text = "$year-$newMonth-$newDay"
            val dateTimestamp = TimeUtil.dateToStamp("$year-$newMonth-$newDay", Locale.TAIWAN)
            viewModel.setEventTime(dateTimestamp)
        }
        val selectedDate = viewModel.date.split("-")
        val year = selectedDate[0]
        val month = selectedDate[1]
        val date = selectedDate[2]
        activity?.let {

            val datePickerDialog = DatePickerDialog(
                    it, dateListener, year.toInt(), month.toInt() - 1, date.toInt()
            )
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }


    private fun showTimePickerDialog(type: Int, startHour: Int, startMinute: Int) {
        TimePickerDialog(activity, { _, hour, minute ->

            when (type) {
                TIME_PICKER_TYPE_START -> {

                    binding.textSelectStartTime.text = "$hour : $minute"
                    Logger.i("$hour : $minute")
                    viewModel.setStartTime(TimeUtil.timeToStamp("$hour:$minute", Locale.TAIWAN))
                }
                TIME_PICKER_TYPE_END -> {
                    val timeTimeStamp = TimeUtil.timeToStamp("$hour:$minute", Locale.TAIWAN)
                    viewModel.startTime.value?.let {
                        if (timeTimeStamp > it) {
                            binding.textSelectEndTime.text = "$hour : $minute"
                            Logger.i("$hour : $minute")
                            viewModel.setEndTime(timeTimeStamp)
                        } else {
                            Toast.makeText(MeTuApplication.appContext,
                                    getString(R.string.reminder_invalid_time), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }, startHour, startMinute, true).show()
    }
}
