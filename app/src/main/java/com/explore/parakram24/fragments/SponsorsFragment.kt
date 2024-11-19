package com.explore.parakram24.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.explore.parakram24.R
import com.explore.parakram24.adapters.SponsorAdapter
import com.explore.parakram24.databinding.FragmentSponsorsBinding
import com.explore.parakram24.viewmodel.SponsorsViewModel

class SponsorsFragment : Fragment() {

    private var _binding : FragmentSponsorsBinding?= null
    private val binding get() = _binding!!
    private lateinit var viewModel: SponsorsViewModel
    private lateinit var adapter : SponsorAdapter
    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSponsorsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SponsorsViewModel::class.java]

        binding.rvSponsors.layoutManager = GridLayoutManager(context,2)
        binding.rvSponsors.setHasFixedSize(true)
        adapter = SponsorAdapter(emptyList()) { url ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (browserIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(browserIntent)
            } else {
                Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
            }
        }
        // Set the adapter to the RecyclerView
        binding.rvSponsors.adapter = adapter

        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.loadingcard)
        dialog.setCancelable(false)
        val layoutParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        dialog.window?.attributes = layoutParams
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary4
                    )
                )
            )
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent)

        }


        viewModel.loading.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        }

        viewModel.sponsorData.observe(viewLifecycleOwner) { data ->
            adapter.setData(data)
        }

        viewModel.fetchData()
        return binding.root
    }
}

