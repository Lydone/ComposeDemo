package com.belaventsev.composedemo

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.belaventsev.composedemo.main.MainFragment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with(supportFragmentManager) {
            if (fragments.isEmpty()) {
                beginTransaction()
                    .add(R.id.fragment_container_view, MainFragment(), null)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}