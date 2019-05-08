package com.hubtel.cardreaders.cardModels

data class CPdetails(var schema: String,
                     var cardNumber: String,
                     var tid: String,
                     var authorization: String,
                     var mid: String,
                     var transactionID: String) {

   /** private var schema: String? = null
    private var card: String? = null
    private var tid: String? = null
    private var authorization: String? = null
    private var mid: String? = null
    private var transID: String? = null**/
}