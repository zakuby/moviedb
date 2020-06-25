package org.moviedb.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.moviedb.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        navigateToMovie()
    }

    private fun navigateToMovie() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}
