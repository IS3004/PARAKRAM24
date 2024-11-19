package com.explore.parakram24.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.explore.parakram24.R
import com.explore.parakram24.adapters.AnnouncementAdapter
import com.explore.parakram24.adapters.SponsorAdapter
import com.explore.parakram24.databinding.FragmentAnnouncementBinding
import com.explore.parakram24.viewmodel.NotificationViewModel
import com.explore.parakram24.viewmodel.SponsorsViewModel


class AnnouncementFragment : Fragment() {

    private var _binding : FragmentAnnouncementBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: NotificationViewModel
    private lateinit var adapter : AnnouncementAdapter
    private lateinit var swipeLayout : SwipeRefreshLayout
    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnouncementBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NotificationViewModel::class.java]

        binding.rvNotifications.layoutManager = LinearLayoutManager(context)
        binding.rvNotifications.setHasFixedSize(true)
        adapter = AnnouncementAdapter(emptyList())
        binding.rvNotifications.adapter = adapter

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

        viewModel.dataLoaded.observe(viewLifecycleOwner){isData ->
            Log.i("isdata",isData.toString())
            if(isData){
                binding.notificationsLoading.visibility= View.GONE
            }
            else{
                binding.notificationsLoading.visibility = View.VISIBLE
            }
        }


        viewModel.loading.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        }

        viewModel.notificationData.observe(viewLifecycleOwner) { data ->
            adapter.setData(data)
        }

        viewModel.fetchData()

        swipeLayout = binding.swipeLayoutNotifications
        swipeLayout.setOnRefreshListener {
            viewModel.fetchData()
            swipeLayout.isRefreshing = false
        }

    }

}