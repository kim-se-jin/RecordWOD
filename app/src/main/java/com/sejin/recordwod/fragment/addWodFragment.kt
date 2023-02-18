package com.sejin.recordwod.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.sejin.recordwod.R
import com.sejin.recordwod.base.BaseFragment
import com.sejin.recordwod.calendar.EventDecorator
import com.sejin.recordwod.calendar.SelectDecorator
import com.sejin.recordwod.databinding.FragmentAddWodBinding
import com.sejin.recordwod.databinding.FragmentMainBinding
import com.sejin.recordwod.view.viewmodel.AddWodViewModel
import java.util.*
//
//class addWodFragment : BaseFragment<FragmentAddWodBinding>() {
//
//    companion object {
//        fun newInstance() = addWodFragment()
//    }
//
//    private lateinit var viewModel: AddWodViewModel
//
//    // 뷰가 생성될 때 호출
//    override fun initView() {
//        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.programName, android.R.layout.simple_spinner_item)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.programSpinner.adapter = adapter
//
//    }
//
//    override fun getViewBinding(
//        inflater: LayoutInflater,
//        container: ViewGroup?
//    ) = FragmentAddWodBinding.inflate(inflater, container, false)
//
//}