package com.hubtel.aposcardreaders

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hubtel.cardreaders.CardDelegates.CardConnectionDelegates
import com.hubtel.cardreaders.CardDelegates.CardPaymentProcessDelegate
import com.hubtel.cardreaders.CardModels.CPdetails
import com.hubtel.cardreaders.cardcore.CPEnvironment
import com.hubtel.cardreaders.cardcore.CPStatus
import com.hubtel.cardreaders.cardcore.CardConnectionManager
import com.hubtel.cardreaders.cardcore.CardManager

class MainActivity : AppCompatActivity() , CardConnectionDelegates,CardPaymentProcessDelegate {


    lateinit var cardPM : CardManager
    lateinit var txt : TextView
    lateinit var button: Button
    lateinit var button2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt = findViewById(R.id.txtview) as TextView
        button = findViewById(R.id.button) as Button
      var cardManager = CardConnectionManager()

        //cardManager.findDevices()
        txt.setText(cardManager.getConnectedCardReader())

         cardPM = CardManager(CPEnvironment.LIVE,"edaad8b6-71ef-470b-b4e7-6be80858c744","A2IfLgu7G9GqwMxjCqrAKAk4ubPmKfWR","ab501271-80a0-4462-b29b-1f35fb6a61aa",this@MainActivity,this@MainActivity)



        button.setOnClickListener {

            cardPM.startCardTransaction()

        }

        button2.setOnClickListener {


        }

    }

    /*
    * {
    "ResponseCode": "0000",
    "Data": {
        "Description": "Transaction pending. Expect callback request for final state.",
        "TransactionId": "d0e15c67c4554020a4f9ac547d11995d",
        "ClientReference": "card_pay",
        "TransactionSession": {
            "MerchantId": "edaad8b6-71ef-470b-b4e7-6be80858c744",
            "MerchantSecretKey": "A2IfLgu7G9GqwMxjCqrAKAk4ubPmKfWR",
            "TransactionSessionId": "d3e57316-e07e-41bb-bd8f-b7bd4396d91b"
        },
        "Meta": null,
        "CardDetail": null
    }
}
    * */


    override fun cannotStartCardTransaction(message: String?) {
    }

    override fun onStatusChanged(status: String) {



        txt.setText(status)


    }

    override fun onDccSelectionRequired() {
    }

    override fun onRegistered() {
    }

    override fun onCustomerSignatureRequired() {

    }

    override fun onCustomerVerificationRequired() {
    }

    override fun onApplicationSelectionRequired() {
    }

    override fun onCardPaymentCompleted(status: CPStatus, cardDetails: CPdetails) {


        var txt = findViewById(R.id.txtview) as TextView

        txt.setText("Payment completion status : " +status.name + "  \n\n   Card Details :   " + cardDetails.toString())


    }

    override fun doesNotsupportBT() {

    }
}
