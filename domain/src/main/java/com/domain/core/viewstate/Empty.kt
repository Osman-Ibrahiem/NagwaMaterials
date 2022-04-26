package com.domain.core.viewstate

class Empty() : BaseVS() {

    constructor(type: Int) : this() {
        this.type = type
    }

    companion object {
        fun get() = Empty()
        fun getWithType(type: Int) = Empty(type)
    }
}