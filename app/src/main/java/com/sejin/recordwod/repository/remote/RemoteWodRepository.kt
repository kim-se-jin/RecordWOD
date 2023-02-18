package com.sejin.recordwod.repository.remote

import com.sejin.recordwod.repository.Either
import com.sejin.recordwod.repository.WodRepository
import com.sejin.recordwod.repository.remote.api.WodService
import javax.inject.Inject
import javax.inject.Singleton

// wodrepository 인터페이스를 상속받아 필요한 코드 구현
@Singleton
class RemoteWodRepository @Inject internal constructor(
    private val wodService : WodService
): WodRepository{

    override suspend fun updateWOD(
    wodId: String,
    date: String,
    wod: String
    ): Either<String> {
         return Either.error(("Someting is wrong"))
    }
}