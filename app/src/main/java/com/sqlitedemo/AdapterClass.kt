package com.sqlitedemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sqlitedemo.databinding.RecylerViewBinding

class AdapterClass(
    var mainActivity2: MainActivity2,
    private val allData: MutableList<UserModel>,
    val listner: OnItemLongClickListener
) :
    RecyclerView.Adapter<AdapterClass.ViewHolder>() {
    inner class ViewHolder(val binding: RecylerViewBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnLongClickListener {
        fun bind(position: UserModel) {
            binding.txtName.text = position.name
            binding.txtGender.text = position.userGender
            binding.imgProfile.setImageBitmap(position.image)
            binding.card.setOnLongClickListener(this)

        }

        override fun onLongClick(p0: View?): Boolean {

            listner.onItemLongClick(adapterPosition)
            return true
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

    interface OnItemLongClickListener {
        fun onItemLongClick(i: Int)
    }
}