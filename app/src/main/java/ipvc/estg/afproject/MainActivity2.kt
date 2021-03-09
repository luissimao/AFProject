package ipvc.estg.afproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.afproject.adapter.LineAdapter
import ipvc.estg.afproject.dataclasses.Place
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var myList: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        myList = ArrayList<Place>()

        for(i in 0 until 10) {
            myList.add(Place("teste $1"))
        }
        recycler_view.adapter = LineAdapter(myList)
        recycler_view.layoutManager = LinearLayoutManager(this)

    }

    fun insert(view: View) {
        myList.add(0, Place("Description XXX"))
        recycler.view.adapter?.notifyDataSetChanged()
    }

    fun voltarmenuprincipal(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

}