package com.jspstudio.community.network

object ResponseCode {
    const val SUCCESS = 200
    const val ERROR = 429
    const val BINDING_ERROR = 400
    const val BINDING_ERROR_TITLE = 4001
    const val BINDING_ERROR_CONTENT = 4002
    const val BINDING_ERROR_DATE = 4003
    const val NOT_FOUND = 404
    const val DUPLICATE_ERROR = 409
    const val SERVER_ERROR = 500
    const val FAIL = 505
    const val NETWORK_ERROR = 600
}