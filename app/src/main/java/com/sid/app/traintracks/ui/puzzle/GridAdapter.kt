package com.sid.app.traintracks.ui.puzzle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid.app.traintracks.R


class GridAdapter(private val mDataSet: Array<Int>, private val topHints: Array<Int>, private val leftHints: Array<Int>) :
    RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private var selectedPosition: Int? = null
    private val selectedToUnselectedDrawable = mapOf(
        R.drawable.ic_blank_track_selected to R.drawable.ic_blank_track,
        R.drawable.ic_no_track_selected to R.drawable.ic_no_track,
        R.drawable.ic_yes_track_selected to R.drawable.ic_yes_track,
        R.drawable.ic_straight_track_horizontal_selected to R.drawable.ic_straight_track_horizontal,
        R.drawable.ic_straight_track_vertical_selected to R.drawable.ic_straight_track_vertical,
        R.drawable.ic_corner_track_ne_selected to R.drawable.ic_corner_track_ne,
        R.drawable.ic_corner_track_nw_selected to R.drawable.ic_corner_track_nw,
        R.drawable.ic_corner_track_se_selected to R.drawable.ic_corner_track_se,
        R.drawable.ic_corner_track_sw_selected to R.drawable.ic_corner_track_sw,
        R.drawable.ic_north_track_selected to R.drawable.ic_north_track,
        R.drawable.ic_east_track_selected to R.drawable.ic_east_track,
        R.drawable.ic_south_track_selected to R.drawable.ic_south_track,
        R.drawable.ic_west_track_selected to R.drawable.ic_west_track
    )
    private val unselectedToSelectedDrawable = mapOf(
        R.drawable.ic_blank_track to R.drawable.ic_blank_track_selected,
        R.drawable.ic_no_track to R.drawable.ic_no_track_selected,
        R.drawable.ic_yes_track to R.drawable.ic_yes_track_selected,
        R.drawable.ic_straight_track_horizontal to R.drawable.ic_straight_track_horizontal_selected,
        R.drawable.ic_straight_track_vertical to R.drawable.ic_straight_track_vertical_selected,
        R.drawable.ic_corner_track_ne to R.drawable.ic_corner_track_ne_selected,
        R.drawable.ic_corner_track_nw to R.drawable.ic_corner_track_nw_selected,
        R.drawable.ic_corner_track_se to R.drawable.ic_corner_track_se_selected,
        R.drawable.ic_corner_track_sw to R.drawable.ic_corner_track_sw_selected,
        R.drawable.ic_north_track to R.drawable.ic_north_track_selected,
        R.drawable.ic_east_track to R.drawable.ic_east_track_selected,
        R.drawable.ic_south_track to R.drawable.ic_south_track_selected,
        R.drawable.ic_west_track to R.drawable.ic_west_track_selected
    )

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imageView: ImageView = v.findViewById<View>(R.id.imageView) as ImageView
        val textView: TextView = v.findViewById<View>(R.id.hint) as TextView
    }

    private fun selectItem(position: Int) {
        mDataSet[position] = unselectedToSelectedDrawable[mDataSet[position]] ?: mDataSet[position]
        notifyItemChanged(position)
    }

    private fun unselectItem(position: Int) {
        mDataSet[position] = selectedToUnselectedDrawable[mDataSet[position]] ?: mDataSet[position]
        notifyItemChanged(position)
    }

    fun setTrackDrawable(drawable: Int) {
        selectedPosition?.let {
            mDataSet[it] = drawable
            notifyItemChanged(it)
        }
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.puzzle_grid, viewGroup, false)
        return ViewHolder(v)
    }

    // END_INCLUDE(recyclerViewOnCreateViewHolder)
    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pos: Int = position
        val size = 10

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.imageView.setImageResource(mDataSet[pos])
        if (pos % size != 0 && pos > size && pos < size * (size - 1) && (pos - (size - 1)) % size != 0) {
            viewHolder.imageView.setOnClickListener {
                selectedPosition?.let { pos -> unselectItem(pos) }
                selectItem(pos)
                selectedPosition = pos
                viewHolder.imageView.setImageResource(mDataSet[pos])
            }
        }
        if (pos in 1 until size - 1) {
            viewHolder.textView.text = topHints[pos - 1].toString()
        } else if (pos % size == 0 && pos in 1 until ((size - 1) * size)) {
            viewHolder.textView.text = leftHints[pos / 10 - 1].toString()
        }
    }

    // END_INCLUDE(recyclerViewOnBindViewHolder)
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataSet.size
    }

    companion object {
        private const val TAG = "CustomAdapter"
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)
}


//class GridAdapter: RecyclerView {
//    private var list = ArrayList<Pair<Boolean, Int>>(64)
//    private var selected: Int = -1
//
//    init {
//        for (i in 0 until 64) {
//            list.add(false to 3)
//        }
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val inflater =
//            parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        var view = convertView
//        if (convertView == null) {
//            view = inflater.inflate(R.layout.puzzle_grid, null) as ImageView
//            view.setImageResource(setImage(position))
//            view.setOnClickListener {
//                if (selected > -1) {
//                    unselectView(selected)
//                }
//                selected = position
//                selectView(selected)
//                val image = setImage(position)
//                view.setImageResource(image)
//                view.rotation = Random.nextInt(4) * 90f
//            }
//        }
//        return view
//    }
//
//    private fun unselectView(position: Int) {
//        list[position] = list[position].first to list[position].second + 3
//        notifyDataSetChanged()
//    }
//
//    private fun selectView(position: Int) {
//        list[position] = list[position].first to list[position].second - 3
//        notifyDataSetChanged()
//    }
//
//    private fun setImage(position: Int): Int {
//        return when (list[position].second) {
//            0 -> R.drawable.ic_no_track_selected
//            1 -> R.drawable.ic_corner_track_selected
//            2 -> R.drawable.ic_straight_track_selected
//            3 -> R.drawable.ic_no_track
//            4 -> R.drawable.ic_corner_track
//            5 -> R.drawable.ic_straight_track
//            else -> R.drawable.ic_no_track
//        }
//    }
//
//    override fun getItem(position: Int): Pair<Boolean, Int> = list[position]
//
//    override fun getItemId(position: Int): Long = position.toLong()
//
//    override fun getCount(): Int = list.size
//}