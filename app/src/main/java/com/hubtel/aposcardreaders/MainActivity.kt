package com.hubtel.aposcardreaders

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hubtel.cardreaders.CardDelegates.CardConnectionDelegates
import com.hubtel.cardreaders.cardcore.CardConnectionManager

class MainActivity : AppCompatActivity() , CardConnectionDelegates {
    override fun doesNotsupportBT() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      var cardManager = CardConnectionManager()

        cardManager.findDevices()


    }
}
