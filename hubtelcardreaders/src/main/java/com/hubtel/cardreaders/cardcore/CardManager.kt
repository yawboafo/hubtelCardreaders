package com.hubtel.cardreaders.cardcore

import android.app.Activity
import com.hubtel.cardreaders.CardDelegates.CardPaymentProcessDelegate
import com.hubtel.cardreaders.CardModels.CPdetails
import io.mpos.Mpos
import io.mpos.accessories.AccessoryFamily
import io.mpos.accessories.parameters.AccessoryParameters
import io.mpos.paymentdetails.ApplicationInformation
import io.mpos.paymentdetails.DccInformation
import io.mpos.provider.ProviderMode
import io.mpos.transactionprovider.*
import io.mpos.transactions.Transaction
import io.mpos.transactions.receipts.ReceiptLineItemKey


class CardManager(var _environment: ProviderMode,
                  var _merchantIdentifier: String = "",
                  var _merchantSecreteKey: String = "",
                  var _sessionID: String = "",
                  var activity: Activity,
                  var cardProcessDelegate: CardPaymentProcessDelegate){




    var transactionProcessDetails: TransactionProcessDetails?=null
    var paymentProcess: TransactionProcess? = null
    var transaction: Transaction? =null
    var cardDetails: CPdetails?=null

    fun  startCardTransaction(){



        if (activity == null) {
            cardProcessDelegate.cannotStartCardTransaction("Context cannot be empty")
            return
        }

        if(_merchantIdentifier.isBlank()){
            cardProcessDelegate.cannotStartCardTransaction("Merchant identifier is required")
            return
        }


        if(_merchantSecreteKey.isBlank()){
            cardProcessDelegate.cannotStartCardTransaction("Merchant SecretKey  is required")
            return
        }

        if(_sessionID.isBlank()){
            cardProcessDelegate.cannotStartCardTransaction("Session ID  is required")
            return
        }






          val  provider = Mpos.createTransactionProvider(activity,_environment,_merchantIdentifier,_merchantSecreteKey)
          val accessoryParameters = AccessoryParameters.Builder(AccessoryFamily.MIURA_MPI)
              .bluetooth().build()

           paymentProcess = provider.startTransaction(_sessionID,accessoryParameters, object : TransactionProcessWithRegistrationListener {
              override fun onStatusChanged(p0: TransactionProcess?, p1: Transaction?, p2: TransactionProcessDetails?) {
                  paymentProcess = p0
                  cardProcessDelegate.onStatusChanged(p2?.information?.get(0).toString() +  "  "  +  p2?.information?.get(1).toString())
              }

              override fun onDccSelectionRequired(p0: TransactionProcess?, p1: Transaction?, p2: DccInformation?) {
                  paymentProcess = p0
                  transaction = p1
                  cardProcessDelegate.onDccSelectionRequired()
              }

              override fun onRegistered(p0: TransactionProcess?, p1: Transaction?) {
                  paymentProcess = p0
                  transaction = p1
                  cardProcessDelegate.onRegistered()
              }

              override fun onCustomerSignatureRequired(p0: TransactionProcess?, p1: Transaction?) {
                  paymentProcess = p0
                  transaction = p1
                  cardProcessDelegate.onCustomerSignatureRequired()


              }

              override fun onCustomerVerificationRequired(p0: TransactionProcess?, p1: Transaction?) {
                  p0?.continueWithCustomerIdentityVerified(false)
                  paymentProcess = p0
                  transaction = p1
                  cardProcessDelegate.onCustomerVerificationRequired()
              }

              override fun onApplicationSelectionRequired(p0: TransactionProcess?, p1: Transaction?, p2: MutableList<ApplicationInformation>?) {


                  paymentProcess = p0
                  transaction = p1
                  p0?.continueWithSelectedApplication(p2?.get(0))
                  cardProcessDelegate.onApplicationSelectionRequired()

              }

              override fun onCompleted(p0: TransactionProcess?, p1: Transaction?, p2: TransactionProcessDetails?) {

                  paymentProcess = p0
                  transaction = p1
                  transactionProcessDetails = p2

                  when(p2?.state ){

                      TransactionProcessDetailsState.ABORTED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Aborted)
                      TransactionProcessDetailsState.APPROVED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Approved)
                      TransactionProcessDetailsState.FAILED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Failed)
                      TransactionProcessDetailsState.INCONCLUSIVE -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Inconclusive)
                      TransactionProcessDetailsState.DECLINED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Declined)
                  }


                  if(p1 !=null){
                      var merchantID =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_MERCHANT_IDENTIFIER).value
                      var schema =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.PAYMENT_DETAILS_SCHEME_OR_LABEL).value
                      var cardNo =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.PAYMENT_DETAILS_MASKED_ACCOUNT).value
                      var transID =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_TRANSACTION_IDENTIFIER).value
                      var terminalID =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_TERMINAL_ID).value
                      var auth =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_AUTHORIZATION_CODE).value
                      cardDetails =  CPdetails(schema,cardNo,terminalID,auth,merchantID,transID)
                  }


                  //cardProcessDelegate.onCompleted(p0,p1,p2)

              }


          })


        }
    fun  requestAbort(){


        if (paymentProcess != null){

             paymentProcess?.requestAbort()
        }
    }
    fun  cardDetails() : CPdetails?{



        return cardDetails
    }
    fun  continuePaymentwithSignature(bytes: ByteArray){


        if(paymentProcess != null)
        paymentProcess?.continueWithCustomerSignature(bytes, true)

    }



}