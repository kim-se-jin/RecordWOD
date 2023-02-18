package com.sejin.recordwod.repository

import javax.inject.Singleton

@Singleton
interface UserRepository {

    suspend fun addUser(username: String, password : String)
    suspend fun UserKakaoLogin()
}