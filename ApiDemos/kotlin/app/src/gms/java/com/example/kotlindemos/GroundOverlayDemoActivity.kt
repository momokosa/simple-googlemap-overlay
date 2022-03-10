// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.kotlindemos

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnGroundOverlayClickListener
import com.google.android.gms.maps.model.*
import java.util.*
import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.CameraUpdateFactory

import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.GoogleMapOptions





/**
 * This shows how to add a ground overlay to a map.
 */
class GroundOverlayDemoActivity : AppCompatActivity(), OnSeekBarChangeListener,
    OnMapReadyCallback, OnGroundOverlayClickListener, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private val images: MutableList<BitmapDescriptor> = ArrayList()
    private val imagesPhp: MutableList<BitmapDescriptor> = ArrayList()
    private var groundOverlay: GroundOverlay? = null
    private var groundOverlayRotated: GroundOverlay? = null
    private lateinit var transparencyBar: SeekBar
    private var currentEntry = 0
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ground_overlay_demo)
        transparencyBar = findViewById(R.id.transparencySeekBar)
        transparencyBar.max = TRANSPARENCY_MAX
        transparencyBar.progress = 0
        var mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        val options = GoogleMapOptions()
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
            .compassEnabled(true)
            .rotateGesturesEnabled(false)
            .tiltGesturesEnabled(false)

//        mapFragment = SupportMapFragment.newInstance(options)
        //        val mapFragment =
//            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        map ?: return
//        map.uiSettings.isRotateGesturesEnabled = false
//        map.uiSettings.isTiltGesturesEnabled = false
        mMap = map

        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
//        enableMyLocation()
        enableCoarseLocation()

        // Register a listener to respond to clicks on GroundOverlays.
        mMap.setOnGroundOverlayClickListener(this)
        /*map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                NEWARK,
                11f
            )
        )*/
//        val location: Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        mMap.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    location.latitude,
//                    location.longitude
//                ), 20.0f
//            )
//        )
//        mMap.moveCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                PHP_FLOOR_32,
//                25f
//            )
//        )
        images.clear()
//        images.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_nj_1922))
        images.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_nj_1922))
        images.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_prudential_sunny))

        imagesPhp.clear()
        imagesPhp.add(BitmapDescriptorFactory.fromResource(R.drawable.php_32nd_floorplan_512_flip))
        imagesPhp.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_prudential_sunny))

        // Add a small, rotated overlay that is clickable by default
        // (set by the initial state of the checkbox.)
        /*groundOverlayRotated = map.addGroundOverlay(
            GroundOverlayOptions()
                .image(images[1]).anchor(0f, 1f)
                .position(NEAR_NEWARK, 4300f, 3025f)
                .bearing(30f)
                .clickable((findViewById<View>(R.id.toggleClickability) as CheckBox).isChecked)
        )*/
//        groundOverlayRotated = map.addGroundOverlay(
//            GroundOverlayOptions()
//                .image(imagesPhp[1]).anchor(0f, 1f)
//                .position(PHP_FLOOR_32, 4300f, 3025f)
//                .bearing(30f)
//                .clickable((findViewById<View>(R.id.toggleClickability) as CheckBox).isChecked)
//        )

        // Add a large overlay at Newark on top of the smaller overlay.
        /*groundOverlay = map.addGroundOverlay(
            GroundOverlayOptions()
                .image(images[currentEntry]).anchor(0f, 1f)
                .position(NEWARK, 8600f, 6500f)
        )*/
        groundOverlay = mMap.addGroundOverlay(
            GroundOverlayOptions()
                .image(imagesPhp[0]).anchor(0f, 0f)
                .bearing(19f)
                .position(PHP_FLOOR_32, 99f)
        )
        transparencyBar.setOnSeekBarChangeListener(this)

        mMap.addMarker(
            MarkerOptions()
                .position(PHP_FLOOR_32)
                .title("Marker in PHP_FLOOR_32")
        )

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Google Map with ground overlay.")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        groundOverlay?.transparency = progress.toFloat() / TRANSPARENCY_MAX.toFloat()
    }

    /*fun switchImage(view: View?) {
        val overlay = groundOverlay ?: return
        currentEntry = (currentEntry + 1) % images.size
        overlay.setImage(images[currentEntry])
    }*/
    fun switchImage(view: View?) {
        val overlay = groundOverlay ?: return
        currentEntry = (currentEntry + 1) % images.size
        overlay.setImage(imagesPhp[currentEntry])
    }

    /**
     * Toggles the visibility between 100% and 50% when a [GroundOverlay] is clicked.
     */
    override fun onGroundOverlayClick(groundOverlay: GroundOverlay) {
        // Toggle transparency value between 0.0f and 0.5f. Initial default value is 0.0f.
        val overlayRotated = groundOverlayRotated ?: return
        overlayRotated.transparency = 0.5f - overlayRotated.transparency
    }

    /**
     * Toggles the clickability of the smaller, rotated overlay based on the state of the View that
     * triggered this call.
     * This callback is defined on the CheckBox in the layout for this Activity.
     */
    fun toggleClickability(view: View) {
        groundOverlayRotated?.isClickable = (view as CheckBox).isChecked
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::mMap.isInitialized) return
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this,
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                true
            )
        }
        // [END maps_check_location_permission]
    }

    private fun enableCoarseLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.e("TAG", "enableCoarseLocation: -> PERMISSION_GRANTED" )
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isCompassEnabled = true
            getCurrentMyLocation()
        }else{
            Log.e("TAG", "enableCoarseLocation: -> PERMISSION_DENIED" )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                COARSE_AND_FIND_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                COARSE_AND_FIND_LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        } else {
            true
        }
    }

    private var currentLocation: Location? = null
    lateinit var locationManager: LocationManager
    lateinit var locationManager2: LocationManager
//    val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    private var hasGps = false
//    val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    private var hasNetwork = false
//    lateinit var locationByGps:Location
//    lateinit var locationByNetwork:Location
    var locationByGps:Location? = null
    var locationByNetwork:Location? = null
    val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Toast.makeText(applicationContext, "gpsLocationListener accuracy -> "+location.accuracy, Toast.LENGTH_SHORT).show()
            locationByGps= location
            animateCamera(locationByGps!!.latitude, locationByGps!!.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    //------------------------------------------------------//
    val networkLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Toast.makeText(applicationContext, "networkLocationListener accuracy -> "+location.accuracy, Toast.LENGTH_SHORT).show()
            locationByNetwork= location
            animateCamera(locationByNetwork!!.latitude, locationByNetwork!!.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    /*@SuppressLint("MissingPermission")
    val lastKnownLocationByGps: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    @SuppressLint("MissingPermission")
    val lastKnownLocationByNetwork: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)*/
    @SuppressLint("MissingPermission")
    lateinit var lastKnownLocationByGps: Location
    @SuppressLint("MissingPermission")
    lateinit var lastKnownLocationByNetwork: Location

    var latitude:Double = 0.0
    var longitude:Double = 0.0

    @SuppressLint("MissingPermission")
    private fun getCurrentMyLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager2 = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null) lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(locationManager2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null) lastKnownLocationByNetwork = locationManager2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//        lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        lastKnownLocationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null){
            if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
                lastKnownLocationByGps?.let {
                    locationByGps = lastKnownLocationByGps
                }
            }
        }
        //------------------------------------------------------//
        if(locationManager2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null){
//        if(false){
            if (hasNetwork) {
                locationManager2.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    networkLocationListener
                )
                lastKnownLocationByNetwork?.let {
                    locationByNetwork = lastKnownLocationByNetwork
                }
            }
        }
        if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps!!.accuracy > locationByNetwork!!.accuracy) {
                currentLocation = locationByGps
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
                // use latitude and longitude as per your need
                animateCamera(latitude, longitude)
            } else {
                currentLocation = locationByNetwork
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
                // use latitude and longitude as per your need
                animateCamera(latitude, longitude)
            }
        }
    }

    private fun animateCamera(latitude: Double, longitude: Double) {
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latitude,
                    longitude
                ), 20.0f
            )
        )
    }


    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TRANSPARENCY_MAX = 100
        private val NEWARK = LatLng(40.714086, -74.228697)
        private val NEAR_NEWARK = LatLng(
            NEWARK.latitude - 0.001,
            NEWARK.longitude - 0.025
        )
        private val PHP_FLOOR_32 = LatLng(13.783071938762749, 100.54601098407117)
//        private val PHP_FLOOR_32 = LatLng(13.782801021311283, 100.54591911841929)
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val COARSE_AND_FIND_LOCATION_PERMISSION_REQUEST_CODE = 1
    }

//    override fun onLocationChanged(p0: Location?) {
//        Log.e("onLocationChanged", " ")
//        if(mMap.isMyLocationEnabled&&p0!=null){
//            Log.e("onLocationChanged", "OK")
//            mMap.moveCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    LatLng(p0.latitude, p0.longitude),
//                    25f
//                )
//            )
//        }
//    }
}