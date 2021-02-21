package com.ibcorp.helpmeapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentAllNewsBinding
import com.ibcorp.helpmeapp.presentation.adapter.NewsArticleAdapter
import com.ibcorp.helpmeapp.presentation.di.Injector
import com.ibcorp.helpmeapp.presentation.news.NewsViewModel
import com.ibcorp.helpmeapp.presentation.news.NewsViewModelFactory
import javax.inject.Inject

class AllNewsFragment : Fragment() {
   @Inject
   lateinit var factory: NewsViewModelFactory
   private lateinit var newsViewModel: NewsViewModel
   private lateinit var adapter: NewsArticleAdapter

   override fun onCreateView(
   inflater: LayoutInflater, container: ViewGroup?,
   savedInstanceState: Bundle?
   ): View? {
      val binding: FragmentAllNewsBinding = DataBindingUtil.inflate(
              inflater, R.layout.fragment_all_news, container, false)
      val view: View = binding.getRoot()
      (requireActivity().application as Injector).createNewsSubComponent().inject(this)

      newsViewModel= ViewModelProvider(this,factory)
              .get(NewsViewModel::class.java)
      initRecyclerView(binding)
      return view
   }

   private fun initRecyclerView(binding:FragmentAllNewsBinding){
      binding.articleRecyclerView.layoutManager = LinearLayoutManager(activity)
      adapter = NewsArticleAdapter()
      binding.articleRecyclerView.adapter = adapter
      displayNewsSource(binding)
   }

   private fun displayNewsSource(binding:FragmentAllNewsBinding){
      binding.articleProgressBar.visibility = View.VISIBLE
      val responseLiveData = newsViewModel.getHeadlines()
      responseLiveData.observe(viewLifecycleOwner, Observer {
         if(it!=null){
            adapter.setList(it)
            adapter.notifyDataSetChanged()
            binding.articleProgressBar.visibility = View.GONE
         }else{
            binding.articleProgressBar.visibility = View.GONE
            Toast.makeText(activity,"No data available", Toast.LENGTH_LONG).show()
         }
      })
   }
}