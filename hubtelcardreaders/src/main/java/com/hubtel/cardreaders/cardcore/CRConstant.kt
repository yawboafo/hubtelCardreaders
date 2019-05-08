package com.hubtel.cardreaders.cardcore

import io.mpos.transactionprovider.TransactionProcess
import io.mpos.transactionprovider.TransactionProcessDetails
import io.mpos.transactions.Transaction

class CPConstant {

    companion object {

        var transactionProcessDetails: TransactionProcessDetails?=null
        var paymentProcess: TransactionProcess? = null
        var transaction: Transaction? =null
        val UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    }

}