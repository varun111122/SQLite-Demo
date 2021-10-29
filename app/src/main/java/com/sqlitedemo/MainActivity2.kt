package com.sqlitedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sqlitedemo.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(), AdapterClass.OnItemLongClickListener {
    lateinit var binding: ActivityMain2Binding
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var databaseHandler: DatabaseHandler
    var list: MutableList<UserModel>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        databaseHandler = DatabaseHandler(this)
        list = databaseHandler.getAllData()

        binding.recyclerView.apply {
            linearLayoutManager = LinearLayoutManager(context)
            this.layoutManager = linearLayoutManager
            val adapter = AdapterClass(this@MainActivity2, list!!, this@MainActivity2)
            this.adapter = adapter

        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                Log.e("varun", p0.toString())


                val temp: MutableList<UserModel> = ArrayList()

                if (p0!!.length >= 1) {

                    for (d in databaseHandler.getAllData()!!) {
                        var strName = d.name


                        if (strName!!.contains(p0, true)) {
                            temp.add(d)
                        }


                    }
                    if (temp.size > 0) {
                        val adapter = AdapterClass(this@MainActivity2, temp, this@MainActivity2)
                        binding.recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@MainActivity2, "No Matching data", Toast.LENGTH_SHORT)
                            .show()
                    }


                } else {
                    val adapter =
                        AdapterClass(this@MainActivity2, list!!, this@MainActivity2)
                    binding.recyclerView.adapter = adapter

                }


            }
        })

    }

    override fun onItemLongClick(i: Int) {
        showBottomSheetDialog(i)

    }

    private fun showBottomSheetDialog(i: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.delete_popup)
        val btn_deleteSingle = bottomSheetDialog.findViewById<Button>(R.id.btn_deleteSingle)
        val btn_deleteMultiple = bottomSheetDialog.findViewById<Button>(R.id.btn_deleteMultiple)



        btn_deleteSingle!!.setOnClickListener() {
            bottomSheetDialog.dismiss()
            databaseHandler.deleteSingle(list!![i].name)
            list!!.removeAt(i);
            binding.recyclerView.adapter!!.notifyDataSetChanged()
            binding.recyclerView.adapter!!.notifyItemChanged(i)
        }

        btn_deleteMultiple!!.setOnClickListener() {
            bottomSheetDialog.dismiss()
            databaseHandler.deleteAllTable()
            list!!.clear()
            binding.recyclerView.adapter!!.notifyDataSetChanged()
            binding.recyclerView.adapter!!.notifyItemChanged(i)
        }
        bottomSheetDialog.show()
    }

}