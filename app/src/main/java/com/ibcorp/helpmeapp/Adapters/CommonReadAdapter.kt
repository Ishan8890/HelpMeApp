package com.ibcorp.helpmeapp.Adapters

import User
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.RowCommonReadBinding

import android.widget.TextView
import com.ibcorp.helpmeapp.Model.CustomToast

/**
 * Created by Belal on 6/19/2017.
 */

class CommonReadAdapter(val userList: List<User>,val context: Context) : RecyclerView.Adapter<CommonReadAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonReadAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_common_read, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CommonReadAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position],context)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: User,context: Context) {
            val textViewName = itemView.findViewById(R.id.title_text_view) as TextView
            val textViewAddress  = itemView.findViewById(R.id.description_text_view) as TextView
            val listItem  = itemView.findViewById(R.id.list_item_layout) as LinearLayout
            textViewName.text = user.topic
            textViewAddress.text = user.description

            listItem.setOnClickListener {
                pdfReader(user.url,context,itemView )
            }
        }

        fun pdfReader(url:String,context: Context,view: View){
//            var pdfUrl = "https://firebasestorage.googleapis.com/v0/b/helpme-419c7.appspot.com/o/Ishan_Android_CV.pdf?alt=media&token=585656ea-0bf9-49fb-9512-bf74203e0045"
//            var pdfUrl = "https://firebasestorage.googleapis.com/v0/b/helpme-419c7.appspot.com/o/question.docx?alt=media&token=a8f8536e-cfac-4e35-852d-0f77ae05a738"
            var pdfUrl = "https://firebasestorage.googleapis.com/v0/b/helpme-419c7.appspot.com/o/HelpMeApp_Doc.pdf?alt=media&token=7d31880c-3a3e-4354-805c-6ebc3e836865"
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
    }


}