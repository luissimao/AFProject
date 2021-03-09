package ipvc.estg.afproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun button4(view: View) {

        var edit1 = findViewById<EditText>(R.id.editTextTextPersonName2)

        val intent = Intent(this, MainActivity2::class.java).apply {
            putExtra("teste", edit1.text)
        }
        startActivity(intent)
    }

}