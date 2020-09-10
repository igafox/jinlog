package com.igalogs.jinlog.home.serch

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.igalogs.jinlog.R
import com.igalogs.jinlog.data.model.Place
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_shrine_map.*

class ShrineMapFragment : Fragment() {

    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    private var placeListItemController = MapPlaceItemController()

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMarkerClickListener(markerOnClickListener)
    }

    private val markerOnClickListener = GoogleMap.OnMarkerClickListener {

        if (bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED) {
            //タッチしたマーカーの情報を表示

            val place = Place()
            placeListItemController.setData(listOf(place))

            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        return@OnMarkerClickListener true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shrine_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bottomSheet = BottomSheetBehavior.from(bottom_sheet_layout)
        setupList()
    }

    private fun setupList() {
        rv_shrine_list.apply {
            this.adapter = placeListItemController.adapter
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}