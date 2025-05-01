package bg.util.audio


import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.log10

import kotlin.math.hypot
import org.jtransforms.fft.DoubleFFT_1D

/**
 * Cette classe gère la partie audio Android
 */
class AudioBg(mainActivity: Activity, textView2: TextView, chartBgDb2: ChartBgDb, chartBgTff2:ChartBgTFF) {

    private val sampleRate = 8000 // Fréquence d'échantillonnage en Hz
    private val REQUEST_RECORD_AUDIO_PERMISSION_CODE = 200

    private val textView: TextView=textView2
    private val activity :Activity = mainActivity
    private val chartBgDb = chartBgDb2
    private val chartBgTFF = chartBgTff2

    private val listFFT:MutableList<DoubleFFT_1D> = mutableListOf()

    val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    init {

    }
    @SuppressLint("MissingPermission")
    val audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    )

    @RequiresPermission(permission.RECORD_AUDIO)
    fun startAudio() {
        Log.i("bg22","startAudio")
        managePermission();

        Log.e("bg22","audioRecord.state  "+audioRecord.state)
        if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
            // Handle initialization failure
            Log.e("bg", "Bg Failure audioRecord not initialized yet audioRecord.state: "+audioRecord.state)
            textView.setText("Bg Failure audioRecord not initialized yet "+audioRecord.state )
            return
        }

        val buffer = ShortArray(bufferSize)


        Thread {
            Log.i("bg22","startRecording")
            audioRecord.startRecording()
            // Read audio data here
        }.start()
        Thread {
            while (true) {
                var decibel2 =0f

                val readCount = audioRecord.read(buffer, 0, buffer.size)
                if (readCount > 0) {
                    var magnetude: DoubleArray = performFFT(buffer)
                    Log.i("bg222","Buffer size :"+buffer.size+" Magnetude size: "+magnetude.size+"  ")
                    chartBgTFF.valArray = magnetude
                    val amplitude = buffer.map { it.toInt() }.maxOrNull() ?: 0
                    if (amplitude==0){
                       chartBgDb.decibel  = -5f
                    }else {
                        decibel2 = 20 * log10(amplitude.toFloat())
                        if (decibel2 > chartBgDb.decibel) {
                            chartBgDb.decibel = decibel2
                        }
                    }
                    activity.runOnUiThread {
                        // Mettez à jour l'interface utilisateur avec la valeur de decibel
                        textView.text = "db : ${"%.0f".format(decibel2)} dB readCount: $readCount"
                    }
                }
            }
        }.start()
    }

    private fun managePermission() {
        // Vérifier si la permission est déjà accordée
        if (ContextCompat.checkSelfPermission(activity, permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Expliquer pourquoi la permission est nécessaire (facultatif)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.RECORD_AUDIO)) {
                // Afficher une explication à l'utilisateur
                // Par exemple, utiliser un AlertDialog pour informer l'utilisateur
            } else {
                // Demander la permission
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO_PERMISSION_CODE
                )
            }
        } else {
            // La permission est déjà accordée, procéder à l'enregistrement audio
            Log.e("bg2","La permission est déjà accordée, procéder à l'enregistrement audio")
        }
    }




    fun performFFT(audioData: ShortArray): DoubleArray {
        val n = audioData.size
        val fft = DoubleFFT_1D(n.toLong()) // DoubleFFT_1D

        // Convertir les données audio en tableau de doubles
        val realData = DoubleArray(n)
        for (i in audioData.indices) {
            realData[i] = audioData[i].toDouble()
        }

        // Appliquer la FFT en place
        fft.realForward(realData)

        listFFT.add(fft)
        if(listFFT.size > 10000){
            listFFT.removeAt(0)
        }

        // Calculer la magnitude du spectre

        val magnitudes2 = calculMagnetude(realData)
        return magnitudes2
    }

    /**
     * Calculer la magnitude du spectre
     */
    fun calculMagnetude(realData:DoubleArray):DoubleArray{
        val n = realData.size
        val magnitudes = DoubleArray(n / 2)
        for (i in magnitudes.indices) {
            val real = realData[2 * i]
            val imag = realData[2 * i + 1]
            magnitudes[i] = hypot(real, imag)
        }
        return magnitudes
    }


}