package com.domain.core

data class RemoteException(
    val code: Int,
    val errorMessage: String?,
) : Exception()

