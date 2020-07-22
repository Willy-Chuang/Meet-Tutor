package com.willy.metu.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentCalendarBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.ext.sortByTimeStamp
import com.willy.metu.util.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.threeten.bp.LocalDate
import java.util.*
import androidx.lifecycle.Observer

class CalendarFragment : Fragment() {

    private val viewModel by viewModels<CalendarBottomSheetViewModel> { getVmFactory() }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var widget: MaterialCalendarView
    private val oneDayDecorator: OneDayDecorator = OneDayDecorator()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        widget = binding.calendarView as MaterialCalendarView
        val calendar = LocalDate.now()
        widget.setCurrentDate(LocalDate.now())
        binding.viewModel = viewModel
        binding.isLiveDataDesign = MeTuApplication.instance.isLiveDataDesign()
        binding.recyclerSchedule.layoutManager = LinearLayoutManager(context)

        val adapter = CalendarBottomSheetAdapter(viewModel)
        binding.recyclerSchedule.adapter = adapter

        // Add dots based on my events

        viewModel.allLiveEvents.observe(viewLifecycleOwner, Observer {
            Logger.d("viewModel.allEvents.observe, it=$it")
            it?.let {

                it.forEach() { event ->
                    val year = TimeUtil.stampToYear(event.eventTime).toInt()
                    val month = TimeUtil.stampToMonthInt(event.eventTime).toInt()
                    val day = TimeUtil.stampToDay(event.eventTime).toInt()

                    addDotDecoration(year, month, day)

                }

            }
        })

        // Get the current selected date
        widget.setOnDateChangedListener { widget, date, selected ->
            if (selected) {

                oneDayDecorator.setDate(date.date)

                val toTimeStamp = TimeUtil.dateToStamp(date.date.toString(), Locale.TAIWAN)

                //Create a sorted list of event based on the current date

                viewModel.selectedLiveEvent.value = viewModel.allLiveEvents.value.sortByTimeStamp(toTimeStamp)

                viewModel.navigationToPostDialog.value = toTimeStamp

                Logger.d("${viewModel.selectedLiveEvent.value}")
                Logger.d("$toTimeStamp")


            }
        }

        viewModel.selectedLiveEvent.observe(viewLifecycleOwner, Observer {
            Logger.d("Sorted Event List : $it")
            it?.let {
                adapter.notifyDataSetChanged()
                binding.viewModel = viewModel
            }

        })

        viewModel.navigationToPostDialog.observe(viewLifecycleOwner, Observer {

            binding.buttonAddEvent.setOnClickListener { view ->
                findNavController().navigate(NavigationDirections.navigateToPostEventDialog(it))
            }

        })

        // Set Indicator of current date
        widget.setSelectedDate(calendar)

        viewModel.navigationToPostDialog.value = TimeUtil.dateToStamp(calendar.toString(), Locale.TAIWAN)

        // Set value to selectedEvent for recyclerView to pop
        viewModel.allLiveEvents.observe(viewLifecycleOwner, Observer {
            Logger.d("viewModel.allEvents.observe, it=$it")
            it?.let {
                val timeStamp = TimeUtil.dateToStamp(widget.selectedDate!!.date.toString(), Locale.TAIWAN)

                viewModel.selectedLiveEvent.value = viewModel.allLiveEvents.value.sortByTimeStamp(timeStamp)


            }

        }
        )



        return binding.root

    }

    override fun onStart() {
        super.onStart()


        // Identify BottomSheetBehavior to present different calendar layout

        bottomSheetBehavior = BottomSheetBehavior.from<NestedScrollView>(persistent_bottom_sheet)

        bottomSheetBehavior.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        changeToMonth()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
//                        Handler().postDelayed({changeToMonth()},200)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        changeToWeek()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }
        })
    }

    // Change the layout of calendar

    fun changeToWeek() {
        binding.calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit()
    }

    fun changeToMonth() {
        binding.calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
    }

    fun addDotDecoration(year: Int, month: Int, day: Int) {
        widget.addDecorators(
                SingleDateDecorator(
                        MeTuApplication.appContext.resources.getColor(R.color.red),
                        CalendarDay.from(year, month, day)
                )
        )
    }
}

