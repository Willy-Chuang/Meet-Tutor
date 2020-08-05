package com.willy.metu.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.willy.metu.MeTuApplication
import com.willy.metu.NavigationDirections
import com.willy.metu.R
import com.willy.metu.databinding.FragmentCalendarBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.*
import org.threeten.bp.LocalDate
import java.util.*


class CalendarFragment : Fragment() {

    private val viewModel by viewModels<CalendarViewModel> { getVmFactory() }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var widget: MaterialCalendarView
    private val oneDayDecorator: OneDayDecorator = OneDayDecorator()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val localDate = LocalDate.now()

        widget = binding.calendarView
        widget.setCurrentDate(localDate)

        binding.viewModel = viewModel

        binding.recyclerSchedule.layoutManager = LinearLayoutManager(context)
        val adapter = CalendarBottomSheetAdapter(viewModel)
        binding.recyclerSchedule.adapter = adapter


        // Layout bottom sheet for different size of device
        binding.persistentBottomSheet.viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    @RequiresApi(Build.VERSION_CODES.Q)
                    override fun onGlobalLayout() {
                        val totalHeight = binding.viewContainer.height
                        val shadowWeekHeight = binding.calendarViewWeek.height
                        val monthHeight = binding.calendarView.height
                        val topMargin = Utils.convertDpToPixelSize(resources.getFloat(R.dimen.bottom_sheet_top_margin), requireContext())
                        binding.persistentBottomSheet.layoutParams.height = totalHeight - shadowWeekHeight - topMargin
                        binding.persistentBottomSheet.requestLayout()

                        bottomSheetBehavior = BottomSheetBehavior.from<NestedScrollView>(binding.persistentBottomSheet)

                        bottomSheetBehavior.peekHeight = totalHeight - monthHeight - topMargin

                        binding.persistentBottomSheet.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

        // Identify BottomSheetBehavior to present different calendar layout
        bottomSheetBehavior = BottomSheetBehavior.from<NestedScrollView>(binding.persistentBottomSheet)

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

        // Set Indicator of current date
        widget.setSelectedDate(localDate)


        // Add dots based on my events
        viewModel.allLiveEvents.observe(viewLifecycleOwner, Observer {
            Logger.d("viewModel.allEvents.observe, it=$it")
            it?.let {

                it.forEach { event ->
                    val year = TimeUtil.stampToYear(event.eventTime).toInt()
                    val month = TimeUtil.stampToMonthInt(event.eventTime).toInt()
                    val day = TimeUtil.stampToDay(event.eventTime).toInt()

                    addDotDecoration(year, month, day)
                }
            }
        })

        // Get the current selected date
        widget.setOnDateChangedListener { _, date, selected ->
            if (selected) {

                oneDayDecorator.setDate(date.date)

                val selectedDate = TimeUtil.dateToStamp(date.date.toString(), Locale.TAIWAN)

                // Create a sorted list of event based on the current date
                viewModel.createDailyEvent(selectedDate)

                Logger.d("$selectedDate")

            }
        }

        viewModel.selectedLiveEvent.observe(viewLifecycleOwner, Observer {
            Logger.d("Sorted Event List : $it")
            it?.let {
                adapter.notifyDataSetChanged()
                binding.viewModel = viewModel
            }
        })

        viewModel.navigationToPostDialog.observe(viewLifecycleOwner, Observer { date ->

            date?.let {
                binding.buttonAddEvent.setOnClickListener {
                    findNavController().navigate(NavigationDirections.navigateToPostEventDialog(date))
                }
            }

        })


        return binding.root

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

    private fun addDotDecoration(year: Int, month: Int, day: Int) {
        widget.addDecorators(
                SingleDateDecorator(
                        MeTuApplication.appContext.resources.getColor(R.color.red),
                        CalendarDay.from(year, month, day)
                )
        )
    }
}

