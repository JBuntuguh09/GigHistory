package com.lonewolf.pasco.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lonewolf.pasco.R
import com.lonewolf.pasco.adaptors.RecyclerViewDashboard
import com.lonewolf.pasco.adaptors.RecyclerViewTest
import com.lonewolf.pasco.databinding.FragmentTestBinding
import com.lonewolf.pasco.resources.Storage
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Test.newInstance] factory method to
 * create an instance of this fragment.
 */
class Test(s: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentTestBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private  var arrayList = ArrayList<HashMap<String, String>>()
    private var type = s

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_test, container, false)
        binding = FragmentTestBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference

        getButtons()
        getData()
        return view
    }

    private fun getButtons() {
        //val listMonitor = ListMonitor(arrayList, setData())

    }

    fun getData(){
        if(type=="Objectives"){
            databaseReference.child("Scores").child("Objectives").child(storage.uSERID!!).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    arrayList.clear()
                    binding.progressBar.visibility = View.GONE
                    for(father in p0.children){
                        val hashMap = HashMap<String, String>()
                        //hashMap["name"] = father.child("name").value.toString()
                        hashMap["score"] = father.child("score").value.toString()
                        hashMap["userId"] = father.child("userId").value.toString()
                        hashMap["date"] = father.child("date").value.toString()
                        hashMap["time"] = father.child("time").value.toString()
                        hashMap["name"] = father.child("name").value.toString()

                        arrayList.add(hashMap)
                    }
                    if(arrayList.size>0){
                        val recyclerViewDashboard = RecyclerViewTest(requireContext(), arrayList)
                        val linearLayoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.layoutManager = linearLayoutManager
                        binding.recyclerView.itemAnimator = DefaultItemAnimator()
                        binding.recyclerView.adapter = recyclerViewDashboard
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }else{
            databaseReference.child("Scores").child("Quiz").child(storage.uSERID!!).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    arrayList.clear()
                    binding.progressBar.visibility = View.GONE
                    for(father in p0.children){
                        val hashMap = HashMap<String, String>()
                        //hashMap["name"] = father.child("name").value.toString()
                        hashMap["score"] = father.child("score").value.toString()
                        hashMap["userId"] = father.child("userId").value.toString()
                        hashMap["date"] = father.child("date").value.toString()
                        hashMap["time"] = father.child("time").value.toString()
                        hashMap["name"] = father.child("name").value.toString()

                        arrayList.add(hashMap)
                    }
                    if(arrayList.size>0){
                        val recyclerViewDashboard = RecyclerViewTest(requireContext(), arrayList)
                        val linearLayoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.layoutManager = linearLayoutManager
                        binding.recyclerView.itemAnimator = DefaultItemAnimator()
                        binding.recyclerView.adapter = recyclerViewDashboard
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }
    }

    private fun setData(){
        println("Stuff")
    }

//    class ListMonitor(
//        initialValue: ArrayList<HashMap<String, String>>,
//        private val onChange: Unit
//    ) {
//        private var list: ArrayList<HashMap<String, String>> by Delegates.observable(initialValue) { _, oldValue, newValue ->
//            if (oldValue != newValue) {
//                // Perform the action by invoking the provided function
//                onChange
//            }
//        }
//
//        fun setList(newList: ArrayList<HashMap<String, String>>) {
//            list = newList
//        }
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Test.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Test("Objectives").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}