package com.ibcorp.helpmeapp.Adapters
import com.ibcorp.helpmeapp.model.source.User
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ibcorp.helpmeapp.R

import androidx.appcompat.app.AlertDialog
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.ui.home.Constant
import com.ibcorp.helpmeapp.ui.home.RefreshView

/**
 * Created by Belal on 6/19/2017.
 */

class CommonReadAdapter() : RecyclerView.Adapter<CommonReadAdapter.ViewHolder>() {

    lateinit var activity:Activity
    lateinit var userList:List<User>
    var isAdmin:Boolean = false
    lateinit var context:Context
    lateinit var refreshViewInterface:RefreshView

    constructor(userList: List<User>, context: Context, isAdmin:Boolean):this(){
        this.userList = userList
        this.context = context
        this.isAdmin = isAdmin
    }

    constructor(userList: List<User>, context: Context, isAdmin:Boolean, activity: Activity, refreshViewInterface:RefreshView):this(){
        this.activity = activity
        this.userList = userList
        this.context = context
        this.isAdmin = isAdmin
        this.refreshViewInterface = refreshViewInterface
    }

    constructor(userList: List<User>, context: Context, isAdmin:Boolean, activity: Activity):this(){
        this.activity = activity
        this.userList = userList
        this.context = context
        this.isAdmin = isAdmin
    }
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonReadAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_common_read, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CommonReadAdapter.ViewHolder, position: Int) {
        if(isAdmin){
            holder.bindItems(userList[position],context,isAdmin,activity!!,refreshViewInterface)
        }else{
            holder.bindItems(userList[position],context,isAdmin,activity!!)
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: User, context: Context, isAdmin:Boolean, activity: Activity, refreshViewInterface: RefreshView) {
            val textViewName = itemView.findViewById(R.id.title_text_view) as TextView
            val textViewAddress  = itemView.findViewById(R.id.description_text_view) as TextView
//            val userName  = itemView.findViewById(R.id.userNameAdmin) as TextView
            val userEmailID  = itemView.findViewById(R.id.emailIdAdmin) as TextView
            val listItem  = itemView.findViewById(R.id.list_item_layout) as LinearLayout
            val rightArrow  = itemView.findViewById(R.id.rightArrow) as ImageView
            val reply  = itemView.findViewById(R.id.reply) as ImageView
            val attachment  = itemView.findViewById(R.id.attachment) as ImageView
            if(isAdmin){
//                userName.visibility = View.VISIBLE
                userEmailID.visibility = View.VISIBLE
                reply.visibility = View.VISIBLE

//                userName.text = user.userName
                userEmailID.text = "Email ID : "+user.emailId
                textViewName.text = "Topic: "+user.topic
                textViewAddress.text = "Description: "+user.description
                rightArrow.visibility = View.GONE
                if(user.isAttached){
                    attachment.visibility = View.VISIBLE
                }else{
                    attachment.visibility = View.GONE
                }
            }else{
                reply.visibility = View.GONE
                attachment.visibility = View.GONE
                rightArrow.visibility = View.VISIBLE
//                userName.visibility = View.GONE
                userEmailID.visibility = View.GONE
                textViewName.text = user.topic
                textViewAddress.text = user.description
            }
            attachment.setOnClickListener {
                pdfReader(user.url,context,itemView )
            }
            reply.setOnClickListener {
                showCustomAlert(context,activity,user,refreshViewInterface)
            }

            listItem.setOnClickListener {
                if(!isAdmin){
                    pdfReader(user.url,context,itemView )
                }
            }
        }

        fun bindItems(user: User, context: Context, isAdmin:Boolean, activity: Activity) {
            val textViewName = itemView.findViewById(R.id.title_text_view) as TextView
            val textViewAddress  = itemView.findViewById(R.id.description_text_view) as TextView
//            val userName  = itemView.findViewById(R.id.userNameAdmin) as TextView
            val userEmailID  = itemView.findViewById(R.id.emailIdAdmin) as TextView
            val listItem  = itemView.findViewById(R.id.list_item_layout) as LinearLayout
            val rightArrow  = itemView.findViewById(R.id.rightArrow) as ImageView
            val reply  = itemView.findViewById(R.id.reply) as ImageView
            val attachment  = itemView.findViewById(R.id.attachment) as ImageView
            if(isAdmin){
//                userName.visibility = View.VISIBLE
                userEmailID.visibility = View.VISIBLE
                reply.visibility = View.VISIBLE

//                userName.text = user.userName
                userEmailID.text = "Email ID : "+user.emailId
                textViewName.text = "Topic: "+user.topic
                textViewAddress.text = "Description: "+user.description
                rightArrow.visibility = View.GONE
                if(user.isAttached){
                    attachment.visibility = View.VISIBLE
                }else{
                    attachment.visibility = View.GONE
                }
            }else{
                reply.visibility = View.GONE
                attachment.visibility = View.GONE
                rightArrow.visibility = View.VISIBLE
//                userName.visibility = View.GONE
                userEmailID.visibility = View.GONE
                textViewName.text = user.topic
                textViewAddress.text = user.description
            }
            attachment.setOnClickListener {
                pdfReader(user.url,context,itemView )
            }


            listItem.setOnClickListener {
                if(!isAdmin){
                    pdfReader(user.url,context,itemView )
                }
            }
        }

        fun pdfReader(url:String,context: Context,view: View){
//            var pdfUrl = "https://firebasestorage.googleapis.com/v0/b/helpme-419c7.appspot.com/o/Ishan_Android_CV.pdf?alt=media&token=585656ea-0bf9-49fb-9512-bf74203e0045"
//            var pdfUrl = "https://firebasestorage.googleapis.com/v0/b/helpme-419c7.appspot.com/o/question.docx?alt=media&token=a8f8536e-cfac-4e35-852d-0f77ae05a738"
//            var pdfUrl = "https://firebasestorage.googleapis.com/v0/b/helpme-419c7.appspot.com/o/HelpMeApp_Doc.pdf?alt=media&token=7d31880c-3a3e-4354-805c-6ebc3e836865"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(url), "application/pdf")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val newIntent: Intent = Intent.createChooser(intent, "Open File")
            try {
                context.startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {
                CustomToast.snackbar("Install the PDF reader on your device",view)
                // Instruct the user to install a PDF reader here, or something
            }
        }

        private fun showCustomAlert(context: Context, activity: Activity, user: User, refreshViewInterface: RefreshView) {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.fragment_slideshow, null)
            val customDialog = AlertDialog.Builder(context)
                .setView(mDialogView)
                .show()
            val btSend = mDialogView.findViewById<Button>(R.id.bt_send)
            val subject = mDialogView.findViewById<EditText>(R.id.et_subject)
            val message = mDialogView.findViewById<EditText>(R.id.et_message)
            val btAttachment = mDialogView.findViewById<Button>(R.id.bt_attachment)
            dialog_progress = mDialogView.findViewById(R.id.progress_bar_dialog)
            if(!user.topic.isNullOrBlank()){
                subject.setText(user.topic)
            }
            btSend.setOnClickListener {
                if(!subject.text.toString().isNullOrBlank()&& !message.text.toString().isNullOrBlank()&&!user.emailId.isNullOrBlank()){
                    var userData = User()
                    userData.topic = subject.text.toString()
                    userData.description =  message.text.toString()
                    userData.emailId = user.emailId
                    userData.isAnswered = true
                    userData.docId = user.docId
                    if(Constant.url.isNullOrBlank()){
                        userData.url =  " "
                        userData.isAttached = false
                    }else{
                        userData.url =  Constant.url
                        userData.isAttached = true
                    }
                    Constant.uploadData(userData,"QuerySolution")
                    refreshViewInterface.getRefreshData()
                    customDialog.dismiss()
                }else{
                    CustomToast.snackbar("Fields is Empty",message)
                }

            }
            btAttachment.setOnClickListener {
                Constant.getPDF(context,activity,true)
            }




        }




    }


companion object{
    lateinit var dialog_progress:ProgressBar
    fun hideProgressBar(){
        //show progress bar
        dialog_progress.visibility = View.GONE
    }
    fun showProgressBar(){
        dialog_progress.visibility = View.VISIBLE
    }
}

}