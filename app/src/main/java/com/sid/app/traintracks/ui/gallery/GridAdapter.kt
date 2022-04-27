package com.sid.app.traintracks.ui.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.sid.app.traintracks.R
import kotlin.random.Random

class GridAdapter: BaseAdapter() {
    private var list = List(64) {Pair(false, 0)}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater =
            parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.puzzle_grid, null) as ImageView
        view.setOnClickListener {
            val image = when (list[position].second) {
                0 -> R.drawable.ic_no_track
                1 -> R.drawable.ic_corner_track
                else -> R.drawable.ic_straight_track
            }
            view.setImageResource(image)
            view.rotation = Random.nextInt(4) * 90f
        }
        return view
    }

    override fun getItem(position: Int): Pair<Boolean, Int> = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size
}