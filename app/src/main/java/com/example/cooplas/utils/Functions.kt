package com.jobesk.gong.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import com.example.cooplas.utils.AppConstants
import com.example.cooplas.utils.AppConstants.Companion.ACCESS_TOKEN
import com.example.cooplas.utils.AppConstants.Companion.USER_PREF
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun isValidEmail(email: String?): Boolean {
    val emailAsRegex: String = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$"
    val pattern: Pattern
    // Regex for a valid email address
    pattern = Pattern.compile(emailAsRegex)
    val matcher: Matcher = pattern.matcher(email)
    if (!matcher.find()) {
        return false
    }
    return true
}

fun getShared_Preference(context: Context): SharedPreferences {
    return context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
}

fun saveAccessToken(context: Context, access_token: String) {
    val sharedPreferences = getShared_Preference(context)
    val editor = sharedPreferences.edit()
    editor.putString(ACCESS_TOKEN, access_token)
    editor.apply()
}

fun clearSharedPreference(context: Context) {

    val settings = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
    settings.edit().clear().commit()

}


fun getAccessToken(context: Context): String? {
    return getShared_Preference(context).getString(ACCESS_TOKEN, "")
}

fun saveUserEmailAndPass(context: Context, email: String, pass: String) {
    val editor = getShared_Preference(context).edit()
    editor.putString(AppConstants.EMAIL, email)
    editor.putString(AppConstants.PASSWORD, pass)
    editor.apply()
}

fun saveLoggedIn(context: Context, value: String) {
    val editor = getShared_Preference(context).edit()
    editor.putString(AppConstants.isLoggedIn, value)
    editor.apply()
}

fun getLoggedIn(context: Context): String {


    return getShared_Preference(context).getString(AppConstants.isLoggedIn, "").toString()
}


fun getEmail(context: Context): String? {
    return getShared_Preference(context).getString(AppConstants.EMAIL, "")
}

fun getPassword(context: Context): String? {
    return getShared_Preference(context).getString(AppConstants.PASSWORD, "")
}

fun getTimewithDate(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date =
        format.parse(dateStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
    format = SimpleDateFormat("dd MMM yyyy  hh:mm a", Locale.getDefault())
    return format.format(date)
}

fun getSimpleDatewithT(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//    format.timeZone=TimeZone.getTimeZone("GMT")
    val date = format.parse(dateStr)
    format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

fun getSimpleDate(dateStr: String): String {
//    var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date = format.parse(dateStr)
    format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

fun getTime(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val time =
        format.parse(dateStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
    format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val newTime = format.format(time)
    return newTime
}

fun getTimewithout_T(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val time = format.parse(dateStr)
    format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val newTime = format.format(time)
    return newTime
}

fun getTimeanddatewithout_T(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val time = format.parse(dateStr)
    format = SimpleDateFormat("hh:mm a dd ,MMM yyyy", Locale.getDefault())
    val newTime = format.format(time)
    return newTime
}

fun getMonth(dateStr: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date =
        format.parse(dateStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
    return android.text.format.DateFormat.format("MMM", date) as String
}

fun getMonthwithoutT(dateStr: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date = format.parse(dateStr)
    return android.text.format.DateFormat.format("MMM", date) as String
}

fun getDate(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date =
        format.parse(dateStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
    format = SimpleDateFormat("dd", Locale.getDefault())
    return format.format(date)
}

fun getDatewithoutT(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date = format.parse(dateStr)
    format = SimpleDateFormat("dd", Locale.getDefault())
    return format.format(date)
}

fun getDay(dateStr: String): String {
    var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date =
        format.parse(dateStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
    format = SimpleDateFormat("EEEE", Locale.getDefault())
    return format.format(date)
}

fun getMonthnumber(dateStr: String): Int {
    var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date = format.parse(dateStr)
    format = SimpleDateFormat("MM", Locale.getDefault())
    return format.format(date).toInt()
}

fun getintDate(dateStr: String): Int {
    var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date = format.parse(dateStr)
    format = SimpleDateFormat("dd", Locale.getDefault())
    return format.format(date).toInt()
}

fun getYear(dateStr: String): Int {
    var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT")
    val date = format.parse(dateStr)
    format = SimpleDateFormat("yyyy", Locale.getDefault())
    return format.format(date).toInt()
}

fun checkConnection(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connMgr.activeNetworkInfo
    if (activeNetworkInfo != null) { // connected to the internet
// connected to the mobile provider's data plan
        return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) { // connected to wifi
            true
        } else activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
    }
    return false
}

fun saveBitmapToFile(file: File): File? {
    try {

        // BitmapFactory options to downsize the image
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        // factor of downsizing the image

        var inputStream = FileInputStream(file)
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream, null, o)
        inputStream.close()

        // The new size we want to scale to
        val REQUIRED_SIZE = 100

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2
        }

        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        inputStream = FileInputStream(file)

        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
        inputStream.close()

        // here i override the original image file
        file.createNewFile()
        val outputStream = FileOutputStream(file)

        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        return file
    } catch (e: Exception) {
        return null
    }

}