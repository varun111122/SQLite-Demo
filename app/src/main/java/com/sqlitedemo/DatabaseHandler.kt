package com.sqlitedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.lang.Exception

val DATABASENAME = "MyDB"
val DATABASE_VERSION = 1

class DatabaseHandler(val context: Context) :
    SQLiteOpenHelper(context, DATABASENAME, null, DATABASE_VERSION) {

    private lateinit var byteArrayOutputStream: ByteArrayOutputStream
    private val createTableQuery =
        "create table USER_TABLE (userName TEXT" +
                ",userGender TEXT " + ",image BLOB)"

    override fun onCreate(p0: SQLiteDatabase?) {
        try {
            p0!!.execSQL(createTableQuery)
            Toast.makeText(context, "Table created succefully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "" + e.message.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun storeUserData(userModel: UserModel) {
        try {
            val sqLiteDatabase = this.writableDatabase
            var bitmap: Bitmap = userModel.image
            byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val imageByte = byteArrayOutputStream.toByteArray()
            val contentValues: ContentValues = ContentValues()
            contentValues.put("userName", userModel.name)
            contentValues.put("userGender", userModel.userGender)
            contentValues.put("image", imageByte)

            val checkIfQueryRun: Long = sqLiteDatabase.insert("USER_TABLE", null, contentValues)
            if (checkIfQueryRun.toInt() != -1) {
                Toast.makeText(context, "data is added to table", Toast.LENGTH_SHORT).show()
                sqLiteDatabase.close()
            } else {
                Toast.makeText(context, "data is not added to table", Toast.LENGTH_SHORT).show()

            }

        } catch (e: Exception) {

        }
    }

    fun getAllData(): ArrayList<UserModel>? {
        val list: MutableList<UserModel>? = ArrayList()
        try {
            var db: SQLiteDatabase = this.readableDatabase

            var objectCursor: Cursor = db.rawQuery("Select * from USER_TABLE", null)
            if (objectCursor.count != 0) {
                while (objectCursor.moveToNext()) {
                    val name = objectCursor.getString(0)
                    val gender = objectCursor.getString(1)
                    val image = objectCursor.getBlob(2)
                    val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    list!!.add(UserModel(name, gender, bitmap))
                }

            } else {
                Toast.makeText(context, "No data in DB", Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {
            Toast.makeText(context, "" + e.message.toString(), Toast.LENGTH_SHORT).show()

        }
        return list as ArrayList<UserModel>?
    }

    fun deleteAllTable() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM USER_TABLE")
        db.close()
    }

    fun deleteSingle(name: String) {
        val db = this.writableDatabase
        db.delete("USER_TABLE", "userName" + "=?", arrayOf(name))
    }
}