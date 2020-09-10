package com.example.cooplas.activities

import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.cooplas.R
import com.example.cooplas.models.GeneralRes
import com.example.cooplas.utils.AppManager
import com.example.cooplas.utils.MarshMallowPermission
import com.jobesk.gong.utils.checkConnection
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnClickListener
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_complete_signup.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class CompleteSignup : AppCompatActivity() {
    var gender=""
    var selected_date=""
    var camerafile: File?=null
    var imagefile: File?=null
    lateinit var marshMallowPermission: MarshMallowPermission
    var CAMERA_CODE=1
    var GALLERY_CODE=2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_signup)
        marshMallowPermission=MarshMallowPermission(this)
        tv_female.setOnClickListener {
            updategender(female = true, male = false)
        }
        tv_male.setOnClickListener {
            updategender(female = false, male = true)
        }
        tv_dob.setOnClickListener {
            val datepickerdialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                tv_dob.text = "$year-$monthOfYear-$dayOfMonth"
                selected_date="$year-$monthOfYear-$dayOfMonth"
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(
                Calendar.DAY_OF_MONTH))

            datepickerdialog.show()
        }
        iv_camera.setOnClickListener {
            showImageSelectionDialog()
        }
        rl_complete_signup.setOnClickListener {
            validateDataAndSingUp()
        }

    }

    private fun updategender(female: Boolean, male: Boolean) {
        when{
            female ->{
                gender="female"
                rl_female.background = resources.getDrawable(R.drawable.round_orange)
                rl_male.background = resources.getDrawable(R.drawable.round_grey)
                tv_female.setTextColor(resources.getColor(R.color.white))
                tv_male.setTextColor(resources.getColor(R.color.black))
            }
            male->{
                gender="male"
                rl_female.background = resources.getDrawable(R.drawable.round_grey)
                rl_male.background = resources.getDrawable(R.drawable.round_orange)
                tv_female.setTextColor(resources.getColor(R.color.black))
                tv_male.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    private fun showImageSelectionDialog() {
        val dialogeplus: DialogPlus = DialogPlus.newDialog(this)
            .setContentHolder(ViewHolder(LayoutInflater.from(this).inflate(R.layout.select_image_dialog,null,false)))
            .setBackgroundColorResId(android.graphics.Color.TRANSPARENT)
            .setCancelable(false)
            .setGravity(Gravity.BOTTOM)
            .setOnClickListener(uploadImageClickListener)
            .create()
        dialogeplus.show()

    }

    private var uploadImageClickListener: OnClickListener = OnClickListener { dialog, view ->
        when (view.id) {
            R.id.tv_take -> {

                // On Take Photo
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera()
                } else {
                    openCamera()
                }
                dialog.dismiss()
            }
            R.id.tv_choose -> {

                // On Pick From Gallery
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage()
                } else {
                    openGallery()
                }
                dialog.dismiss()
            }
            R.id.tv_cancel -> dialog.dismiss()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        }
    }

    private fun openCamera() {
        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        camerafile = File(externalCacheDir,
            System.currentTimeMillis().toString() + ".jpg")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camerafile))
        startActivityForResult(intent, CAMERA_CODE)
    }

    private fun openGallery() {
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            imagefile=camerafile
        }
        if (requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            val imageuri = data?.data!!
            imagefile=File(getPath(this,imageuri))
        }
        Glide.with(this).load(imagefile).placeholder(R.drawable.profile_icon).into(iv_profile)
    }

    private fun validateDataAndSingUp(){

        when (imagefile) {
            null -> run {
                Toast.makeText(
                    this,
                    "Select Profile Image",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        when{
            et_user_name.text?.trim()?.isEmpty()!! -> run {
                et_user_name.error = "Enter your User Name"
                return }
            else -> ""
        }

        when{
            et_relationship.text?.trim()?.isEmpty()!! -> run {
                et_relationship.error = "Enter your Relationship Status"
                return }
            else -> ""
        }


        when (checkConnection(this)) {
            true->{ sign_up(et_user_name.text.trim().toString(), selected_date, et_relationship.text.toString().trim())}
            else->{ Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show()}
        }
    }

    private fun sign_up(user_name: String, date_ob: String, r_status: String) {
        val u_name = RequestBody.create(MediaType.parse("text/plain"), user_name)
        val dob = RequestBody.create(MediaType.parse("text/plain"), date_ob)
        val gender = RequestBody.create(MediaType.parse("text/plain"), gender)
        val relation_status = RequestBody.create(MediaType.parse("text/plain"), r_status)
        val image= MultipartBody.Part.createFormData("profile_pic", imagefile?.name,
            RequestBody.create(MediaType.parse("multipart/form-data"),imagefile))

        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.complete_sign_up("Bearer " + getAccessToken(this), image, gender, u_name, dob, relation_status).enqueue(object : Callback<GeneralRes> {

                override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                    kProgressHUD.dismiss()
                    Toast.makeText(this@CompleteSignup, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                    kProgressHUD.dismiss()
                    if (response.isSuccessful) {
                        Toast.makeText(this@CompleteSignup, response.body()?.message, Toast.LENGTH_SHORT).show()
//                        when (response.body()?.status) {
//                            200 -> {
                                startActivity(
                                    Intent(
                                        this@CompleteSignup,
                                        MainActivity::class.java
                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
//                            }
//                        }
                    } else {
                        val obj = JSONObject(response.errorBody()?.string())
                        Toast.makeText(this@CompleteSignup, obj.get("message").toString(), Toast.LENGTH_SHORT).show()
                    }

                }
            })
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}