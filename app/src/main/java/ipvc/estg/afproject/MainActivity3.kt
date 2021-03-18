package ipvc.estg.afproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.afproject.entities.Nota
import ipvc.estg.afproject.viewModel.NotasViewModel
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {

    private lateinit var notaViewModel: NotasViewModel
    private var notaId: Long = 0L
    private var isEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main3)

        notaViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

        if (intent.hasExtra("nota_id")) {
            notaId = intent.getLongExtra("nota_id", 0L)

            notaViewModel.get(notaId).observe(this, Observer {

                val viewId = findViewById<TextView>(R.id.nota_id)
                val viewTitulo = findViewById<EditText>(R.id.notas_titulo)
                val viewDescricao = findViewById<EditText>(R.id.notas_descricao)

                if (it != null) {

                    viewId.text = it.id.toString()
                    viewTitulo.setText(it.titulo)
                    viewDescricao.setText(it.descricao)

                }
            })
            isEdit = true
        }

        val btnSave = btnSave
        btnSave.setOnClickListener { view ->
            val id = 0L
            val titulo = notas_titulo.text.toString()
            val descricao = notas_descricao.text.toString()

            if (titulo.isBlank() or titulo.isEmpty()) {
                Snackbar.make(view, "Empty data is not allowed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            if (descricao.isBlank() or descricao.isEmpty()) {
                Snackbar.make(view, "Empty data is not allowed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            else {
                val item = Nota(id = id, titulo = titulo, descricao = descricao)
                notaViewModel.insert(item)
                finish()
            }
        }

        val btnUpdate = btnUpdate
        btnUpdate.setOnClickListener { view ->
            val id = nota_id.text.toString().toLong()
            val titulo = notas_titulo.text.toString()
            val descricao = notas_descricao.text.toString()

            if (titulo.isBlank() or titulo.isEmpty()) {
                Snackbar.make(view, "Empty data is not allowed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            if (descricao.isBlank() or descricao.isEmpty()) {
                Snackbar.make(view, "Empty data is not allowed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            else {
                val item = Nota(id = id, titulo = titulo, descricao = descricao)
                notaViewModel.update(item)
                finish()
            }
        }

        val btnDelete = btnDelete
        btnDelete.setOnClickListener {
            val id = nota_id.text.toString().toLong()
            val titulo = notas_titulo.text.toString()
            val descricao = notas_descricao.text.toString()

            val item = Nota(id = id, titulo = titulo, descricao = descricao)
            notaViewModel.delete(item)
            finish()
        }

        if (isEdit) {
            btnSave.visibility = View.GONE
        } else {
            /* No reason to Update or Delete a new Record yet to be saved */
            btnUpdate.visibility = View.GONE
            btnDelete.visibility = View.GONE
        }
    }

    fun voltaraddnote(view: View) {
        val intent = Intent(this, MainActivity2::class.java)
        val tit = findViewById<EditText>(R.id.notas_titulo)
        val desc = findViewById<EditText>(R.id.notas_descricao)
        tit.setText("")
        desc.setText("")
        startActivity(intent)
    }

}
