package com.ibcorp.helpmeapp.ui.gallery

import com.ibcorp.helpmeapp.model.source.User
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.ibcorp.helpmeapp.Adapters.CommonReadAdapter
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentGalleryBinding


class DomainFragment : Fragment() {

    private lateinit var galleryViewModel: DomainViewModel
    var TAG:String?=null
    lateinit var db:FirebaseFirestore
    lateinit var binding:FragmentGalleryBinding
    var selectedDomain:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProvider(this).get(DomainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        val view: View = binding.getRoot()


        //adding a layoutmanager
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        if(CustomToast.isInternetAvailable(requireContext())){
            binding.noInternet.visibility= View.GONE
            binding.parent.visibility = View.VISIBLE
            init()
        }else{
            binding.noInternet.visibility= View.VISIBLE
            binding.parent.visibility = View.GONE
        }

        binding.button3.setOnClickListener(){
//            readDataFromFirestore()
     /*       db.collection("users")
                .get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                    override
                    fun onComplete(task: Task<QuerySnapshot?>) {
                        val list: ArrayList<String> = ArrayList()
                        if (task.isSuccessful()) {
                            for (document in task.getResult()!!) {
                                Log.d(TAG, document.id + " => " + document.data)
                                if(document.data.keys.equals("Topic")){
                                    
                                }
                                var value = document.data.values.toString()
                                list.add(value)
                            }
//                            var adapter =   CommonReadAdapter(list,activity)
//                            binding.sourceRecyclerView.adapter = adapter
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException())
                        }
                    }
                })*/
        }
        return view
    }

    private fun readDataFromFirestore(domainType: String,view: View){
        var mFirestore = FirebaseFirestore.getInstance()
        binding.progressBar.visibility = View.VISIBLE
//        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        mFirestore.collection(domainType).get()
            .addOnSuccessListener {
                var userList:List<User> = it.toObjects(User::class.java)
                if(!userList.isNullOrEmpty()){
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    Utils.captureUserLectureEvents("Lectures",userList.size.toString(),domainType,requireContext())
                    val adapter = CommonReadAdapter(userList, requireContext(),false,requireActivity())
                    binding.recyclerView.adapter = adapter
                }else{
                    Toast.makeText(
                        activity,
                        "No such document!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


/*        mFirestore.collection("users").document(domainType)
            .get()
            .addOnSuccessListener { document ->
                try {
                    if (document != null) {
                        var userInfo = document.toObject(Model::class.java) ?: Toast.makeText(
                            activity,
                            "No such document!",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(TAG, userInfo.toString())
                        var userList = (userInfo as Model).users
//                        var userData = (com.ibcorp.helpmeapp.Model.source.User)userInfo.users
//                        initListView()
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        val adapter = context?.let { CommonReadAdapter(userList, it) }
                        //now adding the adapter to recyclerview
                        binding.recyclerView.adapter = adapter
//                        Toast.makeText(
//                            activity,
//                            "DocumentSnapshot read successfully!",
//                            Toast.LENGTH_LONG
//                        ).show()
                    } else {
                        Toast.makeText(activity, "Something wrong", Toast.LENGTH_LONG).show()
                    }
                }catch (ex: Exception){
                    Log.e(TAG, ex.message)
                }
            }.addOnFailureListener { e -> Log.e(TAG, "Error writing document", e)
            }*/
    }

    fun init(){

        val domainList = resources.getStringArray(R.array.domain)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, domainList
        )
        adapter.setDropDownViewResource(R.layout.spinner_textview_align)
        binding.domainName.adapter = adapter
        binding.domainName.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedDomain = domainList[position]
                readDataFromFirestore(selectedDomain,view)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

//        var progressBar = CustomToast.getProgressBar(requireContext())
//        binding.parent.gravity = Gravity.CENTER
//        binding.parent.addView(progressBar)
//        progressBar.visibility = View.VISIBLE


//        db = FirebaseFirestore.getInstance()

//        binding.webView.webViewClient = WebViewClient()
//        binding.webView.settings.setSupportZoom(true)
//        binding.webView.settings.javaScriptEnabled = true
////        val url = FileUtils.getPdfUrl()
//        binding.webView.loadUrl("https://firebasestorage.googleapis.com/v0/b/helpme-419c7.appspot.com/o/Ishan_Android_CV.pdf?alt=media&token=585656ea-0bf9-49fb-9512-bf74203e0045")
    }


}