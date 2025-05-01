package bg.util.audio

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import bg.audio4.bgaudio4.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.math.sin

class ChartBgTFF(context2: Context, barChart2: BarChart) {

    var valArray: DoubleArray = DoubleArray(50)
    var i : Int =0;
    private  val context=context2
    private val barChart = barChart2
    private lateinit var dataSet: BarDataSet
    private val entries = mutableListOf<BarEntry>()
    private val updateInterval: Long = 100 // Intervalle en millisecondes
    private val maxEntries = 400

    private val handler = Handler(Looper.getMainLooper())

    init {

        setupChart()
        startUpdatingChart()
    }

    private fun setupChart() {
        dataSet = BarDataSet(entries, "Signal")
        dataSet.color = ContextCompat.getColor(context, R.color.purple_500)
        dataSet.setDrawValues(true) // Affiche les valeurs au-dessus des barres

        val data = BarData(dataSet)
        data.barWidth = 0.9f // Largeur de barre appropriée
        barChart.data = data

        barChart.apply {
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setMaxVisibleValueCount(maxEntries)
            setPinchZoom(false)
            setDrawGridBackground(false)
            description.isEnabled = false
            legend.isEnabled = false
            setFitBars(true) // Ajuste les barres pour qu'elles s'adaptent à l'axe X

            xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
            }

            axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = -2f // Ajustez selon vos données
                axisMaximum= 1000f
            }

            axisRight.isEnabled = false
        }

        barChart.invalidate()
    }

    private fun startUpdatingChart() {
        for (j in 1..50) {
            entries.add(BarEntry(entries.size.toFloat(), 0f))
        }
        handler.post(object : Runnable {

            override fun run() {
                // Normaliser la TFF
                val max: Double = (valArray.maxOrNull()?:10).toDouble()
               // Supprimer les données
                while( entries.size > 0){
                    entries.removeAt(0)
                }

                var j=0;
                while (j < maxEntries && j < valArray.size ) {
                    entries.add(BarEntry(j.toFloat(), 1000*(valArray[j]/max).toFloat()))
                    j++
                }




                dataSet.notifyDataSetChanged()
                barChart.data.notifyDataChanged()
                barChart.notifyDataSetChanged()
                //  barChart.moveViewToX(barChart.data.entryCount.toFloat())
                barChart.postInvalidate()

                handler.postDelayed(this, updateInterval)
            }
        })
    }


}