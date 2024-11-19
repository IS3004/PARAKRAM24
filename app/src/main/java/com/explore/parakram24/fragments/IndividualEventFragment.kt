package com.explore.parakram24.fragments

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.explore.parakram24.R
import com.explore.parakram24.adapters.IndividualEventAdapter
import com.explore.parakram24.viewmodel.IndividualEventViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.explore.parakram24.databinding.FragmentIndividualEventBinding
import com.google.android.material.appbar.AppBarLayout
import java.util.Date

class IndividualEventFragment : Fragment() {

    private var _binding: FragmentIndividualEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: IndividualEventViewModel
    private lateinit var adapter : IndividualEventAdapter
    private lateinit var swipeLayout : SwipeRefreshLayout
    private lateinit var dialog : Dialog
    private val args : EventsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("currentTime in IndividualEventFragment on ViewCreated :", Date().toString())
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[IndividualEventViewModel::class.java]
        binding.rvItemEvent.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rvItemEvent.setHasFixedSize(true)
        adapter = IndividualEventAdapter(emptyList(),args.fragment)
        binding.rvItemEvent.adapter = adapter

        viewModel.eventLoading.observe(viewLifecycleOwner) { showLoading ->
            Log.i("loading :", showLoading.toString())
            if (showLoading) {
                dialog.show()
            } else {
                dialog.cancel()
            }
        }

        viewModel.data.observe(viewLifecycleOwner){isData ->
            Log.i("isdata",isData.toString())
            if(isData){
                binding.ivEventComingSoon.visibility= View.GONE
            }
            else{
                binding.ivEventComingSoon.visibility = View.VISIBLE
            }
        }

        viewModel.games.observe(viewLifecycleOwner) { data  ->
            data[args.fragment]?.let { adapter.setData(it) }
        }

        viewModel.fetchData(args.fragment)
        Log.i("args",args.fragment)
        val appBar = activity?.findViewById<ConstraintLayout>(R.id.appBar)?.findViewById<AppBarLayout>(R.id.layoutAppBar)
        val cardView = appBar?.findViewById<CardView>(R.id.cardView)
        val tvTitle = cardView?.findViewById<TextView>(R.id.tvTitle)
        tvTitle?.text = args.fragment

        swipeLayout = binding.swipeLayoutEvents
        swipeLayout.setOnRefreshListener {
            viewModel.fetchData(args.fragment)
            swipeLayout.isRefreshing = false
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndividualEventBinding.inflate(layoutInflater)
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
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}


class MyFirebase : Application(){
    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
    }

}

