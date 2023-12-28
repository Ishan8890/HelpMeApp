package com.ibcorp.helpmeapp.Fragments

import android.app.Activity
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.ibcorp.helpmeapp.Adapters.ExpertAdapter
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentUserBinding
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.model.ExpertResponse
import com.ibcorp.helpmeapp.model.Utils

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentUserBinding
    private var TAG:String = "TAG"
    private var resultData:String = ""
    var mediaControls: MediaController? = null

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        val view: View = binding.getRoot()
        //adding a layoutmanager
        binding.recyclerViewExpert.layoutManager = LinearLayoutManager(activity)
        binding.progressBar.visibility = View.VISIBLE
        if(CustomToast.isInternetAvailable(requireContext())){
            binding.noInternet.visibility= View.GONE
            binding.recyclerViewExpert.visibility = View.VISIBLE
            init()
        }else{
            binding.noInternet.visibility= View.VISIBLE
            binding.recyclerViewExpert.visibility = View.VISIBLE
        }
        return view
    }
    fun init() {
        getRemoteConfigData(requireActivity())
    }

    fun getRemoteConfigData(activity: Activity){
        var mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    resultData = mFirebaseRemoteConfig.getString("key_expert")
                    binding.progressBar.visibility = View.GONE
//                    Log.d(Utils.TAG, "Config params updated: ${Utils.resultData} Fetch and activate succeeded")
                    var gson = Gson()
                    var expertResponse = gson.fromJson(resultData, ExpertResponse::class.java)
                    var adapter = ExpertAdapter(
                        expertResponse.expertArray,
                        requireContext(),
                        false,
                        requireActivity()
                    )
                    binding.recyclerViewExpert.adapter = adapter
                    binding.expertHeader.text = expertResponse.expertHeader
                    if(!Utils.isUpdate){
                        if(expertResponse.updateFlash){
                            Utils.isUpdate =  true
                            showDialog(expertResponse.update_message)
                        }
                    }

                    if(!expertResponse.videoUrl.isNullOrBlank()){
//                        binding.videoView.visibility = View.VISIBLE
//                        initPlayer()
//                        binding.videoView.setVideoURI( Uri.parse("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"));
//                        binding.videoView.start();
                      /*  try {
                            val link = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"
                            val mediaController = MediaController(requireContext())
                            mediaController.setAnchorView(binding.videoView)
                            val video = Uri.parse(link)
                            binding.videoView.setMediaController(mediaController)
                            binding.videoView.setVideoURI(video)
                            binding.videoView.requestFocus()
                            binding.videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener {
                                binding.videoView.start()
                            })
                            binding.videoView.setOnCompletionListener {
                                binding.videoView.resume()
                            }
                        } catch (e: Exception) {
                            // TODO: handle exception
                            Toast.makeText(requireContext(), "Error connecting", Toast.LENGTH_SHORT).show()
                        }*/



                       /* if (mediaControls == null) {
                            // creating an object of media controller class
                            mediaControls = MediaController(requireContext())

                            // set the anchor view for the video view
                            mediaControls!!.setAnchorView(binding.videoView)
                        }

                        // set the media controller for video view
                        binding.videoView!!.setMediaController(mediaControls)

                        // set the absolute path of the video file which is going to be played
                        binding.videoView!!.setVideoURI(
                            Uri.parse(expertResponse.videoUrl))

                        binding.videoView!!.requestFocus()

                        // starting the video
                        binding.videoView!!.start()

                        // display a toast message
                        // after the video is completed
                        binding.videoView!!.setOnCompletionListener {
                            Toast.makeText(requireActivity(), "Video completed",
                                Toast.LENGTH_LONG).show()
                        }

                        // display a toast message if any
                        // error occurs while playing the video
                        binding.videoView!!.setOnErrorListener { mp, what, extra ->
                            Toast.makeText(requireActivity(), "An Error Occured " +
                                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
                            false
                        }
*/
                    }else{
                        binding.videoView.visibility = View.GONE
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    Log.d("TAG", "Fetch Failed")
                }
            }
    }
    private fun initPlayer() {
        var videoUri:Uri = getURI("https://images.all-free-download.com/footage_preview/webm/christmas_tree_7.webm")
        if (mediaControls == null) {
            // creating an object of media controller class
            mediaControls = MediaController(requireContext())

            // set the anchor view for the video view
            mediaControls!!.setAnchorView(binding.videoView)
        }

        // set the media controller for video view
        binding.videoView!!.setMediaController(mediaControls)
        binding.videoView.setVideoURI(videoUri)
        binding.videoView!!.requestFocus()
        binding.videoView.start()
    }

    private fun showDialog(message: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.update_flash)
        val body = dialog.findViewById(R.id.cancel) as ImageView
        val msg = dialog.findViewById(R.id.update_message) as TextView
        msg.text = message
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        val height: Int = metrics.heightPixels
        body.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.getWindow()!!.setLayout((6 * width)/7, (4 * height)/5)
    }

    private fun getURI(videoname: String): Uri{
        if (URLUtil.isValidUrl(videoname)) {
            //  an external URL
            return Uri.parse(videoname);
        } else { //  a raw resource
            return Uri.parse(
                "android.resource://" +
                        "/raw/" + videoname
            );
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}