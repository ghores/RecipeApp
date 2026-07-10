package com.example.recipeapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

open class BaseActivity : AppCompatActivity() {
    //Calligraphy
    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val app = newBase.applicationContext as MyApp
            super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase, app.viewPump))
        } else {
            super.attachBaseContext(newBase)
        }
    }
}