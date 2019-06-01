package com.sac.gleamwash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*

import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    var auth: FirebaseAuth? = null
    var verificationCode: String? = null
    var phoneNumber: String? = null
    var otp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        startFirebaseLogin();
        btn_generate_otp.setOnClickListener({
            phoneNumber = et_phone_number.text.toString()
            mCallback?.let {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber!!,      // Phone number to verify
                        60,               // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        this,             // Activity (for callback binding)
                        it
                )
            }
        })

        btn_sign_in.setOnClickListener({
            verificationCode?.let {
                otp = et_otp.getText().toString();
                var credential = PhoneAuthProvider.getCredential(it, otp!!)
                signInWithPhone(credential);
            }

        })


    }

    fun signInWithPhone(credential: PhoneAuthCredential) {
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            Toast.makeText(this@LoginActivity, "Success OTP", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this@LoginActivity, "Incorrect OTP", Toast.LENGTH_SHORT).show();

                        }
                    }

                })

    }

    fun startFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(this@LoginActivity, "verification fialed", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                Toast.makeText(this@LoginActivity, "verification completed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)
                verificationCode = p0;
                Toast.makeText(this@LoginActivity, "Code sent", Toast.LENGTH_SHORT).show();
            }

        }
    }


}