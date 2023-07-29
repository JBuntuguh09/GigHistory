package com.lonewolf.pasco.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.adaptors.RecyclerViewDictionary
import com.lonewolf.pasco.database.DictionaryViewModel
import com.lonewolf.pasco.database.Question
import com.lonewolf.pasco.databinding.FragmentDictionaryBinding
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Dictionary.newInstance] factory method to
 * create an instance of this fragment.
 */
class Dictionary : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private  var arrayList = ArrayList<HashMap<String, String>>()
    private val liveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var dictionaryViewModel: DictionaryViewModel
    private lateinit var binding: FragmentDictionaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dictionary, container, false)
        storage = Storage(requireContext())
        binding = FragmentDictionaryBinding.bind(view)
        databaseReference = FirebaseDatabase.getInstance().reference
        linearLayoutManager = LinearLayoutManager(requireContext())
        liveData.observe(viewLifecycleOwner){
            searchFor("")
        }

        dictionaryViewModel= ViewModelProvider(this, defaultViewModelProviderFactory).get(DictionaryViewModel::class.java)

        dictionaryViewModel.liveData.observe(viewLifecycleOwner){data->

            if (data.isNotEmpty()) {
                getOffLine(data)
            } else {

                getWord()
            }


        }
        binding.progressBar.visibility = View.VISIBLE
        //getWord()
        getButtons()
        return view
    }

    private fun getOffLine(data: List<com.lonewolf.pasco.database.Dictionary>) {
        arrayList.clear()
        binding.recyclerView.removeAllViews()
        for(a in 0 until data.size){
            val hasd = data[a]
            val hash = HashMap<String, String>()
            hash["Word"] = hasd.Word
            hash["Meaning"] = hasd.Meaning
            hash["Pic"] = hasd.Pic

            arrayList.add(hash)
        }
        binding.progressBar.visibility = GONE
        if(arrayList.size>0){
            val recyclerViewDictionary = RecyclerViewDictionary(requireActivity(), arrayList)
            binding.recyclerView.layoutManager = linearLayoutManager
            binding.recyclerView.itemAnimator = DefaultItemAnimator()
            binding.recyclerView.adapter = recyclerViewDictionary
        }
    }

    private fun getButtons() {

        binding.edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                storage.randVal= p0.toString()
                liveData.postValue(p0.toString())
            }

        })
        binding.edtSearch.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                        searchFor(binding.edtSearch.text.toString())
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun getWord(){
        databaseReference.child("Dictionary").child("History").orderByChild("Word")
            .addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        arrayList.clear()
                        binding.recyclerView.removeAllViews()
                        for(father in p0.children){
                            val hash = HashMap<String, String>()
                            hash["Word"] = father.child("Word").value.toString()
                            hash["Meaning"] = father.child("Meaning").value.toString()
                            hash["Pic"] = father.child("Pic_Path").value.toString()

                            arrayList.add(hash)
                        }
                        binding.progressBar.visibility = GONE
                        if(arrayList.size>0){
                            deleteAll()
                            insertData()
                            val recyclerViewDictionary = RecyclerViewDictionary(requireActivity(), arrayList)
                            binding.recyclerView.layoutManager = linearLayoutManager
                            binding.recyclerView.itemAnimator = DefaultItemAnimator()
                            binding.recyclerView.adapter = recyclerViewDictionary
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {


                }
            }
        )
    }

    private fun searchFor(word:String){
        val arrayListAlt = ArrayList<HashMap<String, String>>()
        binding.recyclerView.removeAllViewsInLayout()

        for (a in 0 until arrayList.size){
            val hashMap = arrayList[a]
            if(hashMap["Word"]!!.lowercase(Locale.getDefault()).contains(binding.edtSearch.text.toString()
                    .lowercase(Locale.getDefault()))){
                arrayListAlt.add(hashMap)
            }


        }

        if(arrayListAlt.size>0){
            val recyclerViewDictionary = RecyclerViewDictionary(requireActivity(), arrayListAlt)
            binding.recyclerView.layoutManager = linearLayoutManager
            binding.recyclerView.itemAnimator = DefaultItemAnimator()
            binding.recyclerView.adapter = recyclerViewDictionary
        }
    }

    private fun insertData(){
        for(a in 0 until arrayList.size){
            val hash = arrayList.get(a)
            val dic = com.lonewolf.pasco.database.Dictionary(0, hash.get("Word")!!,hash.get("Meaning")!!,hash.get("Pic")!!,
            ShortCut_To.currentDatewithTime)
            dictionaryViewModel.insert(dic)

        }
    }
    private fun deleteAll(){
        dictionaryViewModel.deleteAll()
    }

    override fun onResume() {
        (activity as MainBase).mainBaseBinding.txtTopic.setText(getString(R.string.history_dictionary))
        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Dictionary.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                Dictionary().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}