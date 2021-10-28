package com.sqlitedemo

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sqlitedemo.databinding.ActivityMainBinding
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    lateinit var databaseHandler: DatabaseHandler
    lateinit var image_uri: Uri
    lateinit var imgToStore: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        databaseHandler = DatabaseHandler(this)

        binding.txtSelectImage.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.btnAllData.setOnClickListener(this)

    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.upload_photo)
        val camera = bottomSheetDialog.findViewById<RelativeLayout>(R.id.layout_takephoto)
        val gallary = bottomSheetDialog.findViewById<RelativeLayout>(R.id.layout_choosephoto)
        val btnClose = bottomSheetDialog.findViewById<Button>(R.id.btn_cancel_dialog)

        btnClose!!.setOnClickListener() {
            bottomSheetDialog.dismiss()
        }

        camera!!.setOnClickListener() {
            bottomSheetDialog.dismiss()
            runtimePermissions("1")
        }

        gallary!!.setOnClickListener() {
            bottomSheetDialog.dismiss()
            runtimePermissions("2")
        }
        bottomSheetDialog.show()
    }

    private fun runtimePermissions(value: String) {

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {

                            if (value.equals("1")) {
                                takePhoto()
                            } else {
                                choosePhoto()
                            }

                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {

                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Log.e("Dexter error:: ", it.name)
            }
            .check()
    }

    private fun takePhoto() {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, 0)

    }

    private fun choosePhoto() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(i, "Select Picture"),
            1
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            CropImage.activity(image_uri).start(this)


        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage2 = data.data
            CropImage.activity(selectedImage2).start(this)

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.getUri()

//                val file = File(resultUri.getPath()!!)
                imgToStore = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
                binding.txtSelectImage.visibility = View.GONE
                binding.imgProfile.setImageBitmap(imgToStore)

                Log.e("Path", "" + resultUri);

                //store image in multipart variable


            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            val result = CropImage.getActivityResult(data)
            var error = result!!.getError();
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show();
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.txt_select_image -> {
                showBottomSheetDialog()
            }
            R.id.btn_submit -> {
                storeImage()
            }
            R.id.btn_allData -> {
                startActivity(Intent(this, MainActivity2::class.java))
            }
        }
    }

    fun storeImage() {

        try {
            if (!binding.edtName.text.toString().isEmpty() && !binding.edtGender.text.toString()
                    .isEmpty() && binding.imgProfile.drawable != null
            ) {

                databaseHandler.storeUserData(
                    UserModel(
                        binding.edtName.text.toString(),
                        binding.edtGender.text.toString(),
                        imgToStore
                    )
                )
            } else {
                Toast.makeText(this, "Please fill all userInfo", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}