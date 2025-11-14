package com.abdulmateen.pos_offline

import android.app.Application
import com.abdulmateen.pos_offline.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}