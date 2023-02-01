package com.sejin.recordwod.data.useData

import com.sejin.recordwod.data.database.UserInfo

class create(userId: String, name:String, BBweight:Boolean, DBweight:Boolean, KBweight:Boolean, userBox:String ){
    val newUser = UserInfo(userId, name, BBweight, DBweight, KBweight, userBox)
}