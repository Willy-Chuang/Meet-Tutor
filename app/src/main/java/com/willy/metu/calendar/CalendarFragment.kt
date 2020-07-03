package com.willy.metu.calendar

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.databinding.FragmentCalendarBinding
import kotlinx.android.synthetic.main.bottom_sheet_calendar.*

class CalendarFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var binding: FragmentCalendarBinding
    lateinit var widget: MaterialCalendarView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentCalendarBinding.inflate(inflater, container, false)



        return binding.root

    }

    override fun onStart() {
        super.onStart()

        widget = view?.findViewById(R.id.calendarView) as MaterialCalendarView
        widget.setOnDateChangedListener { widget, date, selected ->
            if(selected) {
                Toast.makeText(
                    MeTuApplication.appContext,
                    "current date : ${date.date}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        bottomSheetBehavior = BottomSheetBehavior.from<NestedScrollView>(persistent_bottom_sheet)

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when(state){
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


}

