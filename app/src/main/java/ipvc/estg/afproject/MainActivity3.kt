package ipvc.estg.afproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity3 : AppCompatActivity() {

    private lateinit var noteText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        noteText = findViewById(R.id.note)

        val button = findViewById<Button>(R.id.submitbuttonnote)
        button.setOnClickListener {
            val replyIntent = Intent()
            if  (TextUtils.isEmpty(noteText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_NOTE, noteText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

    }

    companion object {
        const val EXTRA_REPLY_NOTE = "ipvc.estg.android.note"
    }

    fun voltarnotas(view: View) {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent);
    }

}