package bg.audio4.bgaudio4

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bg.util.audio.AudioBg
import bg.util.audio.ChartBgDb
import bg.util.audio.ChartBgTFF
import com.github.mikephil.charting.charts.BarChart


class MainActivity : AppCompatActivity() {


    private lateinit var audioBg: AudioBg


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val barChartBgDb:BarChart = findViewById(R.id.barChartDb)
        val barChartBgTFF:BarChart = findViewById(R.id.barChartTFF)
        val textView: TextView = findViewById(R.id.textView2)
        val monBouton: Button = findViewById(R.id.button)
        val chartBgDB = ChartBgDb(this,barChartBgDb);
        val chartBgTFF = ChartBgTFF(this,barChartBgTFF);

        audioBg = AudioBg(this,textView,chartBgDB,chartBgTFF)
        monBouton.setOnClickListener {
            Log.i("bg2","clickButton");
            audioBg.startAudio();
        }
    }
}