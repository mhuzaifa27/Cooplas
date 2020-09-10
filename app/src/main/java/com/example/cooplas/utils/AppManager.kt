package com.example.cooplas.utils

class AppManager{

     val restClient = RestClient()

     companion object {
         private  val instance = AppManager()
         fun  getInstance(): AppManager {
             return  instance
         }
     }
 }