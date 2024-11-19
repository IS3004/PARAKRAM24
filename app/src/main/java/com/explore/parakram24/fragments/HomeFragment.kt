package com.explore.parakram24.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.explore.parakram24.R
import com.explore.parakram24.adapters.ViewPagerAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import com.explore.parakram24.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentPage = 0

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countDownStart()
        binding.eventBrochure.setOnClickListener{
            browser("https://drive.google.com/file/d/1m-R-CRPPjHHP30zZTs293OvfZKwFh9dC/view")
        }
        binding.noc.setOnClickListener {
            browser("https://drive.google.com/file/d/1zhVgGgWp5wOWLTPSqcK748Pwh-Z5ipYg/view")
        }

        binding.rulebook.setOnClickListener {
            browser("https://drive.google.com/file/d/1ST66nTiMW_pwpS4uiiP6Oiq5hqWm4GuB/view")
        }

        //binding.background.setRenderEffect(RenderEffect.createBlurEffect(10f,10f, Shader.TileMode.MIRROR))
        binding.viewPagerCarousel.adapter = ViewPagerAdapter()

        binding.viewPagerCarousel.setPageTransformer(Transformer())

        addDotsIndicator()
        viewLifecycleOwner.lifecycleScope.launch {
            while(true) {
                delay(2000)
                binding.viewPagerCarousel.currentItem = (currentPage++) % 7
            }
        }


        binding.viewPagerCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
                currentPage = position
            }
        })
    }

    private fun browser(url : String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (browserIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(browserIntent)
        } else {
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }



    private fun countDownStart() {
        @Suppress("DEPRECATION") val handler = Handler()
        val runnable = object : Runnable {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun run() {
                handler.postDelayed(this, 1000)
                try {
                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val futureDate: Date = dateFormat.parse("2024-03-08 00:00:00")!!
                    if (!currentDate.after(futureDate)) {
                        var diff: Long = (futureDate.time
                                - currentDate.time)
                        val days = diff / (24 * 60 * 60 * 1000)
                        diff -= days * (24 * 60 * 60 * 1000)
                        val hours = diff / (60 * 60 * 1000)
                        diff -= hours * (60 * 60 * 1000)
                        val minutes = diff / (60 * 1000)
                        diff -= minutes * (60 * 1000)
                        val seconds = diff / 1000
                        binding.txtDay.text = "" + String.format("%02d", days)
                        binding.txtHour.text = "" + String.format("%02d", hours)
                        binding.txtMinute.text = "" + String.format("%02d", minutes)
                        binding.txtSecond.text = "" + String.format("%02d",seconds)
                    }
                    else {
                        binding.comingsoon.visibility = View.INVISIBLE
                        binding.txtDay.visibility = View.INVISIBLE
                        binding.txtHour.visibility = View.INVISIBLE
                        binding.txtMinute.visibility = View.INVISIBLE
                        binding.txtSecond.visibility = View.INVISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        handler.postDelayed(runnable, 1 * 1000)

    }

    private fun addDotsIndicator() {
        val size=7
        val dots = arrayOfNulls<ImageView>(size)
        for (i in 0 until size) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.indicator_inactive
            ))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.dotLayout.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.indicator_active
        ))
    }

    private fun updateDots(position: Int) {
        val childCount = binding.dotLayout.childCount
        for (i in 0 until childCount) {
            val dot = binding.dotLayout.getChildAt(i) as ImageView
            val drawableId =
                if (i == position) R.drawable.indicator_active else R.drawable.indicator_inactive
            dot.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawableId))
        }
    }

}

class Transformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val width = view.width.toFloat()
        view.rotationX = 0f
        view.rotationY = 0f
        view.rotation = 0f
        view.scaleX = 1f
        view.scaleY = 1f
        view.pivotX = 0f
        view.pivotY = 0f
        view.translationY = 0f
        view.translationX = -width * position
        view.alpha = if (position <= -1f || position >= 1f) 0f else 1f
        view.pivotX = (if (position < 0) 0 else view.width).toFloat()
        view.scaleX = if (position < 0) 1f + position else 1f - position
    }

}

