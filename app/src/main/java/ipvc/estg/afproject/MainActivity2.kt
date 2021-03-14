package ipvc.estg.afproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.afproject.adapters.NotaAdapter
import ipvc.estg.afproject.viewModel.NotasViewModel
import kotlinx.android.synthetic.main.activity_main2.*

private const val TAG = "MainActivity2"

class MainActivity2 : AppCompatActivity() {

    private lateinit var notaViewModel: NotasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

        fab_add.setOnClickListener { _ ->
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        val recyclerView = notas_list
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        notaViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

        notaViewModel.allItems.observe(this, Observer { items ->
            items?.let { adapter.setItems(it) }
        })
    }

    fun voltarlista(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}