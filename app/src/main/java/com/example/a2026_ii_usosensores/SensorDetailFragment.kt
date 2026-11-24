package com.example.a2026_ii_usosensores

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.a2026_ii_usosensores.databinding.FragmentGyroSensorBinding
import com.example.a2026_ii_usosensores.databinding.FragmentSensorDetailBinding
import com.google.android.material.appbar.MaterialToolbar

class SensorDetailFragment : Fragment(), SensorEventListener {

    private var tipoSensor: Int? = null
    var _binding: FragmentSensorDetailBinding? = null
    val binding get() = _binding!!


    val activityRecognitionPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result == true){
            Toast.makeText(this.context, "Se ha otorgado el permiso de reconocimiento de actividad", Toast.LENGTH_LONG).show()
        } else {
            // No location access granted.
            Toast.makeText(this.context, "No se ha otorgado el permiso de reconocimiento de actividad", Toast.LENGTH_LONG).show()
        }
    }


    lateinit var sensorManager: SensorManager
    var sensor:  Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tipoSensor = it.getInt(TIPO_SENSOR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSensorDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tipoSensor?.also { tipo ->
            sensor = sensorManager.getDefaultSensor(tipo)

            if (tipo == Sensor.TYPE_STEP_COUNTER){
                Log.d("SENSOR_STEP_COUNTER", "Se aha detectado el sensor : ${sensor?.name}")
                activityRecognitionPermissionRequest.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }

    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {

        event?.also {evento ->

            binding.tvNombre.text = "Nombre: " + evento.sensor?.name
            binding.tvTimestamp.text = "Timestamp: " + evento.timestamp.toString()
            binding.tvValores.text = evento.values.joinToString(prefix = "val = { ", postfix = " }", separator = ", ")
        }

    }


    override fun onPause() {
        super.onPause()

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this)
    }


    override fun onResume() {
        super.onResume()

        sensor?.also {
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        const val TIPO_SENSOR = "TIPO_SENSOR"
        @JvmStatic
        fun newInstance(tipoSensor: Int) =
            SensorDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("TIPO_SENSOR", tipoSensor)
                }
            }
    }
}