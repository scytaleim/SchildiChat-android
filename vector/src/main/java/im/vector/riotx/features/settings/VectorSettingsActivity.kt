/*
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.riotx.features.settings

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import im.vector.matrix.android.api.failure.GlobalError
import im.vector.matrix.android.api.session.Session
import im.vector.riotx.R
import im.vector.riotx.core.di.ScreenComponent
import im.vector.riotx.core.extensions.replaceFragment
import im.vector.riotx.core.platform.VectorBaseActivity
import im.vector.riotx.features.settings.devices.VectorSettingsDevicesFragment
import kotlinx.android.synthetic.main.activity_vector_settings.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Displays the client settings.
 */
class VectorSettingsActivity : VectorBaseActivity(),
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
        FragmentManager.OnBackStackChangedListener,
        VectorSettingsFragmentInteractionListener {

    override fun getLayoutRes() = R.layout.activity_vector_settings

    override fun getTitleRes() = R.string.title_activity_settings

    private var keyToHighlight: String? = null

    var ignoreInvalidTokenError = false

    @Inject lateinit var session: Session

    override fun injectWith(injector: ScreenComponent) {
        injector.inject(this)
    }

    override fun initUiAndData() {
        configureToolbar(settingsToolbar)

        if (isFirstCreation()) {
            // display the fragment
            when (intent.getIntExtra(EXTRA_DIRECT_ACCESS, EXTRA_DIRECT_ACCESS_ROOT)) {
                EXTRA_DIRECT_ACCESS_GENERAL                ->
                    replaceFragment(R.id.vector_settings_page, VectorSettingsGeneralFragment::class.java, null, FRAGMENT_TAG)
                EXTRA_DIRECT_ACCESS_ADVANCED_SETTINGS                ->
                    replaceFragment(R.id.vector_settings_page, VectorSettingsAdvancedSettingsFragment::class.java, null, FRAGMENT_TAG)
                EXTRA_DIRECT_ACCESS_SECURITY_PRIVACY                 ->
                    replaceFragment(R.id.vector_settings_page, VectorSettingsSecurityPrivacyFragment::class.java, null, FRAGMENT_TAG)
                EXTRA_DIRECT_ACCESS_SECURITY_PRIVACY_MANAGE_SESSIONS ->
                    replaceFragment(R.id.vector_settings_page,
                            VectorSettingsDevicesFragment::class.java,
                            null,
                            FRAGMENT_TAG)
                else                                                 ->
                    replaceFragment(R.id.vector_settings_page, VectorSettingsRootFragment::class.java, null, FRAGMENT_TAG)
            }
        }

        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onDestroy() {
        supportFragmentManager.removeOnBackStackChangedListener(this)
        super.onDestroy()
    }

    override fun onBackStackChanged() {
        if (0 == supportFragmentManager.backStackEntryCount) {
            supportActionBar?.title = getString(getTitleRes())
        }
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val oFragment = try {
            pref.fragment?.let {
                supportFragmentManager.fragmentFactory.instantiate(classLoader, it)
            }
        } catch (e: Throwable) {
            showSnackbar(getString(R.string.not_implemented))
            Timber.e(e)
            null
        }

        if (oFragment != null) {
            oFragment.setTargetFragment(caller, 0)
            // Replace the existing Fragment with the new Fragment
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.right_in, R.anim.fade_out, R.anim.fade_in, R.anim.right_out)
                    .replace(R.id.vector_settings_page, oFragment, pref.title.toString())
                    .addToBackStack(null)
                    .commit()
            return true
        }
        return false
    }

    override fun requestHighlightPreferenceKeyOnResume(key: String?) {
        keyToHighlight = key
    }

    override fun requestedKeyToHighlight(): String? {
        return keyToHighlight
    }

    override fun handleInvalidToken(globalError: GlobalError.InvalidToken) {
        if (ignoreInvalidTokenError) {
            Timber.w("Ignoring invalid token global error")
        } else {
            super.handleInvalidToken(globalError)
        }
    }

    companion object {
        fun getIntent(context: Context, directAccess: Int) = Intent(context, VectorSettingsActivity::class.java)
                .apply { putExtra(EXTRA_DIRECT_ACCESS, directAccess) }

        private const val EXTRA_DIRECT_ACCESS = "EXTRA_DIRECT_ACCESS"

        const val EXTRA_DIRECT_ACCESS_ROOT = 0
        const val EXTRA_DIRECT_ACCESS_ADVANCED_SETTINGS = 1
        const val EXTRA_DIRECT_ACCESS_SECURITY_PRIVACY = 2
        const val EXTRA_DIRECT_ACCESS_SECURITY_PRIVACY_MANAGE_SESSIONS = 3
        const val EXTRA_DIRECT_ACCESS_GENERAL = 4

        private const val FRAGMENT_TAG = "VectorSettingsPreferencesFragment"
    }
}
