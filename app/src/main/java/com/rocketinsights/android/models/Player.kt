package com.rocketinsights.android.models

enum class Position(val stringValue: String) {
    FW("Forward"),
    MF("Midfielder"),
    DF("Defender"),
    GK("Goalkeeper"),
    NA("")
}

data class Player(
    val firstName: String = "",
    val lastName: String = "",
    val position: Position = Position.NA
)
