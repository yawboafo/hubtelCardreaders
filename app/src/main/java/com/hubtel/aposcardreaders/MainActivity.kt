package com.hubtel.aposcardreaders

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hubtel.cardreaders.CardDelegates.CardPaymentProcessDelegate

class MainActivity : AppCompatActivity(),CardPaymentProcessDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
