package com.ibcorp.helpmeapp.presentation.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ibcorp.helpmeapp.DetailActivity
import com.ibcorp.helpmeapp.model.source.Source
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.ItemLoadingBinding
import com.ibcorp.helpmeapp.databinding.NewsSourceListItemBinding


class NewsSourceAdapter(var context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val sourceList = ArrayList<Source>()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    lateinit var itemLoadingBinding: ItemLoadingBinding
    lateinit var binding: NewsSourceListItemBinding
    private var isLoadingAdded = false
    fun setList(artists: List<Source>) {
        sourceList.addAll(artists)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType === VIEW_TYPE_ITEM) {
            binding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.news_source_list_item,
                    parent,
                    false
            )
            MyViewHolder(binding)
        } else {
            itemLoadingBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.item_loading,
                    parent,
                    false
            )
            LoadingViewHolder(itemLoadingBinding)
        }
    }


    override fun getItemCount(): Int {
        return if (sourceList == null) 0 else sourceList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (sourceList.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MyViewHolder) {
            if(sourceList!=null)
            holder.bind(sourceList[position])
            binding.listItemLayout.setOnClickListener {
                Log.d("", "DATA::::" + sourceList[position])
                var articleId: String = sourceList[position].id
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ID", articleId)
                context.startActivity(intent)
            }
        } else if (holder is LoadingViewHolder) {
            itemLoadingBinding.progressBar.visibility = View.VISIBLE
        }


    }


    private fun add(list: Source) {
        sourceList.add(list);
        notifyItemInserted(sourceList.size - 1);
    }

    public fun addAll(pList: List<Source>) {
        for (list in pList) {
            add(list)
        }
    }

    private fun remove(list: Source) {
        var position = sourceList.indexOf(list);
        if (position > -1) {
            sourceList.removeAt(position);
            notifyItemRemoved(position);
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0)!!)
        }
    }

    public fun isEmpty(): Boolean {
        return getItemCount() == 0;
    }


    public fun addLoadingFooter() {
        isLoadingAdded = true;
//        add(new GetAccountTransactions . AcountTransactions ());
    }

    public fun removeLoadingFooter() {
        isLoadingAdded = false;

        if (sourceList != null && sourceList.size > 0) {
            var position: Int = sourceList!!.size - 1;
            var item = getItem(position);
            if (item != null) {
                sourceList.removeAt(position);
                notifyItemRemoved(position);
            }
        }

    }

    public fun clearData(){
        sourceList.clear()
    }

    private fun getItem(position: Int): Source? {
        return sourceList.get(position)
    }


    class MyViewHolder(val binding: NewsSourceListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(source: Source) {
            binding.titleTextView.text = source.name
            binding.descriptionTextView.text = source.description

        }
    }

    private class LoadingViewHolder(val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }


    }
}
