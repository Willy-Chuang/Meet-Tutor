package com.willy.metu.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.willy.metu.MainActivity
import com.willy.metu.R
import com.willy.metu.data.User
import com.willy.metu.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    var auth : FirebaseAuth? = null
    var googleSignInClient : GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        google_sign_in_button.setOnClickListener {
            //First step
            googleLogin()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("906661261750-eq9rl8502jk5v6at1ik754gfj7cm4t80.apps.googleusercontent.com")
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        //printHashKey()

    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }
    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }

    }
    fun googleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_LOGIN_CODE){
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess){
                var account = result.signInAccount
                //Second step
                firebaseAuthWithGoogle(account)
            }
        }
    }
    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                    task ->
                    if(task.isSuccessful){
                        //Login
                        moveMainPage(task.result?.user)
                    }else{
                        //Show the error message
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }
    }

    fun moveMainPage(user:FirebaseUser?){
        if(user != null){
            var currentUser = User(image = user.photoUrl.toString(),
                    email = user.email.toString(),
                    name = user.displayName.toString())


            UserManager.user = currentUser
            viewModel.postUser(currentUser)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

//    fun setupUser(user: User) {
//
//        _user.value = user
//        Logger.i("=============")
//        Logger.i("| setupUser |")
//        Logger.i("user=$user")
//        Logger.i("MainViewModel=${this}")
//        Logger.i("=============")
//    }
//
//    fun checkUser() {
//        if (user.value == null) {
//            UserManager.userToken?.let {
//                getUserProfile(it)
//            }
//        }
//    }
}