package com.sqlitedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sqlitedemo.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var databaseHandler: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        databaseHandler = DatabaseHandler(this)

        Log.e("value", databaseHandler.getAllData().toString())
        binding.recyclerView.apply {
            linearLayoutManager = LinearLayoutManager(context)
            this.layoutManager = linearLayoutManager
            val adapter = AdapterClass(this@MainActivity2,databaseHandler.getAllData())
            this.adapter = adapter

        }

    }
}