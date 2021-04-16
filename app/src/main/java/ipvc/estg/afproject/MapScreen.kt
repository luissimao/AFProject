package ipvc.estg.afproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MapScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_screen)
    }

    fun addOccurrence(view: View) {

        val intent = Intent(this, AddOccurrence::class.java)

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