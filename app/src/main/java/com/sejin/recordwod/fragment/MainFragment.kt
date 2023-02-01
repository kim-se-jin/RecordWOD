package com.sejin.recordwod.fragment


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.*
import com.sejin.recordwod.base.BaseFragment
import com.sejin.recordwod.calendar.EventDecorator
import com.sejin.recordwod.calendar.SelectDecorator
import com.sejin.recordwod.databinding.FragmentMainBinding
import com.sejin.recordwod.view.viewmodel.MainViewModel
import java.util.*

class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    lateinit var calendar: MaterialCalendarView



    // 뷰가 생성될 때 호출
    override fun initView() {
        calendar = binding.calendarView
        calendar.setSelectedDate(CalendarDay.today()) // 처음 선택되어 있는 날짜 지정
        calendar.addDecorator(SelectDecorator())

        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                calendar.addDecorator(EventDecorator(Collections.singleton(date)))
            }
        })
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)
}

