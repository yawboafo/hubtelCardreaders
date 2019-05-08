package com.hubtel.cardreaders.cardcore

enum class CPStatus {
    Failed,
    Declined,
    Aborted,
    Inconclusive,
    Approved,
    ContextNull,
    MerchantIDNull,
    MerchantSecretKeyNull,
    SessionIDNull,
    CREATED,
    CONNECTING_TO_ACCESSORY,
    PREPARING,
    INITIALIZING_TRANSACTION,
    WAITING_FOR_CARD_PRESENTATION,
    WAITING_FOR_CARD_DATA_INPUT,
    WAITING_FOR_ADDRESS_INPUT,
    WAITING_FOR_VERIFICATION_DATA_CONFIRMATION,
    WAITING_FOR_CARD_REMOVAL,
    PROCESSING,
    ACCEPTED,
    NOT_REFUNDABLE,
    INCONCLUSIVE,
    NULL_STATE,
    WAITING_FOR_SCAN;
}