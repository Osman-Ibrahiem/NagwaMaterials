package com.domain.core.viewstate

class Success() : BaseVS() {

    var code = 200
    var message: String? = ""

    constructor(message: String?) : this() {
        this.message = message
    }

    constructor(message: String?, code: Int) : this() {
        this.message = message
        this.code = code
    }

    constructor(message: String?, code: Int, type: Int) : this() {
        this.message = message
        this.code = code
        this.type = type
    }

    companion object {
        fun get(message: String? = "", code: Int = 200) = Success(message, code)
        fun getWithType(message: String? = "", code: Int = 200, type: Int) =
            Success(message, code, type)
    }
}