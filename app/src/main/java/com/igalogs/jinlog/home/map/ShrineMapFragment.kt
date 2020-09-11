package com.igalogs.jinlog.home.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.igalogs.jinlog.R
import com.igalogs.jinlog.data.model.Place
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_shrine_map.*


@AndroidEntryPoint
class ShrineMapFragment : Fragment() {

    private val viewModel: ShrineMapViewModel by viewModels()

    private lateinit var map: GoogleMap

    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    private val placeListItemController = MapPlaceItemController()

    private val placeMarker = mutableMapOf<Marker, Place>()

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val sydney = LatLng(37.6322797, 139.9011214)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMarkerClickListener(OnMarkerClickListener)
        googleMap.setOnCameraIdleListener(OnCameraIdleListener)
    }

    private val OnMarkerClickListener = GoogleMap.OnMarkerClickListener { marker ->

        if (bottomSheet.state != BottomSheetBehavior.STATE_COLLAPSED) {
            //タッチしたマーカーの情報を表示

            val selectPlace = placeMarker[marker]

            selectPlace ?: return@OnMarkerClickListener true

            placeListItemController.setData(listOf(selectPlace))

            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        return@OnMarkerClickListener true
    }

    private val OnCameraIdleListener = GoogleMap.OnCameraIdleListener {

        val latLng = map.cameraPosition.target
        viewModel.onCameraIdel(latLng)
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
        setupMarker()
    }

    private fun setupList() {
        rv_shrine_list.apply {
            this.adapter = placeListItemController.adapter
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupMarker() {
        viewModel.hitPlaces.observe(viewLifecycleOwner, Observer { places ->
            map.clear()
            placeMarker.clear()
            for (place in places) {
                val marker = createPlaceMarker(place)
                placeMarker[marker] = place
            }
        })
    }

    private fun createPlaceMarker(place: Place): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(LatLng(place.latitude, place.longitude))
                .title(place.name)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}