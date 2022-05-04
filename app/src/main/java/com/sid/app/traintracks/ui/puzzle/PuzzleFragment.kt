package com.sid.app.traintracks.ui.puzzle

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.sid.app.traintracks.databinding.FragmentPuzzleBinding
import com.sid.app.traintracks.R
import com.sid.app.traintracks.helper.Puzzle


class PuzzleFragment : Fragment() {

    private lateinit var puzzleViewModel: PuzzleViewModel
    private lateinit var adapter: GridAdapter
    private var _binding: FragmentPuzzleBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        puzzleViewModel =
            ViewModelProvider(this)[PuzzleViewModel::class.java]

        _binding = FragmentPuzzleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        puzzleViewModel.text.observe(viewLifecycleOwner) {
            binding.textPuzzle.text = it
        }

        val animR: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(
            binding.trainImage.context,
            R.drawable.animated_train
        )
        animR?.let {
            binding.trainImage.setImageDrawable(it)
        }
        (binding.trainImage.drawable as Animatable?)?.start()


        val smokeAnim: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(
            binding.smokeImage.context,
            R.drawable.animated_smoke1
        )
        smokeAnim?.let {
            binding.smokeImage.setImageDrawable(it)
        }
        (binding.smokeImage.drawable as Animatable?)?.start()
        binding.smokeImage.visibility = View.VISIBLE


        var transitionDelay = resources.getInteger(R.integer.transition_delay).toLong()
        var longAnimation = resources.getInteger(R.integer.animation_long).toLong()
        var veryLongAnimation = resources.getInteger(R.integer.animation_very_long).toLong()
        var shortAnimation = resources.getInteger(R.integer.animation_short).toLong()
        val dip = resources.getDimension(R.dimen.train_length) * -1 - 10
        val resources: Resources = resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            resources.displayMetrics
        )

        if (puzzleViewModel.initialized.value == true) {
            transitionDelay = 0
            veryLongAnimation = 0
            longAnimation = 0
            shortAnimation = 0
        }

        ObjectAnimator.ofFloat(binding.trainImage, "x", px).apply {
            duration = longAnimation
            startDelay = transitionDelay + 100
            interpolator = LinearInterpolator()
            start()
        }

        binding.smokeImage
            .animate()
            .alpha(0f)
            .setDuration(longAnimation).startDelay = veryLongAnimation
        binding.gridLayout.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(shortAnimation)
                .setStartDelay(veryLongAnimation)
                .setListener(null)
        }

        val gridSize = 10
        val currentGridInputs = Array(gridSize * gridSize) { R.drawable.ic_blank_track }
        val gridAnswers = Array(gridSize * gridSize) { R.drawable.ic_blank_track }
        val topHints = Array(gridSize - 2) { 0 }
        val leftHints = Array(gridSize - 2) { 0 }

        if (puzzleViewModel.initialized.value == false) {
            val path = Puzzle().generateMaze(gridSize - 2)
            for (i in 1 until path.size - 1) {
                topHints[path[i].second]++
                leftHints[path[i].first]++
            }
            for (i in 1 until gridSize - 1) {
                currentGridInputs[0 * gridSize + i] = R.drawable.ic_blank_bottom
                currentGridInputs[i * gridSize + 0] = R.drawable.ic_blank_right
                currentGridInputs[(gridSize - 1) * gridSize + i] = R.drawable.ic_blank_top
                currentGridInputs[i * gridSize + gridSize - 1] = R.drawable.ic_blank_left
                gridAnswers[0 * gridSize + i] = R.drawable.ic_blank_bottom
                gridAnswers[i * gridSize + 0] = R.drawable.ic_blank_right
                gridAnswers[(gridSize - 1) * gridSize + i] = R.drawable.ic_blank_top
                gridAnswers[i * gridSize + gridSize - 1] = R.drawable.ic_blank_left
            }
            currentGridInputs[0] = R.drawable.ic_blank_bottomright
            gridAnswers[0] = R.drawable.ic_blank_bottomright
            currentGridInputs[gridSize - 1] = R.drawable.ic_blank_bottomleft
            gridAnswers[gridSize - 1] = R.drawable.ic_blank_bottomleft
            currentGridInputs[gridSize * gridSize - 1] = R.drawable.ic_blank_topleft
            gridAnswers[gridSize * gridSize - 1] = R.drawable.ic_blank_topleft
            currentGridInputs[(gridSize - 1) * gridSize] = R.drawable.ic_blank_topright
            gridAnswers[(gridSize - 1) * gridSize] = R.drawable.ic_blank_topright
            for (cell in path) {
                val index = (cell.first + 1) * gridSize + cell.second + 1
                when (cell.third) {
                    -1 -> gridAnswers[index] = R.drawable.ic_straight_track_vertical
                    -2 -> gridAnswers[index] = R.drawable.ic_straight_track_horizontal
                    -3 -> gridAnswers[index] = R.drawable.ic_corner_track_ne
                    -4 -> gridAnswers[index] = R.drawable.ic_corner_track_se
                    -5 -> gridAnswers[index] = R.drawable.ic_corner_track_sw
                    -6 -> gridAnswers[index] = R.drawable.ic_corner_track_nw
                    -7 -> {
                        currentGridInputs[index] = R.drawable.ic_east_track_start
                        gridAnswers[index] = R.drawable.ic_east_track_start
                    }
                    -8 -> {
                        currentGridInputs[index] = R.drawable.ic_north_track_start
                        gridAnswers[index] = R.drawable.ic_north_track_start
                    }
                    -9 -> {
                        currentGridInputs[index] = R.drawable.ic_west_track_start
                        gridAnswers[index] = R.drawable.ic_west_track_start
                    }
                    -10 -> {
                        currentGridInputs[index] = R.drawable.ic_south_track_start
                        gridAnswers[index] = R.drawable.ic_south_track_start
                    }
                    else -> {
                        currentGridInputs[index] = R.drawable.ic_blank_track
                        gridAnswers[index] = R.drawable.ic_blank_track
                    }
                }
            }
            puzzleViewModel.initialize(gridAnswers, currentGridInputs, topHints, leftHints)
        } else {
            puzzleViewModel.leftHints.value?.forEachIndexed { index, leftHint ->
                leftHints[index] = leftHint
            }
            puzzleViewModel.topHints.value?.forEachIndexed { index, topHint ->
                topHints[index] = topHint
            }
            puzzleViewModel.answers.value?.forEachIndexed { index, answer ->
                gridAnswers[index] = answer
            }
            puzzleViewModel.storedValues.value?.forEachIndexed { index, storedVal ->
                currentGridInputs[index] = storedVal
            }
        }

        adapter = GridAdapter(puzzleViewModel.storedValues, topHints, leftHints)
        binding.gridLayout.layoutManager = GridLayoutManager(context, gridSize)
        binding.gridLayout.adapter = adapter

        binding.delete.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_blank_track_selected)
        }

        binding.noTrack.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_no_track_selected)
        }

        binding.yesTrack.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_yes_track_selected)
        }

        binding.cornerNE.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_corner_track_ne_selected)
        }

        binding.cornerNW.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_corner_track_nw_selected)
        }

        binding.cornerSW.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_corner_track_sw_selected)
        }

        binding.cornerSE.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_corner_track_se_selected)
        }

        binding.straightHorizontal.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_straight_track_horizontal_selected)
        }

        binding.straightVertical.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_straight_track_vertical_selected)
        }

        binding.north.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_north_track_selected)
        }

        binding.east.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_east_track_selected)
        }

        binding.south.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_south_track_selected)
        }

        binding.west.setOnClickListener {
            adapter.setTrackDrawable(R.drawable.ic_west_track_selected)
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        binding.container.children.forEach { it.visibility = View.VISIBLE }
    }

    override fun onPause() {
        super.onPause()
        adapter.unselectTrack()
        binding.container.children.forEach { it.visibility = View.GONE }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding.smokeImage.animation.cancel()
        //binding.trainImage.animation.cancel()
        binding.gridLayout.clearAnimation()
        binding.smokeImage.clearAnimation()
        binding.trainImage.clearAnimation()

        _binding = null
    }
}