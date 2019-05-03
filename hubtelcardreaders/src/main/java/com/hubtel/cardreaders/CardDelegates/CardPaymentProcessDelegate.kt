package com.hubtel.cardreaders.CardDelegates

import com.hubtel.cardreaders.CardCore.CPStatus
import io.mpos.paymentdetails.ApplicationInformation
import io.mpos.paymentdetails.DccInformation
import io.mpos.transactionprovider.TransactionProcess
import io.mpos.transactionprovider.TransactionProcessDetails
import io.mpos.transactions.Transaction

interface  CardPaymentProcessDelegate {

    fun cannotStartCardTransaction(error: String)
    fun onStatusChanged(status: String)
    fun onDccSelectionRequired(process: TransactionProcess?, transaction: Transaction?, dccInformation: DccInformation?)
    fun onRegistered(process: TransactionProcess?, transaction: Transaction?)
    fun onCustomerSignatureRequired(process: TransactionProcess?, transaction: Transaction?)
    fun onCustomerVerificationRequired(process: TransactionProcess?, transaction: Transaction?)
    fun onApplicationSelectionRequired(
         process: TransactionProcess?,
         transaction: Transaction?,
          appList: MutableList<ApplicationInformation>?)
    fun onCardPaymentCompleted(status: CPStatus)

}