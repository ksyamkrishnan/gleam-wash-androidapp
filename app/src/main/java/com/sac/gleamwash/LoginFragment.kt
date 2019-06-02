package com.sac.gleamwash

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit


class LoginFragment : Fragment() {

    var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    var auth: FirebaseAuth? = null
    var verificationCode: String? = null
    var phoneNumber: String? = null
    var otp: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startFirebaseLogin();
        btn_generate_otp.setOnClickListener({
            phoneNumber = et_phone_number.text.toString()
            activity?.let { nonNullActivity ->
                mCallback?.let {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber!!,      // Phone number to verify
                        60,               // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        nonNullActivity,             // Activity (for callback binding)
                        it
                    )
                }
            }})


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
                        (activity as HomeActivity).loadFragment(HomeFragment())
                    } else {
                        Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show();

                    }
                }

            })

    }

    fun startFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(context, "verification fialed", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                Toast.makeText(context, "verification completed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)
                verificationCode = p0;
                Toast.makeText(context, "Code sent", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
