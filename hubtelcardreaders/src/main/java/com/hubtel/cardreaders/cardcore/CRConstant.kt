package com.hubtel.cardreaders.cardcore

import io.mpos.transactionprovider.TransactionProcess
import io.mpos.transactionprovider.TransactionProcessDetails
import io.mpos.transactions.Transaction

class CPConstant {

    companion object {

        var transactionProcessDetails: TransactionProcessDetails?=null
        var paymentProcess: TransactionProcess? = null
        var transaction: Transaction? =null
    }

}