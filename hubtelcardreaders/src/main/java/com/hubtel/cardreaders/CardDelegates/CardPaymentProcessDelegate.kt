package com.hubtel.cardreaders.CardDelegates

import com.hubtel.cardreaders.CardModels.CPdetails
import com.hubtel.cardreaders.cardcore.CPStatus
import io.mpos.transactions.CardDetails


interface  CardPaymentProcessDelegate {


    fun cannotStartCardTransaction(message: String?)
    fun onStatusChanged(status: String)
    fun onDccSelectionRequired()
    fun onRegistered()
    fun onCustomerSignatureRequired()
    fun onCustomerVerificationRequired()
    fun onApplicationSelectionRequired()
    fun onCardPaymentCompleted(status: CPStatus,cardDetails: CPdetails)

}