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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

    // update and delete

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(41.6873737, -8.8372531)
        val zoomLevel = 8f;
        mMap.addMarker(MarkerOptions().position(sydney).title("Center of Viana do Castelo"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllOccurrences()
        var position: LatLng
        var id: Int? = 0

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.login_p), Context.MODE_PRIVATE
        )

        if (sharedPref != null){
            id = sharedPref.all[getString(R.string.id)] as Int?
        }

        call.enqueue(object : Callback<List<Occurrences>> {
            override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                if (response.isSuccessful){

                    occurrences = response.body()!!

                    for(occurrence in occurrences){
                        position = LatLng(occurrence.latitude, occurrence.longitude)

                        Log.d("occurrences.user_id", occurrence.user_id.toString());
                        Log.d("_id", id.toString());

                        if(occurrence.user_id == id) {

                            mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                        } else {
                            mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Occurrences>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, getString(R.string.erromapa), Toast.LENGTH_SHORT).show()
            }
        })

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