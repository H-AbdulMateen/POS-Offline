package com.abdulmateen.pos_offline.android

import android.app.Application
import com.abdulmateen.pos_offline.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
    companion object{
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}
