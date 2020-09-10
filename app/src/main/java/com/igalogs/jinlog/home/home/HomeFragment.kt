package com.igalogs.jinlog.home.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.igalogs.jinlog.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

  private val homeViewModel: HomeViewModel by viewModels()

  private val itemController = HomeItemController()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_home, container, false)
    homeViewModel.get()
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    setupList()
    setupViewModelObseve()
  }

  private fun setupViewModelObseve() {
    homeViewModel.logs.observe(viewLifecycleOwner, Observer { logs ->
      itemController.setData(logs)
    })
  }

  private fun setupList() {
    val spanCount = 2
    recyclerView.apply {
      this.adapter = itemController.adapter
      this.layoutManager = GridLayoutManager(requireContext(),spanCount).apply {
        itemController.spanCount = spanCount
        this.spanSizeLookup = itemController.spanSizeLookup
      }
      setItemSpacingDp(6)
    }
  }

}