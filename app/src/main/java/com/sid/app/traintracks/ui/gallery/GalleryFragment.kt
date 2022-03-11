package com.sid.app.traintracks.ui.gallery

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.sid.app.traintracks.databinding.FragmentGalleryBinding
import com.sid.app.traintracks.R
import com.sid.app.traintracks.helper.Puzzle


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        galleryViewModel =
            ViewModelProvider(this)[GalleryViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        galleryViewModel.text.observe(viewLifecycleOwner, {
            binding.textGallery.text = it
        })

        val animR: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(binding.trainImage.context, R.drawable.animated_train)
        animR?.let {
            binding.trainImage.setImageDrawable(it)
        }
        (binding.trainImage.drawable as Animatable?)?.start()


        val smokeAnim: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(binding.smokeImage.context, R.drawable.animated_smoke1)
        smokeAnim?.let {
            binding.smokeImage.setImageDrawable(it)
        }
        (binding.smokeImage.drawable as Animatable?)?.start()
        binding.smokeImage.visibility = View.VISIBLE


        val transitionDelay = resources.getInteger(R.integer.transition_delay).toLong()
        val longAnimation = resources.getInteger(R.integer.animation_long).toLong()
        val veryLongAnimation = resources.getInteger(R.integer.animation_very_long).toLong()
        val shortAnimation = resources.getInteger(R.integer.animation_short).toLong()
        val dip = resources.getDimension(R.dimen.train_length) * -1 -10
        val resources: Resources = resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            resources.displayMetrics
        )

        ObjectAnimator.ofFloat(binding.trainImage, "x", px).apply {
            duration = longAnimation
            startDelay = transitionDelay + 100
            interpolator = LinearInterpolator()
            start()
        }

        binding.smokeImage
            .animate()
            .alpha(0f)
            .setDuration(veryLongAnimation)
            .setStartDelay(veryLongAnimation)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.smokeImage.visibility = View.GONE
                }
            })
        binding.gridLayout.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(shortAnimation)
                .setStartDelay(longAnimation)
                .setListener(null)
        }

        val puzzle = Puzzle(5)
        puzzle.fill()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.gridLayout.clearAnimation()
        binding.smokeImage.clearAnimation()
        binding.trainImage.clearAnimation()

        _binding = null
    }
}