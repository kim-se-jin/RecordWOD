package com.sejin.recordwod.repository.remote.api

import com.sejin.recordwod.model.request.updateRequest
import com.sejin.recordwod.model.response.updateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface WodService {
    @PUT("/wod/{wodId}")
    suspend fun updateNote(
        @Path("wodId") wodId:String,
        @Body wodRequest : updateRequest
    ): Response<updateResponse>
}