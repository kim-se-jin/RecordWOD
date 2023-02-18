package com.sejin.recordwod.repository

import javax.inject.Singleton

// 여기서 데이터를 가져다 주는 역할
@Singleton
interface WodRepository {

    /**
     * updateWOD 유저가 작성한 와드를 서버로 업데이트
     * @param wodId : The WOD ID
     * @param date : 작성한 날짜
     * @param wod : 그 날 와드 내용, 기록 등등...
     */
    suspend fun updateWOD(
        wodId : String,
        date : String,
        wod : String
    ): Either<String>

}