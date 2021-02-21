package com.ibcorp.helpmeapp.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ibcorp.helpmeapp.Fragments.AllNewsFragment
import com.ibcorp.helpmeapp.Fragments.SourceFragment


@Suppress("DEPRECATION")
internal class MyAdapter(
   var context: Context,
   fm: FragmentManager,
   var totalTabs: Int
) :
FragmentPagerAdapter(fm) {
   override fun getItem(position: Int): Fragment {
      return when (position) {
         0 -> {
            SourceFragment()
         }
         1 -> {
            AllNewsFragment()
         }
         else -> getItem(position)
      }
   }
   override fun getCount(): Int {
      return totalTabs
   }
}