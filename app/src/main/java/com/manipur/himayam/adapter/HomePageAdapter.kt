package com.manipur.himayam.adapter

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manipur.himayam.R
import com.manipur.himayam.dto.Item
import com.manipur.himayam.misc.ItemDiffCallback
import com.manipur.himayam.misc.SpannedHtmlString.Companion.fromHtml

class HomePageAdapter(
    private var items: List<Item>,
    private val onItemClicked: (Item) -> Unit
) : RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_page, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return HomePageViewHolder(view)
    }


//    fun updateData(newItems: List<Item>) {
//        items = newItems
//        notifyDataSetChanged()
//    }
    fun updateData(newItems: List<Item>) {
        val diffCallback = ItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class HomePageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image)
        val textView: TextView = view.findViewById(R.id.heading)

        fun bind(item: Item) {
            imageView.setImageDrawable(item.image)
            val htmlContent = item.text
            val spanned: Spanned = fromHtml(htmlContent, itemView.context, textView, 600, 900)
            if (spanned.length > 100) {
                textView.text = spanned
            } else {
                textView.text = spanned
                textView.setOnClickListener(null)
            }

            itemView.setOnClickListener {
                onItemClicked(item)
            }
        }
    }
}