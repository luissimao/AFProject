package ipvc.estg.afproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    fun voltarmenuprincipal(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

}