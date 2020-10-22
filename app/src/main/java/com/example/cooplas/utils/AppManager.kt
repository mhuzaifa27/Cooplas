package com.example.cooplas.utils

import com.example.cooplas.models.Post

class AppManager{

    var post_data = Post(id =0, body ="", user_id =0, created_at ="", updated_at ="", likes_count =0, comments_count =0, media = emptyList(), comments = emptyList(), user =null)

     val restClient = RestClient()

     companion object {
         private  val instance = AppManager()
         fun  getInstance(): AppManager {
             return  instance
         }
     }
 }