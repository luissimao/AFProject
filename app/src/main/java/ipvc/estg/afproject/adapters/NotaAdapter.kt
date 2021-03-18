package ipvc.estg.afproject.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.afproject.MainActivity3
import ipvc.estg.afproject.R
import ipvc.estg.afproject.entities.Nota
import java.util.Collections.emptyList

private const val TAG = "NotaAdapter"

class NotaAdapter internal constructor(context: Context) :

        RecyclerView.Adapter<NotaAdapter.NotasViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var itemsList = emptyList<Nota>().toMutableList()

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Nota

            Log.d(TAG, "Setting onClickListener for item ${item.id}")

            val intent = Intent(v.context, MainActivity3::class.java).apply {
                putExtra("nota_id", item.id)
            }
            v.context.startActivity(intent)
        }
    }

    inner class NotasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemId: TextView = itemView.findViewById(R.id.notas_id)
        val descricaodanota: TextView = itemView.findViewById(R.id.notas_viewholder_titulo)
        val detalhesdanota: TextView = itemView.findViewById(R.id.notas_viewholder_descricao)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NotasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        val current = itemsList[position]

        holder.itemView.tag = current

        with(holder) {
            itemId.text = "Note "  + current.id.toString()
            descricaodanota.text = current.titulo
            detalhesdanota.text = current.descricao

            itemView.setOnClickListener(onClickListener)
        }
    }

    internal fun setItems(items: List<Nota>) {
        this.itemsList = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = itemsList.size
}