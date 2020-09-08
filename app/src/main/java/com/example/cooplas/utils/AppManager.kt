package com.example.cooplas.utils

import com.jobesk.gong.utils.RestClient

class AppManager{

     val restClient = RestClient()

     companion object {
         private  val instance = AppManager()
         fun  getInstance(): AppManager {
             return  instance
         }
     }
 }