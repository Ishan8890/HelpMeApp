package com.ibcorp.helpmeapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.model.UserDetail
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.PrefManager
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentImproveUsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImproveUsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImproveUsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentImproveUsBinding
    private lateinit var database: DatabaseReference
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_improve_us, container, false)
        val view: View = binding.getRoot()
        init(view)
        return view
    }

    fun init(view:View){
        database = Firebase.database.reference
        prefManager = PrefManager(requireContext())
        var username = prefManager!!.userName
        var email =  prefManager!!.emailId
        var query = binding.etQuery.text
        var token = prefManager!!.firebaseToken
        var ratingCount =0.0f
        binding.rateus.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingCount = rating
        }
        binding.btSubmit.setOnClickListener {
            Utils.hideKeyboard(view,requireActivity())
            if(!username.isNullOrBlank()&&!email.isNullOrBlank()&&!query.isNullOrBlank()){
                var userDetail = UserDetail()
                userDetail.userRating = ratingCount.toString()
                userDetail.username = username
                userDetail.email = email
                userDetail.query = query.toString()
                userDetail.token = token
                Utils.writeNewUser(userDetail,database,view,"Thanks for your feedback.","Improve us",true)
                binding.etQuery.text.clear()
            }else{
                CustomToast.snackbar("Field is Empty",view)
            }
        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImproveUsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImproveUsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}