package ipvc.estg.afproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.afproject.api.EndPoints
import ipvc.estg.afproject.api.ServiceBuilder
import ipvc.estg.afproject.api.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.login_p), Context.MODE_PRIVATE
        )

        if (sharedPref != null){
            if(sharedPref.all[getString(R.string.login_shared)]==true){
                var intent = Intent(this, MapScreen::class.java)
                startActivity(intent)
            }
        }

    }

    fun button4(view: View) {

        val intent = Intent(this, MainActivity2::class.java)

        startActivity(intent)
    }

    fun buttonMapa(view: View) {

       val intent = Intent(this, MapScreen::class.java)

        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postTest(email.text.toString(), password.text.toString())

        call.enqueue(object :Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {

                if (response.isSuccessful){

                    for(Users in response.body()!!){

                        Log.d("TAG_", Users.email.toString() + Users.password.toString())

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
}
