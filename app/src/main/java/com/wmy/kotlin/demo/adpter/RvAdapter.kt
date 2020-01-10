package com.wmy.kotlin.demo.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.wmy.kotlin.demo.R


/**
 *
 *@author：wmyasw
 */
class RvAdapter(private val ctx : Context, private val list:List<String>): Adapter<RvAdapter.MyViewHolder>(){
    private val layout_inflater :LayoutInflater
    private var listener : MyListener? = null
    fun setListener(listener:MyListener){
        this.listener=listener
    }
    init {
        layout_inflater = LayoutInflater.from(ctx)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = layout_inflater.inflate(R.layout.item,null)
        val holder =MyViewHolder(view)
        return holder

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text_view : TextView
        val imageView2 : ImageView

        init {
            text_view = itemView.findViewById(R.id.textView13)
            imageView2 = itemView.findViewById(R.id.imageView2)

        }
        fun setOnClick(item: MyListener,position:Int){
            itemView.setOnClickListener({item.itemListener(itemView,position = position)})
        }

    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        holder.text_view.text = list.get(position)

        listener?.let { holder.setOnClick(it,position) }
    }

    interface MyListener {
        fun itemListener(v: View?, position: Int)
    }
}