package com.hubtel.cardreaders.cardcore

import android.content.Context
import com.hubtel.cardreaders.cardDelegates.CardPaymentProcessDelegate
import com.hubtel.cardreaders.cardModels.CPdetails
import io.mpos.Mpos
import io.mpos.accessories.AccessoryFamily
import io.mpos.accessories.parameters.AccessoryParameters
import io.mpos.paymentdetails.ApplicationInformation
import io.mpos.paymentdetails.DccInformation
import io.mpos.provider.ProviderMode
import io.mpos.transactionprovider.*
import io.mpos.transactions.Transaction
import io.mpos.transactions.receipts.ReceiptLineItemKey


class CardManager(var _environment: CPEnvironment,
                  var _merchantIdentifier: String ,
                  var _merchantSecreteKey: String,
                  var _sessionID: String ,
                  var activity: Context,
                  var cardProcessDelegate: CardPaymentProcessDelegate){






    var transactionProcessDetails: TransactionProcessDetails?=null
    var paymentProcess: TransactionProcess? = null
    var transaction: Transaction? =null
    var cardDetails: CPdetails


    init {
        cardDetails =  CPdetails("","","","","","")
    }

    fun getEnvironment(type: CPEnvironment) : ProviderMode {


        when(type){

            CPEnvironment.LIVE -> return  ProviderMode.LIVE
            CPEnvironment.TEST -> return  ProviderMode.TEST
            CPEnvironment.MOCK -> return  ProviderMode.MOCK
            null ->  return   ProviderMode.TEST
        }
    }

    fun  startCardTransaction(){



        if (activity == null) {
            cardProcessDelegate.onCardPaymentCompleted(CPStatus.ContextNull,cardDetails)
            return
        }

        if(_merchantIdentifier.isBlank()){
            cardProcessDelegate.onCardPaymentCompleted(CPStatus.MerchantIDNull,cardDetails)
            return
        }


        if(_merchantSecreteKey.isBlank()){
            cardProcessDelegate.onCardPaymentCompleted(CPStatus.MerchantSecretKeyNull,cardDetails)
            return
        }

        if(_sessionID.isBlank()){
            cardProcessDelegate.onCardPaymentCompleted(CPStatus.SessionIDNull,cardDetails)
            return
        }






          val  provider = Mpos.createTransactionProvider(activity,getEnvironment(_environment),_merchantIdentifier,_merchantSecreteKey)
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
                  paymentProcess = p0
                  transaction = p1
                  p0?.continueWithCustomerIdentityVerified(false)

                 // cardProcessDelegate.onCustomerVerificationRequired()
              }

              override fun onApplicationSelectionRequired(p0: TransactionProcess?, p1: Transaction?, p2: MutableList<ApplicationInformation>?) {


                  paymentProcess = p0
                  transaction = p1
                  p0?.continueWithSelectedApplication(p2?.get(0))
                 // cardProcessDelegate.onApplicationSelectionRequired()

              }

              override fun onCompleted(p0: TransactionProcess?, p1: Transaction?, p2: TransactionProcessDetails?) {

                  paymentProcess = p0
                  transaction = p1
                  transactionProcessDetails = p2


                  if(p1 !=null){
                      var merchantID =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_MERCHANT_IDENTIFIER).value
                      var schema =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.PAYMENT_DETAILS_SCHEME_OR_LABEL).value
                      var cardNo =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.PAYMENT_DETAILS_MASKED_ACCOUNT).value
                      var transID =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_TRANSACTION_IDENTIFIER).value
                      var terminalID =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_TERMINAL_ID).value
                      var auth =  p1.customerReceipt.getReceiptLineItem(ReceiptLineItemKey.CLEARING_DETAILS_AUTHORIZATION_CODE).value
                      cardDetails =  CPdetails(schema,cardNo,terminalID,auth,merchantID,transID)
                  }

                  when(p2?.state ){

                      TransactionProcessDetailsState.ABORTED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Aborted,cardDetails)
                      TransactionProcessDetailsState.APPROVED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Approved,cardDetails)
                      TransactionProcessDetailsState.FAILED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Failed,cardDetails)
                      TransactionProcessDetailsState.INCONCLUSIVE -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Inconclusive,cardDetails)
                      TransactionProcessDetailsState.DECLINED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.Declined,cardDetails)
                      TransactionProcessDetailsState.CREATED ->  cardProcessDelegate.onCardPaymentCompleted(CPStatus.CREATED,cardDetails)
                      TransactionProcessDetailsState.CONNECTING_TO_ACCESSORY -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.CONNECTING_TO_ACCESSORY,cardDetails)
                      TransactionProcessDetailsState.PREPARING -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.PREPARING,cardDetails)
                      TransactionProcessDetailsState.INITIALIZING_TRANSACTION -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.INITIALIZING_TRANSACTION,cardDetails)
                      TransactionProcessDetailsState.WAITING_FOR_CARD_PRESENTATION -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.WAITING_FOR_CARD_PRESENTATION,cardDetails)
                      TransactionProcessDetailsState.WAITING_FOR_CARD_DATA_INPUT -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.WAITING_FOR_CARD_DATA_INPUT,cardDetails)
                      TransactionProcessDetailsState.WAITING_FOR_ADDRESS_INPUT -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.WAITING_FOR_ADDRESS_INPUT,cardDetails)
                      TransactionProcessDetailsState.WAITING_FOR_VERIFICATION_DATA_CONFIRMATION -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.WAITING_FOR_VERIFICATION_DATA_CONFIRMATION,cardDetails)
                      TransactionProcessDetailsState.WAITING_FOR_CARD_REMOVAL ->  cardProcessDelegate.onCardPaymentCompleted(CPStatus.WAITING_FOR_CARD_REMOVAL,cardDetails)
                      TransactionProcessDetailsState.PROCESSING ->  cardProcessDelegate.onCardPaymentCompleted(CPStatus.PROCESSING,cardDetails)
                      TransactionProcessDetailsState.ACCEPTED -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.ACCEPTED,cardDetails)
                      TransactionProcessDetailsState.NOT_REFUNDABLE -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.NOT_REFUNDABLE,cardDetails)
                      TransactionProcessDetailsState.WAITING_FOR_SCAN -> cardProcessDelegate.onCardPaymentCompleted(CPStatus.WAITING_FOR_SCAN,cardDetails)
                      null ->cardProcessDelegate.onCardPaymentCompleted(CPStatus.NULL_STATE,cardDetails)
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
    private fun  cardDetails() : CPdetails?{



        return cardDetails
    }
    fun  continuePaymentwithSignature(bytes: ByteArray){


        if(paymentProcess != null)
        paymentProcess?.continueWithCustomerSignature(bytes, true)

    }



}