package com.example.a2016_ii_usosensores

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2026_ii_usosensores.databinding.FragmentSensorsBinding

class SensorsFragment : Fragment() {
    private var _binding: FragmentSensorsBinding? = null
    val binding get() = _binding!!

    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSensorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensores: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        binding.rvSensores.layoutManager = LinearLayoutManager(this.context)
        binding.rvSensores.adapter = SensorsListAdapter(this.activity, sensores)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}