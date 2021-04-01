package ipvc.estg.afproject

import android.content.Intent
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

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

}