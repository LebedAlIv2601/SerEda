package com.disgust.sereda.utils.firebase

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.MainActivity
import com.disgust.sereda.utils.firebase.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class FirebaseAuthHelper {
    private val auth = Firebase.auth
    private val database = FirebaseDatabase.getInstance()
    private val usersDatabaseReference: DatabaseReference = database.reference.child("users")

    private var storedVerificationId: String? = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private fun createCallbacks(
        onVerificationFailed: ((FirebaseException) -> Unit)? = null,
        onCodeSent: ((String, PhoneAuthProvider.ForceResendingToken) -> Unit)? = null,
        onVerificationCompleted: ((credential: PhoneAuthCredential) -> Unit)? = null
    ) =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                onVerificationCompleted?.invoke(credential)
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed?.invoke(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
                onCodeSent?.invoke(verificationId, token)
            }
        }

    fun isAuth() = auth.currentUser != null

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    fun getCode(
        phone: String,
        onVerificationFailed: ((FirebaseException) -> Unit)? = null,
        onCodeSent: ((String, PhoneAuthProvider.ForceResendingToken) -> Unit)? = null,
        onVerificationCompleted: ((credential: PhoneAuthCredential) -> Unit)? = null
    ) {
        val mainActivity = MainActivity.getInstance()
        if (mainActivity != null) {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(30L, TimeUnit.SECONDS)
                .setActivity(mainActivity)
                .setCallbacks(
                    createCallbacks(
                        onVerificationFailed,
                        onCodeSent,
                        onVerificationCompleted
                    )
                )
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun verifyCode(code: String, completeListener: (Task<AuthResult>) -> Unit) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential, completeListener)
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        completeListener: (Task<AuthResult>) -> Unit = {}
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                usersDatabaseReference.child(auth.currentUser?.uid ?: "").get()
                    .addOnCompleteListener {
                        if (!it.result.exists()) {
                            authUser(auth.currentUser)
                        }
                    }
                completeListener.invoke(task)
            }
    }

    private fun authUser(user: FirebaseUser?) {
        if (user != null) {
            usersDatabaseReference.child(user.uid)
                .setValue(User(user.uid, user.phoneNumber ?: "0"))
        }
    }
}