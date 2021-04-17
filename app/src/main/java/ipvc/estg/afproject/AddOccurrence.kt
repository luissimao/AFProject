package ipvc.estg.afproject

import android.R.attr.data
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.afproject.api.EndPoints
import ipvc.estg.afproject.api.Occurrences
import ipvc.estg.afproject.api.ServiceBuilder
import ipvc.estg.afproject.api.Users
import ipvc.estg.afproject.entities.Nota
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddOccurrence : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_occurrence)
    }

    fun back(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun submit(view: View) {

        var id: Int? = 0

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.login_p), Context.MODE_PRIVATE
        )

        if (sharedPref != null){
            id = sharedPref.all[getString(R.string.id)] as Int?
        }

        val titulo = findViewById<EditText>(R.id.occurrencetitle2)
        val descricao = findViewById<EditText>(R.id.occurrencedescription2)
        val imagem = 1
        val userId = 2
        val latitude = 1
        val longitude = 1

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.insert(titulo.text.toString(), descricao.text.toString(),imagem.toString(),latitude.toDouble(), longitude.toDouble(),userId)

        call.enqueue(object : Callback<Occurrences> {
            override fun onResponse(call: Call<Occurrences>?, response: Response<Occurrences>?) {

                Log.d("response_body", response!!.body()!!.toString());
                if (response!!.isSuccessful){

                    Log.d("erro", response.toString() + "EROROROROROROR");

                    val toast = Toast.makeText(applicationContext, "Inserido com sucesso..", Toast.LENGTH_SHORT)
                    toast.show()

                }
            }
            override fun onFailure(call: Call<Occurrences>?, t: Throwable?) {
                Toast.makeText(applicationContext, t!!.message, Toast.LENGTH_SHORT).show()
                Log.e("error", t!!.message.toString())
            }
        })
    }


}