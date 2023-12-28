package com.ibcorp.helpmeapp.presentation.di.news

import com.ibcorp.helpmeapp.DetailActivity
import com.ibcorp.helpmeapp.Fragments.AllNewsFragment
import com.ibcorp.helpmeapp.Fragments.NewsFragment
import com.ibcorp.helpmeapp.Fragments.QuizFragment
import com.ibcorp.helpmeapp.Fragments.SourceFragment
import dagger.Subcomponent

@NewsScope
@Subcomponent(modules = [NewsModule::class])
interface NewsSubComponent {
    fun inject(sourceFragment: SourceFragment)
    fun inject(allNewsFragment: AllNewsFragment)
    fun inject(detailActivity: DetailActivity)
    fun inject(quizFragment: QuizFragment)

    @Subcomponent.Factory
    interface Factory{
        fun create():NewsSubComponent
    }

}

