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
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        map ?: return
        mMap = map

        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        enableMyLocation()

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