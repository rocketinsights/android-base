package com.rocketinsights.android.mappings

import com.rocketinsights.android.db.models.DbMessage
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.network.models.ApiMessage

fun ApiMessage.toMessage() = Message(
    text = text,
    timestamp = timestamp
)

fun ApiMessage.toDbMessage() = DbMessage(
    uuid = uuid,
    text = text,
    timestamp = timestamp
)

fun DbMessage.toMessage() = Message(
    id = id,
    text = text,
    timestamp = timestamp
)

fun List<ApiMessage>.toDbMessages() = map { it.toDbMessage() }

fun List<DbMessage>.toMessages() = map { it.toMessage() }