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
    val created_at: String,
    val following: Int

)



data class GeneralRes(
  //  val error: Int,
    val status: Int,
    val message: String
)

data class PostlikeRes(
    val next_offset: Int,
    val likes: List<LikesData>
)

data class LikesData(
    val id: Int,
    val post_id: Int,
    val user_id: Int,
    val created_at: String,
    val updated_at: String,
    val user: User
)

data class FollowersRes(
    val next_offset: Int,
    val followers: List<User>,
    val following: List<User>
)

data class ProfileRes(
    val posts_next_offset: Int,
    val wall: Wall
)

data class Wall(
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
    val created_at: String,
    val following_count: Int,
    val followers_count: Int,
    val posts: List<Post>
)

data class Post(
    val id: Int,
    val body: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String,
    val likes_count: Int,
    val comments_count: Int,
    val media: List<Media>,
    val comments: List<Comments>,
    val user: User?
)

data class Media(
    val id: Int,
    val type: String,
    val path: String,
    val post_id: Int,
    val user_id: Int
)

data class Comments(
    val id: Int,
    val body: String,
    val post_id: Int,
    val user_id: Int,
    val created_at: String,
    val updated_at: Boolean,
    val user: User
)

data class AddCommentRes(
    val status: Int,
    val message: String,
    val comment: Comments
)

data class CommentlistRes(
    val next_offset: Int,
    val comments: List<Comments>
)
