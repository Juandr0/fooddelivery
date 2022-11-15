package fragment.user

import adapters.SearchBarRecyclerAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Restaurant
import com.example.fooddeliveryproject.R
import com.example.fooddeliveryproject.db


class SearchFragment : Fragment() {

lateinit var restaurantNamesList : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getRestaurantNames()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    private fun initializeRecyclerView(view : View){
        var recyclerView = view.findViewById<RecyclerView>(R.id.search_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = SearchBarRecyclerAdapter(this, restaurantNamesList)
        recyclerView.adapter = adapter
    }



    private fun getRestaurantNames(){
        var restaurantList = mutableListOf<String>()
        val docRef = db.collection("restaurants")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    restaurantList.add(document["name"].toString())
                }
                restaurantNamesList = restaurantList
            initializeRecyclerView(requireView())
        }

    }

}