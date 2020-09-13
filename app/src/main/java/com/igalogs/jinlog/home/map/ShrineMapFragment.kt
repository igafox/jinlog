package com.igalogs.jinlog.home.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_shrine_map.*
import kotlinx.android.synthetic.main.fragment_shrine_map.toolbar
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@AndroidEntryPoint
@RuntimePermissions
class ShrineMapFragment : Fragment() {

    private val viewModel: ShrineMapViewModel by viewModels()

    private lateinit var map: GoogleMap

    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    private val placeListItemController = MapPlaceItemController()

    private val visibleMarkers = mutableMapOf<Marker, Place>()

    private lateinit var locationClient: FusedLocationProviderClient

    //private lateinit var settingClient: SettingsClient

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        googleMap.setOnMarkerClickListener(OnMarkerClickListener)
        googleMap.setOnCameraIdleListener(OnCameraIdleListener)
        setupMapLocationSetting()
    }

    private val OnMarkerClickListener = GoogleMap.OnMarkerClickListener { marker ->

        //タッチしたマーカーの情報を表示
        val selectPlace = visibleMarkers[marker]
        selectPlace ?: return@OnMarkerClickListener true

        val list = visibleMarkers.values.toMutableList()
        val index: Int = list.indexOf(selectPlace)
        list.removeAt(index)
        list.add(0, selectPlace)

        placeListItemController.setData(list)

        if (bottomSheet.state != BottomSheetBehavior.STATE_COLLAPSED) {
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
        locationClient = FusedLocationProviderClient(requireActivity())

        setupList()
        setupMarker()
        setupFab()
        setupBottomSheet()
    }

    private fun setupMapLocationSetting() {
        val locationPermissionState = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (locationPermissionState == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false
            moveMapCameraToCurrentLocationWithPermissionCheck(false)
        }
    }


    private fun setupList() {
        rv_shrine_list.apply {
            this.adapter = placeListItemController.adapter
            this.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupFab() {
        fab_list.setOnClickListener {
            val list = visibleMarkers.values.toMutableList()
            placeListItemController.setData(list)
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        fab_location.setOnClickListener {
            moveMapCameraToCurrentLocationWithPermissionCheck(true)
        }
    }

    private fun setupBottomSheet() {
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            bottom_sheet_layout?.apply {
                val params = bottom_sheet_layout.layoutParams
                params.height = requireView().height - toolbar.height
                this.layoutParams = params
            }
        }

        bottomSheet = BottomSheetBehavior.from(bottom_sheet_layout).apply {
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0.45) {
                    fab_list.visibility = View.INVISIBLE
                    fab_location.visibility = View.INVISIBLE
                } else {
                    fab_list.visibility = View.VISIBLE
                    fab_location.visibility = View.VISIBLE
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })


    }

    private fun setupMarker() {
        viewModel.hitPlaces.observe(viewLifecycleOwner, Observer { places ->
            //非表示になったものだけ抽出
            val ignoreMarkers = visibleMarkers.filterValues { place -> !places.contains(place) }
            ignoreMarkers.forEach { entry ->
                entry.key.remove()
                visibleMarkers.remove(entry.key)
            }

            for (place in places) {
                if (!visibleMarkers.containsValue(place)) {
                    val marker = createPlaceMarker(place)
                    visibleMarkers[marker] = place
                }
            }
        })
    }


    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun moveMapCameraToCurrentLocation(animate: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
//            val locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(500)
//                .setFastestInterval(300)
//
//            //GPS有効判定
//            try {
//                val builder = LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest)
//                val settingClient: SettingsClient = LocationServices.getSettingsClient(requireContext())
//                settingClient.checkLocationSettings(builder.build()).await()
//            } catch (e: ResolvableApiException) {
//                e.startResolutionForResult(requireParentFragment().activity, REQUEST_CODE_ENABLE_LOCATION)
//                return@launch
//            }

            val location = locationClient.lastLocation.await()
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15F)
                if (animate) {
                    map.animateCamera(cameraUpdate)
                } else {
                    map.moveCamera(cameraUpdate)
                }
            } else {


            }
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_ENABLE_LOCATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    setupMapLocationSetting()
                }
            }
        }
    }

    companion object {

        const val REQUEST_CODE_ENABLE_LOCATION = 1001

    }

}