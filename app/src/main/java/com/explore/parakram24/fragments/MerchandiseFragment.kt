package com.explore.parakram24.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.explore.parakram24.R
import com.explore.parakram24.databinding.FragmentMerchandiseBinding

class MerchandiseFragment : Fragment() {


    private var _binding : FragmentMerchandiseBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchandiseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { Glide.with(it)
            .load("https://res.cloudinary.com/dmf9vpeu2/image/upload/v1708955492/T-shirt_mlb5tv.png")
            .apply(
                RequestOptions().placeholder(R.drawable.ic_loading).error(R.drawable.img)
                    .diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    )
            ).into(binding.ivTshirt)

            Glide.with(it)
                .load("https://res.cloudinary.com/dmf9vpeu2/image/upload/v1708955492/Jacket_h6xhte.png")
                .apply(
                    RequestOptions().placeholder(R.drawable.ic_loading).error(R.drawable.img)
                        .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        )
                ).into(binding.ivHoodie)
        }

        binding.btnBuy.setOnClickListener{
            val url = "https://docs.google.com/forms/d/e/1FAIpQLSfusnTl0EayLZb_QJkuE1RxGFrseBoluGb50vSqsy0DVkRfAw/viewform"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (browserIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(browserIntent)
            } else {
                Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}