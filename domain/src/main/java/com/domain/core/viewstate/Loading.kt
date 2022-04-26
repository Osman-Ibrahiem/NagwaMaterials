package com.domain.core.viewstate

class Loading() : BaseVS() {

    constructor(type: Int) : this() {
        this.type = type
    }

    companion object {
        fun get() = Loading()
        fun getWithType(type: Int) = Loading(type)
    }
}