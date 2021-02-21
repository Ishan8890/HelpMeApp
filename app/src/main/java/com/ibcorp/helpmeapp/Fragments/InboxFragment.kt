package com.ibcorp.helpmeapp.Fragments

import com.ibcorp.helpmeapp.model.source.User
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.ibcorp.helpmeapp.Adapters.CommonReadAdapter
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.PrefManager
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentInboxBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InboxFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InboxFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentInboxBinding
    private var prefManager: PrefManager? = null

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false)
        val view: View = binding.getRoot()
        //adding a layoutmanager
        binding.recyclerViewInbox.layoutManager = LinearLayoutManager(activity)
        prefManager = PrefManager(requireContext())
        readDataFromFirestore()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InboxFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InboxFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun readDataFromFirestore(){
        var mFirestore = FirebaseFirestore.getInstance()
        binding.progressBarDialogInbox.visibility = View.VISIBLE
        var email =  prefManager!!.emailId
        mFirestore.collection("QuerySolution").get()
            .addOnSuccessListener {
                var selectedUserList = ArrayList<User>()
                var userList:List<User> = it.toObjects(User::class.java)
                if(!userList.isNullOrEmpty()){
                    binding.progressBarDialogInbox.visibility = View.GONE
                    binding.recyclerViewInbox.visibility = View.VISIBLE
                    Utils.captureUserTypeEvents("com.ibcorp.helpmeapp.Model.source.User Inbox",userList.size.toString(),requireContext())
                    for(userData in userList){
                        if(userData.emailId.equals(email)){
                            var selectedUser = User()
                            selectedUser.url = userData.url
                            selectedUser.topic = userData.topic
                            selectedUser.description = userData.description
                            selectedUser.isAnswered = userData.isAnswered
                            selectedUser.isAttached = userData.isAttached
                            selectedUserList.add(selectedUser)
                        }
                    }
                    val adapter = CommonReadAdapter(selectedUserList, requireContext(),false,requireActivity())
                    binding.recyclerViewInbox.adapter = adapter
                }else{
                    Toast.makeText(
                        activity,
                        "No such document!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}