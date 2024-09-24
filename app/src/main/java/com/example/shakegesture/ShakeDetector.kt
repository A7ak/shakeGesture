package com.example.shakegesture

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast

class ShakeDetector(private val context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var shakeThreshold: Float = 15f
    private var lastUpdate: Long = 0
    private var shakeTimeThreshold: Int = 500


    fun start() {
        // Register listener
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        // Unregister listener
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val shakeMagnitude = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastUpdate) > shakeTimeThreshold) {
                if (shakeMagnitude > shakeThreshold) {
                    lastUpdate = currentTime
                    onShake()
                }
            }
        }
    }

    private fun onShake() {
        // Handle shake event here
        Toast.makeText(context, "Shake detected!", Toast.LENGTH_LONG).show()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }
}
