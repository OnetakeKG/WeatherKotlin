package net.nov.weatherkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import net.nov.weatherkotlin.ui.MainFragment
import net.nov.weatherkotlin.ui.threads.ThreadsFragment
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ThreadsFragment.newInstance())
            .commitNow()
        return true
    }
}