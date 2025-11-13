package com.abdulmateen.cmpskeleton

import android.app.Application
import com.abdulmateen.cmpskeleton.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}