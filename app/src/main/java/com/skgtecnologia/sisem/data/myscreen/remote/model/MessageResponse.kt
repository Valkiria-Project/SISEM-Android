package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.MessageModel

data class MessageResponse(
    val text: String
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.MESSAGE

    override fun mapToDomain(): BodyRowModel = MessageModel(
        text = text
    )
}
