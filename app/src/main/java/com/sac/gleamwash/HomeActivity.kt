package com.sac.gleamwash

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity


/**
 * A login screen that offers login via email/password.
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadFragment(LoginFragment())
    }

    fun loadFragment(fragment: Fragment) {
        var ft : FragmentTransaction = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragment);
        ft.commit();
    }
}