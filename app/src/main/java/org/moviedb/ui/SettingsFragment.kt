package org.moviedb.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import org.moviedb.R
import java.util.*
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), HasAndroidInjector {

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        initLanguagePrefs()
    }

    private fun initLanguagePrefs() {
        val languagePreference = findPreference<ListPreference>(getString(R.string.pref_key_language))
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            updateLanguage(newValue as String)
            return@setOnPreferenceChangeListener true
        }
    }

    @Suppress("DEPRECATION")
    private fun updateLanguage(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration: Configuration = resources.configuration
        val displayMetrics = resources.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            configuration.setLocale(locale)
            requireContext().createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, displayMetrics)
        }
        requireActivity().recreate()
    }
}