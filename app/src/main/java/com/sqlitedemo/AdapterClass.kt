package com.sqlitedemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sqlitedemo.databinding.RecylerViewBinding

class AdapterClass(val mainActivity2: MainActivity2, private val allData: ArrayList<UserModel>?) :
    RecyclerView.Adapter<AdapterClass.ViewHolder>() {
    class ViewHolder(val binding: RecylerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: UserModel) {
            binding.txtName.text = position.name
            binding.txtGender.text = position.userGender
            binding.imgProfile.setImageBitmap(position.image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = RecylerViewBinding.inflate(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allData!![position])
    }

    override fun getItemCount() = allData!!.size
}