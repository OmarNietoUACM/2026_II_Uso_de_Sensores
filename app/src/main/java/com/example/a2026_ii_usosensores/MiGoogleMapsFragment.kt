package com.example.a2026_ii_usosensores

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MiGoogleMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

   private lateinit var googleMap: GoogleMap

    // se asigna la loalidad por default CDMX
    private  var localidad: LatLng = LatLng(19.42847, -99.12766)
    private lateinit var miMarcador: MarkerOptions //para solo tener un marcador


    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                updateLocalizacion()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                updateLocalizacion()
            }

            else -> {
                // No location access granted.
                Toast.makeText(this.context, "No se ha otorgado el permiso de localización", Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun updateLocalizacion() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location!=null) {
                    localidad = LatLng(location.latitude, location.longitude)
                    setNewLocation(localidad)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_mi_google_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    fun setNewLocation(location: LatLng){
        if (googleMap!=null) {

            googleMap.clear()
            miMarcador = MarkerOptions()
                .position(localidad)
                .title("Aqui estoy")


            googleMap.animateCamera(CameraUpdateFactory.zoomIn())
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(localidad))
            googleMap.addMarker(miMarcador)

        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        map.clear()
        setNewLocation(localidad)
    }

}