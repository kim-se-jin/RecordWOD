package com.sejin.recordwod.model.response

data class updateResponse(
    override val status : State,
    override val message : String,
    val wodId : String?
) : BaseResponse
