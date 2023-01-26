package com.sejin.recordwod.view.state

data class LoginState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val username: String = "",
    val password: String = "",
    val isValidUsername: Boolean? = null,
    val isValidPassword: Boolean? = null
) : State

