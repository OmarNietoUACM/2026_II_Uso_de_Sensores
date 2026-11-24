package com.example.a2026_ii_usosensores

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.a2026_ii_usosensores.databinding.FragmentGPSBinding
import com.example.a2026_ii_usosensores.databinding.FragmentGyroSensorBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GPSFragment : Fragment() {
    var _binding: FragmentGPSBinding? = null
    val binding get() = _binding!!


    private lateinit var fusedLocationClient: FusedLocationProviderClient


    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                updateLocalizacion()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                updateLocalizacion()
            }

            else -> {
                // No location access granted.
            }
        }
    }


    private fun getStrDateTime(time: Long?): String {
        var strDateTime = "Fecha-Hora = "
        if (time != null){
            val instant = Instant.ofEpochMilli(time!!)
            val formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd hh:mm:ss")
            strDateTime += formatter.format(instant.atZone(ZoneId.systemDefault())).toString()
        }
        return strDateTime
    }


    private fun updateLocalizacion() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                binding.tvNombre.text = "Sensor GPS"
                binding.tvLatitud.text = "Latitud = ${location?.latitude ?: "?"}"
                binding.tvLongitud.text = "Longitud = ${location?.longitude ?: "?"}"
                binding.tvAltitud.text = "Altitud = ${location?.altitude ?: "?"}"
                binding.tvFechaHora.text = getStrDateTime(location?.time)
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
        _binding = FragmentGPSBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        binding.tvLatitud.text = "Latitud = "
        binding.tvLongitud.text = "Longitud = "
        binding.tvAltitud.text = "Altitud = "
        binding.tvFechaHora.text = "Altitud = "
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}