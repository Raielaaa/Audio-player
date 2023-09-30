package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    //Initializing an object of MediaPlayer Class
    private var mediaPlayerClap: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    //Initializing the button
    private lateinit var fabPlayView: FloatingActionButton
    private lateinit var fabPauseView: FloatingActionButton
    private lateinit var fabStopView: FloatingActionButton
    private lateinit var sbAudioView: SeekBar

    private lateinit var decimalFormat: DecimalFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabPlayView = findViewById(R.id.fabPlay)
        fabPauseView = findViewById(R.id.fabPause)
        fabStopView = findViewById(R.id.fabStop)
        sbAudioView = findViewById(R.id.sbAudio)
        handler = Handler(Looper.getMainLooper())

        fabPlayView.setOnClickListener {
            if (mediaPlayerClap == null) {
                mediaPlayerClap = MediaPlayer.create(this@MainActivity, R.raw.music)
                initializeSeekbar()
            }
            mediaPlayerClap?.start()
        }
        fabPauseView.setOnClickListener {
            mediaPlayerClap?.pause()
        }
        fabStopView.setOnClickListener {
            mediaPlayerClap?.stop()
            mediaPlayerClap?.reset()
            mediaPlayerClap?.release()
            mediaPlayerClap = null
            handler.removeCallbacks(runnable)
            sbAudioView.progress = 0
        }
    }
    private fun initializeSeekbar() {
        sbAudioView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sbAudioView: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayerClap?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
        val tvStartView: TextView = findViewById(R.id.tvStart)
        val tvEndView: TextView = findViewById(R.id.tvEnd)
        decimalFormat = DecimalFormat("00")

        val duration: Int = mediaPlayerClap!!.duration / 1000
        var minutes: Int = duration / 60
        var hour: Int = minutes / 60
        var seconds: Int = duration / 60 % 60

        sbAudioView.max = mediaPlayerClap!!.duration
        runnable = Runnable {
            sbAudioView.progress = mediaPlayerClap!!.currentPosition

            val playedTime: Int = mediaPlayerClap!!.currentPosition / 1000
            tvStartView.text = "$playedTime sec"

            tvEndView.text = "${decimalFormat.format(hour)}:${decimalFormat.format(minutes)}:${decimalFormat.format(seconds)}"
            seconds--
            if (seconds == -1) {
                seconds = 59
                minutes--
                tvEndView.text = "${decimalFormat.format(hour)}:${decimalFormat.format(minutes)}:${decimalFormat.format(seconds)}"
            }
            if (minutes == -1) {
                minutes == 59
                hour--
                tvEndView.text = "${decimalFormat.format(hour)}:${decimalFormat.format(minutes)}:${decimalFormat.format(seconds)}"
            }
            //Original from the vid.............tvEndView.text = "${duration - playedTime} sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}