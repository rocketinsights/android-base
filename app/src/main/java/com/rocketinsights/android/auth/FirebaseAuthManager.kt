package com.rocketinsights.android.auth

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.rocketinsights.android.R

class FirebaseAuthManager(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val authUI: AuthUI
) : AuthManager {

    /**
     * Launch sign-in/register flow with email or other accounts if specified among providers
     * and enabled as a sign-in method in Firebase console.
     */
    override fun launchSignInFlow() {
        // return if already signed in
        if (firebaseAuth.currentUser != null) return

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
            // This is where you can provide more ways for users to register and sign in.
        )

        // Create and launch the sign-in intent.
        context.startActivity(
            authUI.createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_rocketinsights_logo) // set logo
                .setTheme(R.style.Theme_AndroidBase_Main) // set theme
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                ) // set ToS and Privacy Policy URLs
                .build()
        )
    }

    override fun logout() {
        authUI.signOut(context)
    }
}
