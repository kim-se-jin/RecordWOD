package com.sejin.recordwod

import javax.inject.Singleton

@Singleton
interface UserRepository {

    suspend fun addUser(username: String, password : String)

}