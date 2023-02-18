package com.sejin.recordwod.fragment


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.*
import com.sejin.recordwod.R
import com.sejin.recordwod.base.BaseFragment
import com.sejin.recordwod.calendar.EventDecorator
import com.sejin.recordwod.calendar.SelectDecorator
import com.sejin.recordwod.databinding.FragmentMainBinding
import com.sejin.recordwod.view.viewmodel.MainViewModel
import java.time.LocalDate
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

        // 오늘 날짜
        val today = CalendarDay.today().toString().split("{","}").get(1)
//        val today = CalendarDay.today().year.toString() + "-" + CalendarDay.today().month.toString() + "-" + CalendarDay.today().day.toString()

        with(binding){
            updateBtn.setOnClickListener {
                val db = Firebase.firestore
                val wod = hashMapOf(
                    "wodId" to "1a2a3a4a",
                    "date" to today,
                    "wodNote" to "amrap squat snatch"
                )

                db.collection("wods").document(today)
                    .set(wod)
                    .addOnSuccessListener { Log.d("wow", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.d("wow", "Error writing document") }
            }

            recordLayout.mainWodTV.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_mainFragment_to_addWodFragment)
            }


        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)
}

