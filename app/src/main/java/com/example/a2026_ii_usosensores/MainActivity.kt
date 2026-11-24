package com.example.a2026_ii_usosensores

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.a2016_ii_usosensores.SensorsFragment
import com.example.a2026_ii_usosensores.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         */

        binding.bottomNavigation.setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.home_menu-> {
                        supportFragmentManager.commit {
                            replace<SensorsFragment>(binding.fragmentContainerView.id)
                            setReorderingAllowed(true)
                            addToBackStack("Main_fragment") // Name can be null
                        }
                        true
                    }
                    R.id.gyro_menu -> {
                        supportFragmentManager.commit {
                            replace<CompassSensorFragment>(binding.fragmentContainerView.id)
                            setReorderingAllowed(true)
                            addToBackStack("Gyro") // Name can be null
                        }
                        true
                    }
                    R.id.gps_menu -> {
                        supportFragmentManager.commit {
                            replace<GPSFragment>(binding.fragmentContainerView.id)
                            setReorderingAllowed(true)
                            addToBackStack("GPS") // Name can be null
                        }
                        true
                    }
                    R.id.mapa_menu -> {
                        // Respond to navigation item 2 click
                        supportFragmentManager.commit {
                            replace<MiGoogleMapsFragment>(binding.fragmentContainerView.id)
                            setReorderingAllowed(true)
                            addToBackStack("Mapa") // Name can be null
                        }
                        true
                    }
                    else -> false
                }

        }


    }


    /*
    override fun onResume() {
        super.onResume()

        sensorAcel?.also { acelerometro ->
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL)

        }
    }


    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //


    }

    override fun onSensorChanged(event: SensorEvent?) {
        /*
        binding.tvDatos.text = "x = " + event?.values[0].toString() + ",  " +
        "y = " + event?.values[1].toString() + ",  " +
        "z = " + event?.values[2].toString() + ""
*/
    }

     */
}



