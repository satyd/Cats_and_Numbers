package com.levp.catsandmath.presentation.common.di

import com.levp.catsandmath.CatsMathApp
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface ApplicationComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setApp(app: CatsMathApp): Builder

        fun build(): ApplicationComponent
    }
}