package org.moviedb.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import org.moviedb.R
import org.moviedb.databinding.ActivityMainBinding
import org.moviedb.ui.base.BaseActivity
import org.moviedb.utils.setBlackStatusBar
import org.moviedb.utils.setWhiteStatusBar

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val navController by lazy { findNavController(R.id.nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity()
    }

    private fun setupActivity() {
        val toolbar = binding.toolbar

        setSupportActionBar(toolbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detail_fragment -> setBlackStatusBar()
                else -> setWhiteStatusBar()
            }
        }

        setupActionBarWithNavController(navController)
    }

    private fun openSettings() = navController.navigate(R.id.action_to_settings)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
