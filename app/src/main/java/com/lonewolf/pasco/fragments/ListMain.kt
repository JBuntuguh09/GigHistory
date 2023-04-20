package com.lonewolf.pasco.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lonewolf.pasco.resources.Storage
import com.google.firebase.database.*
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.adaptors.RecyclerViewList
import com.lonewolf.pasco.database.*
import com.lonewolf.pasco.resources.ShortCut_To

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListMain : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var progressBar : ProgressBar
    private lateinit var topicsViewModel: TopicsViewModel
    private lateinit var notesViewModel: NotesViewModel
    private var liveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var search : EditText
    private  var arrayList = ArrayList<HashMap<String, String>>()
    var shouldInterceptBackPress = true

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
        val view = inflater.inflate(R.layout.fragment_list_main, container, false)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        recyclerView = view.findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(requireContext())
        progressBar = (activity as MainBase).mainBaseBinding.progressBar
        search = view.findViewById(R.id.edtSearch)


        topicsViewModel = ViewModelProvider(requireActivity()).get(TopicsViewModel::class.java)
        notesViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(NotesViewModel::class.java)

        getListOffline(ShortCut_To.getTopics(storage.selectedCategory!!))

       // showUI()
        getButton()
        backPressed()
        return view
    }

    private fun getButton() {
        search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                liveData.postValue(p0.toString())
            }

        })

        liveData.observe(viewLifecycleOwner){
            getListOffline(ShortCut_To.getTopics(storage.selectedCategory!!))
        }
    }

    private fun getListOffline(data : MutableList<String>){
        recyclerView.removeAllViews()
        val arrayList : ArrayList<HashMap<String, String>> = ArrayList()
        for(a in 0 until  data.size){
            val lash = HashMap<String, String>()
            val hash = data[a]
            if(hash.lowercase().contains(search.text.toString().lowercase())) {
                lash.put("val", hash)
                arrayList.add(lash)
            }
        }

        progressBar.visibility = View.GONE
       // if(arrayList.size>0){

            val recyclerViewList = RecyclerViewList(requireContext(), arrayList, 0)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = recyclerViewList
       // }
    }

    private fun insertOffline(arrayList:ArrayList<HashMap<String, String>>){
        for(a in 0 until arrayList.size){
            val hash = arrayList.get(a)
            val notex = Notes(0, hash.get("Topic")!!, hash.get("Sub_Topic")!!,
                hash.get("Content")!!, hash.get("Priority")!!.toInt(),hash.get("Date_Created")!!)
            notesViewModel.insert(notex)

        }
    }

    override fun onResume() {
        if(storage.selectedCategory.equals("Section B")){
            //Theory Q & A
            (activity as MainBase).mainBaseBinding.txtTopic.text = "Theory Q & A"
        }else{
            (activity as MainBase).mainBaseBinding.txtTopic.text = storage.selectedCategory
        }

        super.onResume()
    }

    private fun showBottom() {
        val dialogue = Dialog(requireContext())
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogue.setContentView(R.layout.layout_options)




        dialogue.show()
        dialogue.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogue.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogue.window!!.attributes.windowAnimations = R.style.bottomAnim
        dialogue.window!!.setGravity(Gravity.BOTTOM)
    }

    private fun backPressed(){
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainBase).bMenu = 4
                println("vvvvvv")
                    isEnabled = false
                    activity?.onBackPressed()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListMain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}