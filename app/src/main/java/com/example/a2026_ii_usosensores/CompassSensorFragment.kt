package com.example.a2026_ii_usosensores

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.example.a2026_ii_usosensores.databinding.FragmentGyroSensorBinding

class CompassSensorFragment : Fragment(), SensorEventListener {

    var _binding: FragmentGyroSensorBinding? = null
    val binding get() = _binding!!

    lateinit var sensorManager: SensorManager

    var sensorGravity:  Sensor? = null
    var sensorMagnet:  Sensor? = null

    val lastGravity = FloatArray(3)
    val lastMagnet = FloatArray(3)
    val rotacionMatrix = FloatArray(9)
    val orientacion = FloatArray(3)


    var seHaCopiadoLastGrav = false
    var seHaCopiadoLastMagnet = false

    var lastUpdatedTime = 0L
    var actualGrados = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGyroSensorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {

        event?.also {evento ->

            if (event.sensor == sensorGravity) {
                event.values.copyInto(lastGravity)
                seHaCopiadoLastGrav = true
            } else if (event.sensor == sensorMagnet) {
                event.values.copyInto(lastMagnet)
                seHaCopiadoLastMagnet = true
            }

            val updateTime = System.currentTimeMillis() - lastUpdatedTime > 250

            if (seHaCopiadoLastGrav && seHaCopiadoLastMagnet && updateTime) {
                SensorManager.getRotationMatrix(rotacionMatrix, null, lastGravity, lastMagnet)
                SensorManager.getOrientation(rotacionMatrix, orientacion)

                val azimuthRad = orientacion[0]
                val azimuthGrad = Math.toDegrees(azimuthRad.toDouble()).toFloat()

                val rotateAnimation = RotateAnimation(
                    actualGrados, -azimuthGrad,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )

                rotateAnimation.duration = 250
                rotateAnimation.fillAfter = true

                binding.imageViewCompass.startAnimation(rotateAnimation)

                actualGrados = -azimuthGrad
                lastUpdatedTime = System.currentTimeMillis()

                val x = azimuthGrad.toInt()
                binding.tvNorte.text = "N = ${x}°"
            }

            if (event.sensor == sensorMagnet) {
                binding.tvNombre.text = "Nombre: " + event.sensor?.name
                binding.tvX.text = "x: " + event.values[0].toString()
                binding.tvY.text = "y: " + event.values[1].toString()
                binding.tvZ.text = "z: " + event.values[2].toString()
                binding.tvTimestamp.text = "Timestamp: " + event.timestamp.toString()
            }
        }
    }


    override fun onPause() {
        super.onPause()

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this, sensorGravity)
        sensorManager.unregisterListener(this, sensorMagnet)
    }


    override fun onResume() {
        super.onResume()

        sensorGravity?.also {
            sensorManager.registerListener(this, sensorGravity,SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorMagnet?.also {
            sensorManager.registerListener(this, sensorMagnet,SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

}