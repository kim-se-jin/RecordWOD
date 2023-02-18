package com.sejin.recordwod.model

open class UserInfo {
    var userId: String = ""
    var name: String = "" // 유저 이름
    var BBweight: Boolean = true // 바벨무게 true 일 때 lb, false일 때 kg
    var DBweight: Boolean = true // 덤벨무게 true 일 때 lb, false일 때 kg
    var KBweight: Boolean = true // 케틀벨무게 true 일 때 lb, false일 때 kg
    var userBox : String = "" // 유저가 다니는 박스

    constructor(_userId:String, _name:String, _BBweight: Boolean, _DBweight:Boolean, _KBweight:Boolean, _userBox:String){
        userId = _userId
        name = _name
        BBweight = _BBweight
        DBweight = _DBweight
        KBweight = _KBweight
        userBox = _userBox
    }


    fun CreateUser(
        userId: String,
        name: String,
        BBweight: Boolean,
        DBweight: Boolean,
        KBweight: Boolean,
        userBox: String
    ) {
        val newUser = UserInfo(userId, name, BBweight, DBweight, KBweight, userBox)
//        WriteDataBase.child("users").child(userId).setValue(newUser)
    }

}

