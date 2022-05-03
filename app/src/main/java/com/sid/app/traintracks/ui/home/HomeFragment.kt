package com.sid.app.traintracks.ui.home

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.sid.app.traintracks.R
import com.sid.app.traintracks.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

//    private lateinit var trainAnimation: AnimationDrawable
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val animR: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(binding.trainImage.context, R.drawable.animated_train)
        animR?.let {
            binding.trainImage.setImageDrawable(it)
        }
        binding.button.setOnClickListener {
            (binding.trainImage.drawable as Animatable?)?.start()
            findNavController().navigate(R.id.action_nav_home_to_nav_gallery)
        }

//        val trainImage = binding.trainImage.apply {
//            setBackgroundResource(R.drawable.animated_train)
//            trainAnimation = background as AnimationDrawable
//        }
//        trainImage.setOnClickListener { trainAnimation.start() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}