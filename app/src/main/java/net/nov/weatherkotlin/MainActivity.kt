package net.nov.weatherkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.nov.weatherkotlin.ui.MainFragment
import java.lang.reflect.Array.newInstance
import java.net.URLClassLoader.newInstance

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}