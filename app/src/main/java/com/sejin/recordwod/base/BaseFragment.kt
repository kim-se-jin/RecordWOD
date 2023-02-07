package com.sejin.recordwod.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.sejin.recordwod.utils.autoCleaned
import com.sejin.recordwod.view.state.State
import com.sejin.recordwod.view.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// 추상화 클래스
// 클래스의 틀을 복제
abstract class BaseFragment<VB : ViewBinding> :
//abstract class BaseFragment<VB : ViewBinding, STATE : State, VM : BaseViewModel<STATE>> :
    Fragment() {
        private var _binding: VB by autoCleaned()
        val binding: VB get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    abstract fun initView()
//    abstract fun render(state: STATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
//        observeState()
    }

//    private fun observeState() {
//        viewModel.state
//            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
//            .onEach { state -> render(state) }
//            .launchIn(viewLifecycleOwner.lifecycleScope)
//    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    private var progressBar: ProgressBar? = null

    fun setProgressBar(bar: ProgressBar) {
        progressBar = bar
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

}
