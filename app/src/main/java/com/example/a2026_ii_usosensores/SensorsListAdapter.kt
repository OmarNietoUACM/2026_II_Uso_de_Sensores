package com.example.a2016_ii_usosensores

import android.content.Context
import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.a2026_ii_usosensores.R
import com.example.a2026_ii_usosensores.SensorDetailFragment
import com.example.a2026_ii_usosensores.databinding.ItemSensorViewBinding

class SensorsListAdapter(val context: Context?, val listaSensores: List<Sensor>) :
    RecyclerView.Adapter<SensorsListAdapter.SensorHolder>() {


    class SensorHolder(val itemBinding: ItemSensorViewBinding):
        RecyclerView.ViewHolder(itemBinding.root) {
            fun llenarItemView(context: Context?, sensor: Sensor){
                itemBinding.tvNombre.text = "Nombre: " + sensor.name
                itemBinding.tvVendedor.text = "Vendedor: " + sensor.vendor
                itemBinding.tvVersion.text = "Versión: " + sensor.version.toString()
                itemBinding.tvTipo.text = "Tipo: " + sensor.type.toString()
                itemBinding.tvMaxRan.text = "MaxRan: " + sensor.maximumRange.toString()
                itemBinding.tvResolucion.text = "Resolución: " + sensor.resolution.toString()
                itemBinding.tvPotencia.text = "Potencia: " + sensor.power.toString()
                itemBinding.tvMinDelay.text = "MinDelay: " + sensor.minDelay.toString()

                itemBinding.cardView.setOnClickListener {
                    context as AppCompatActivity
                    context.supportFragmentManager.commit {
                        val sensorDetalleFragment = SensorDetailFragment.newInstance(sensor.type)
                        replace(R.id.fragment_container_view, sensorDetalleFragment, "SENSORES_DETALLE")
                        setReorderingAllowed(true)
                        addToBackStack("SENSORES_DETALLE")
                    }
                }

            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorHolder {
       val itemBinding  = ItemSensorViewBinding.inflate(
           LayoutInflater.from(parent.context),
           parent, false
       )
        return SensorHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SensorHolder, position: Int) {
       val sensor = listaSensores[position]
        holder.llenarItemView(context, sensor)
    }

    override fun getItemCount(): Int {
        return listaSensores.size
    }

}