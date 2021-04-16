package ipvc.estg.afproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.afproject.api.EndPoints
import ipvc.estg.afproject.api.Occurrences
import ipvc.estg.afproject.api.ServiceBuilder
import ipvc.estg.afproject.api.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var occurrences: List<Occurrences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllOccurrences()
        var position: LatLng

        Log.d("TAG_", call.toString() + "AQUIIIIIIIIIIIIIIIII")

        call.enqueue(object : Callback<List<Occurrences>> {
            override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                if (response.isSuccessful){

                    Log.d("TAG_", response.toString() + "AQUIIIIIIIIIIIIIIIII")

                    occurrences = response.body()!!

                    for(occurrences in occurrences){
                        position = LatLng(occurrences.latitude, occurrences.longitude)
                        mMap.addMarker(MarkerOptions().position(position).title(occurrences.titulo + " - " + occurrences.descricao))
                    }
                }
            }

            override fun onFailure(call: Call<List<Occurrences>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, getString(R.string.erromapa), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(41.6873737, -8.8372531)
        val zoomLevel = 8f;
        mMap.addMarker(MarkerOptions().position(sydney).title("Center of Viana do Castelo"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
    }

    fun addOccurrence(view: View) {

        val intent = Intent(this, AddOccurrence::class.java)
        // do map
        startActivity(intent)
    }

    fun logout(view: View) {

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.login_p), Context.MODE_PRIVATE
        )
        with(sharedPref.edit()){
            putBoolean(getString(R.string.login_shared), false)
            putString(getString(R.string.nome), "")
            putInt(getString(R.string.id), 0)
            commit()
        }

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

}