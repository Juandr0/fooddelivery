package fragment.restaurant

import adapters.restaurantMenuAdapters.RestaurantMenuEditRecyclerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.OrderItem
import classes.User
import com.example.fooddeliveryproject.R
import com.example.fooddeliveryproject.db
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var restaurantMenuView: TextView
    lateinit var addNewDishButton: Button
    lateinit var  refreshImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()

        refreshImageButton.setOnClickListener {
            refreshFragment()
        }

        addNewDishButton.setOnClickListener {
            setCurrentFragmentToRestaurantEditMenu()
        }

        val currentUser = fragment.user.auth.currentUser
        val docRef = db.collection("users").document(currentUser!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                val user = document.toObject<User>()
                restaurantMenuView.text = "${user!!.name} " + getString(R.string.menu)

                FirebaseFirestore.getInstance().collection("restaurants").document("${user.menuId}").collection("menu")
                    .get()
                    .addOnSuccessListener { documents ->
                        for(document in documents){
                            val orderItem = documents.toObjects(OrderItem::class.java)
                            //Code for recyclerView
                            var recyclerView = view?.findViewById<RecyclerView>(R.id.restaurantEditMenuRecyclerView)
                            //What type of layout the list will have. This makes it a linear list
                            recyclerView?.layoutManager = LinearLayoutManager(context)
                            // Created an adapter from our adapter-class and sent in the list of restaurants
                            val adapter = RestaurantMenuEditRecyclerAdapter(this, orderItem)
                            //Connect our adapter to our recyclerView
                            recyclerView?.adapter = adapter
                            //End of recyclerView

                            adapter.setOnItemClickListener(object : RestaurantMenuEditRecyclerAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    //toast to check if clicking works
                                    Toast.makeText(context,
                                        "you clicked on item no. $position",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    when (position) {
                                        0 -> {
//
                                        }
                                        1 -> {

                                        }
                                        2 -> {
                                        }
                                    }
                                }

                            }) // End of click handler

                        }
                    }
            }




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_menu, container, false)
        restaurantMenuView = v.findViewById(R.id.restaurantMenuView)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNewDishButton = view.findViewById(R.id.addNewDishButton)
        refreshImageButton = view.findViewById(R.id.refreshImageButton)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setCurrentFragmentToRestaurantEditMenu(){
        val restaurantEditMenuFragment = RestaurantEditMenuFragment()
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.restaurantInterfaceContainer, restaurantEditMenuFragment)
        transaction.commit()
    }

    private fun refreshFragment(){
        val menuFragment = MenuFragment()
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.restaurantInterfaceContainer, menuFragment)
        transaction.commit()
    }

}