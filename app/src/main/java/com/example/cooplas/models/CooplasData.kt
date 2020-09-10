package com.example.cooplas.models

data class SignUpSigninRes(
    val success: Boolean,
    val message: String,
    val user: User
)

data class User(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val phone_verified: Boolean,
    val email: String,
    val email_verified: Boolean,
    val email_verification_code: Int,
    val password_reset_code: String,
    val role: String,
    val profile_pic: String,
    val gender: String,
    val username: String,
    val dob: String,
    val relationship_status: String,
    val current_location_id: String,
    val auth_token: String,
    val updated_at: String,
    val created_at: String

)

data class GeneralRes(
  //  val error: Int,
    val status: Int,
    val message: String
)

