package com.sid.app.traintracks.ui.gallery

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.sid.app.traintracks.R
import com.sid.app.traintracks.databinding.FragmentGalleryBinding

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
                ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val animR: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(binding.trainImage.context, R.drawable.animated_train)
        animR?.let {
            binding.trainImage.setImageDrawable(it)
        }
        (binding.trainImage.drawable as Animatable?)?.start()

        val smokes = arrayOf(
            binding.smokeImage1
        )

        val smokeAnims = arrayOf(
            R.drawable.animated_smoke1
        )

        val smokeAnim: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(smokes[0].context, smokeAnims[0])
        smokeAnim?.let {
            smokes[0].setImageDrawable(it)
        }
        (smokes[0].drawable as Animatable?)?.start()



        val transitionDelay = resources.getInteger(R.integer.transition_delay).toLong()
        val longAnimation = resources.getInteger(R.integer.animation_long).toLong()
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}