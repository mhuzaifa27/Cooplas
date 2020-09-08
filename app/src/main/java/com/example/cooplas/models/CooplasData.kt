package com.jobesk.gong.models

data class SignUpRes(
    val status: Int,
    val success: Boolean,
    val message: String,
    val token: String,
    val user: User
)

data class User(
    val email: String,
    val phone: String,
    val in_app_notifications: Boolean,
    val text_notifications: Boolean,
    val taxonomie_id: Int,
    val customer_id: Int,
    val updated_at: String,
    val created_at: String,
    val id: Int
)

data class GeneralRes(
  //  val error: Int,
    val status: Int,
    val message: String
)

