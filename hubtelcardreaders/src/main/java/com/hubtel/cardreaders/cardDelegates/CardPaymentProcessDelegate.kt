package com.hubtel.cardreaders.cardDelegates

import com.hubtel.cardreaders.cardModels.CPdetails
import com.hubtel.cardreaders.cardcore.CPStatus


interface  CardPaymentProcessDelegate {



    fun onStatusChanged(status: String)
    fun onDccSelectionRequired()
    fun onRegistered()
    fun onCustomerSignatureRequired()
    fun onCustomerVerificationRequired()
    fun onApplicationSelectionRequired()
    fun onCardPaymentCompleted(status: CPStatus,cardDetails: CPdetails)

}