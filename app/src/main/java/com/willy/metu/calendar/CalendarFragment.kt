package com.willy.metu.calendar

import android.graphics.Color
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.databinding.FragmentCalendarBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.util.*
import kotlinx.android.synthetic.main.bottom_sheet_calendar.*
import org.threeten.bp.LocalDate
import java.util.*

class CalendarFragment : Fragment() {

    private val viewModel by viewModels<CalendarBottomSheetViewModel> {getVmFactory()}

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
        val calendarView = binding.calendarView
        val calendar = Calendar.getInstance()
        calendarView.setCurrentDate(LocalDate.now())
        binding.viewModel = viewModel
        binding.isLiveDataDesign = MeTuApplication.instance.isLiveDataDesign()
        binding.recyclerSchedule.layoutManager = LinearLayoutManager(context)

        val adapter = CalendarBottomSheetAdapter()
        binding.recyclerSchedule.adapter = adapter

        viewModel.liveEvents.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Logger.d("viewModel.liveEvent.observe, it=$it")
            it?.let {
                binding.viewModel = viewModel
            }
        })

        val test = TimeUtil.stampToWeekday(1593932797000)
        Logger.i(test)

        val testa = TimeUtil.stampToDayOfMonth(1593932797000)
        Logger.i(testa)

        val testb = TimeUtil.stampToMonth(1593932797000)
        Logger.i(testb)


        return binding.root

    }

    override fun onStart() {
        super.onStart()

        val activity = activity
        val calendar = LocalDate.now()


        widget = view?.findViewById(R.id.calendarView) as MaterialCalendarView

        widget.addDecorators(HighlightWeekendsDecorator())

        // Set Indicator of current date
        widget.setSelectedDate(calendar)

        // Add Dot to a date
        widget.addDecorators(SingleDateDecorator(MeTuApplication.appContext.resources.getColor(R.color.red), CalendarDay.from(2020,7,13)))

        // Get the current selected date
        widget.setOnDateChangedListener { widget, date, selected ->
            if (selected) {

                oneDayDecorator.setDate(date.date)

                Toast.makeText(
                    MeTuApplication.appContext,
                    "current date : ${date.date}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Add Dot Span to the date


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

    fun onDateSelected(
        @NonNull widget: MaterialCalendarView,
        @NonNull date: CalendarDay,
        selected: Boolean
    ) { //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.date)
        widget.invalidateDecorators()
    }


}

