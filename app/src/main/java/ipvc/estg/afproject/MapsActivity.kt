package ipvc.estg.afproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var occurrences: List<Occurrences>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var results = FloatArray(1)

    // update and delete

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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

        if (sharedPref != null) {
            id = sharedPref.all[getString(R.string.id)] as Int?
        }

        call.enqueue(object : Callback<List<Occurrences>> {
            override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                if (response.isSuccessful) {

                    occurrences = response.body()!!

                    for (occurrence in occurrences) {
                        position = LatLng(occurrence.latitude, occurrence.longitude)

                        if (occurrence.user_id == id) {
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

    fun calculateDistance(lat1: Double, lng1: Double, lat2:Double, lng2:Double) : Float {

        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }
// teste filtros branch
    fun addOccurrence(view: View) {

        val intent = Intent(this, AddOccurrence::class.java)
        // do map
        startActivity(intent)
    }

    fun filtro15km(view: View) {

        Toast.makeText(applicationContext, R.string.filtro15km, Toast.LENGTH_SHORT).show()

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123)

            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    mMap.clear()

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

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id)] as Int?
                    }

                    call.enqueue(object : Callback<List<Occurrences>> {
                        override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                            if (response.isSuccessful) {

                                occurrences = response.body()!!

                                for (occurrence in occurrences) {
                                    position = LatLng(occurrence.latitude, occurrence.longitude)

                                    if (calculateDistance(41.6873737, -8.8372531, occurrence.latitude, occurrence.longitude) < 15000) {
                                        mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                                    } else {
                                        mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))

                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Occurrences>>, t: Throwable) {
                            Toast.makeText(this@MapsActivity, getString(R.string.erromapa), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        }
    }

    fun filtro35km(view: View) {

        Toast.makeText(applicationContext, R.string.filtro35km, Toast.LENGTH_SHORT).show()

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123)

            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    mMap.clear()

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

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id)] as Int?
                    }

                    call.enqueue(object : Callback<List<Occurrences>> {
                        override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                            if (response.isSuccessful) {

                                occurrences = response.body()!!

                                for (occurrence in occurrences) {
                                    position = LatLng(occurrence.latitude, occurrence.longitude)

                                    if (calculateDistance(41.6873737, -8.8372531, occurrence.latitude, occurrence.longitude) < 35000) {
                                        mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                                    } else {
                                        mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))

                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Occurrences>>, t: Throwable) {
                            Toast.makeText(this@MapsActivity, getString(R.string.erromapa), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        }
    }

    fun filtro65k(view: View) {

        Toast.makeText(applicationContext, R.string.filtro65km, Toast.LENGTH_SHORT).show()

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123)

            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    mMap.clear()

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

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id)] as Int?
                    }

                    call.enqueue(object : Callback<List<Occurrences>> {
                        override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                            if (response.isSuccessful) {

                                occurrences = response.body()!!

                                for (occurrence in occurrences) {
                                    position = LatLng(occurrence.latitude, occurrence.longitude)

                                    if (calculateDistance(41.6873737, -8.8372531, occurrence.latitude, occurrence.longitude) < 65000) {
                                        mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                                    } else {
                                        mMap.addMarker(MarkerOptions().position(position).title(occurrence.titulo + " - " + occurrence.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))

                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Occurrences>>, t: Throwable) {
                            Toast.makeText(this@MapsActivity, getString(R.string.erromapa), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        }
    }

    fun filtro4(view: View) {

        Toast.makeText(applicationContext, R.string.filtro4, Toast.LENGTH_SHORT).show()

        mMap.clear()

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(41.6873737, -8.8372531)
        val zoomLevel = 8f;
        mMap.addMarker(MarkerOptions().position(sydney).title("Center of Viana do Castelo"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAcidentes()
        var position: LatLng
        var id: Int? = 0

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.login_p), Context.MODE_PRIVATE
        )

        if (sharedPref != null) {
            id = sharedPref.all[getString(R.string.id)] as Int?
        }

        call.enqueue(object : Callback<List<Occurrences>> {
            override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                if (response.isSuccessful) {

                    occurrences = response.body()!!

                    for (occurrence in occurrences) {
                        position = LatLng(occurrence.latitude, occurrence.longitude)

                        if (occurrence.user_id == id) {
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


    fun filtro5(view: View) {

                    Toast.makeText(applicationContext, R.string.filtro5, Toast.LENGTH_SHORT).show()

                    mMap.clear()

                    // Add a marker in Sydney and move the camera
                    val sydney = LatLng(41.6873737, -8.8372531)
                    val zoomLevel = 8f;
                    mMap.addMarker(MarkerOptions().position(sydney).title("Center of Viana do Castelo"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getBuracos()
                    var position: LatLng
                    var id: Int? = 0

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.login_p), Context.MODE_PRIVATE
                    )

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id)] as Int?
                    }

                    call.enqueue(object : Callback<List<Occurrences>> {
                        override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                            if (response.isSuccessful) {

                                occurrences = response.body()!!

                                for (occurrence in occurrences) {
                                    position = LatLng(occurrence.latitude, occurrence.longitude)

                                    if (occurrence.user_id == id) {
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

                fun filtro6(view: View) {

                    Toast.makeText(applicationContext, R.string.filtro6, Toast.LENGTH_SHORT).show()

                    mMap.clear()

                    // Add a marker in Sydney and move the camera
                    val sydney = LatLng(41.6873737, -8.8372531)
                    val zoomLevel = 8f;
                    mMap.addMarker(MarkerOptions().position(sydney).title("Center of Viana do Castelo"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getOutros()
                    var position: LatLng
                    var id: Int? = 0

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.login_p), Context.MODE_PRIVATE
                    )

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id)] as Int?
                    }

                    call.enqueue(object : Callback<List<Occurrences>> {
                        override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                            if (response.isSuccessful) {

                                occurrences = response.body()!!

                                for (occurrence in occurrences) {
                                    position = LatLng(occurrence.latitude, occurrence.longitude)

                                    Log.d("occurrences.user_id", occurrence.user_id.toString());
                                    Log.d("_id", id.toString());

                                    if (occurrence.user_id == id) {

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

                fun filtro7(view: View) {

                    Toast.makeText(applicationContext, R.string.filtro7, Toast.LENGTH_SHORT).show()

                    mMap.clear()

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

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id)] as Int?
                    }

                    call.enqueue(object : Callback<List<Occurrences>> {
                        override fun onResponse(call: Call<List<Occurrences>>, response: Response<List<Occurrences>>) {

                            if (response.isSuccessful) {

                                occurrences = response.body()!!

                                for (occurrence in occurrences) {
                                    position = LatLng(occurrence.latitude, occurrence.longitude)

                                    if (occurrence.user_id == id) {
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

                fun logout(view: View) {

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.login_p), Context.MODE_PRIVATE
                    )
                    with(sharedPref.edit()) {
                        putBoolean(getString(R.string.login_shared), false)
                        putString(getString(R.string.nome), "")
                        putInt(getString(R.string.id), 0)
                        commit()
                    }

                    val intent = Intent(this, MainActivity::class.java)

                    startActivity(intent)
                }

}
