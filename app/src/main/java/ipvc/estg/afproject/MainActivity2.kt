package ipvc.estg.afproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.afproject.MainActivity3.Companion.EXTRA_REPLY_NOTE
import ipvc.estg.afproject.adapters.NoteAdapter
import ipvc.estg.afproject.entities.Note
import ipvc.estg.afproject.viewModel.NoteViewModel

class MainActivity2 : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NoteAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it)}
        })

        val fab = findViewById<Button>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val pnote = data?.getStringExtra(EXTRA_REPLY_NOTE)

            if (pnote != null) {
                val note = Note(note = pnote)
                noteViewModel.insert(note)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }



    fun voltarmenuprincipal(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

    fun addnote(view: View) {
        val intent = Intent(this, MainActivity3::class.java)
        startActivity(intent)
    }

}