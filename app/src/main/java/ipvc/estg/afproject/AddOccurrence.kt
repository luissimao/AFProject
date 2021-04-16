package ipvc.estg.afproject

import android.R.attr.data
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity


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
        val toast = Toast.makeText(applicationContext, "Hello", Toast.LENGTH_SHORT)
        toast.show()
    }


}