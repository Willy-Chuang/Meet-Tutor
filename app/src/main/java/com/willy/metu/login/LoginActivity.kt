package com.willy.metu.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.willy.metu.R
import com.willy.metu.data.User
import com.willy.metu.ext.getVmFactory
import com.willy.metu.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        google_sign_in_button.setOnClickListener {
            //First step
            googleLogin()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.token_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        viewModel.firebaseUser.observe(this, Observer {
            it?.let {
                moveMainPage(it)
            }
        })

    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_LOGIN_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                //Second step
                viewModel.loginAuth(account)
            }
        }
    }


    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            val currentUser = User(image = user.photoUrl.toString(),
                    email = user.email.toString(),
                    name = user.displayName.toString())

            UserManager.user = currentUser
            viewModel.postUser(currentUser)

            startActivity(Intent(this, SplashActivity::class.java))
            overridePendingTransition(0, android.R.anim.fade_out)
            finish()
        }
    }

}