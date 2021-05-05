package ipvc.estg.afproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import ipvc.estg.afproject.api.EndPoints
import ipvc.estg.afproject.api.ServiceBuilder
import ipvc.estg.afproject.api.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null
    private lateinit var text: TextView
    private lateinit var pb: CircularProgressBar
    private lateinit var square: TextView

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        square = findViewById(R.id.tv_square)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        text = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)

        setUpSensorStuff()

        setUpSensorStuff2()

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.login_p), Context.MODE_PRIVATE
        )

        if (sharedPref != null){
            if(sharedPref.all[getString(R.string.login_shared)]==true){
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }

    }

    fun button4(view: View) {

        val intent = Intent(this, MainActivity2::class.java)

        startActivity(intent)
    }

    fun buttonteste(view: View) {

        val intent = Intent(this, MapsActivity::class.java)

        startActivity(intent)
    }

    fun buttonMapa(view: View) {

       val intent = Intent(this, MapsActivity::class.java)

        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postTest(email.text.toString(), password.text.toString())

        call.enqueue(object :Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {

                if (response.isSuccessful){

                    for(Users in response.body()!!){

                        val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.login_p), Context.MODE_PRIVATE
                        )
                        with(sharedPref.edit()){
                            putBoolean(getString(R.string.login_shared), true)
                            putString(getString(R.string.nome), Users.nome)
                            putInt(getString(R.string.id), Users.id)
                            commit()
                        }
                    }

                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Toast.makeText(this@MainActivity, getString(R.string.errologin), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setUpSensorStuff2() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST,
                    SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light1 = event.values[0]

            text.text = "Sensor: $light1\n${brightness(light1)}"
            pb.setProgressWithAnimation(light1)
        }

        // if segundo sensor

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {

            val sides = event.values[0]

            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.BLUE else Color.GREEN
            square.setBackgroundColor(color)

            square.text = "Cima/Baixo ${upDown.toInt()}\nEsquerda/Direita ${sides.toInt()}"
        }

    }

    private fun brightness(brightness: Float): String {

        return when (brightness.toInt()) {
            0 -> "Pitch black"
            in 1..10 -> "Dark"
            in 11..50 -> "Grey"
            in 51..5000 -> "Normal"
            in 5001..25000 -> "Incredibly bright"
            else -> "This light will blind you"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        // Register a listener for the sensor.
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}
