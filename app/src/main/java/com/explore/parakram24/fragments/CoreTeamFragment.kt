package com.explore.parakram24.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.explore.parakram24.R
import com.explore.parakram24.adapters.CoreteamAdapter
import com.explore.parakram24.databinding.FragmentCoreTeamBinding
import com.explore.parakram24.viewmodel.CoreTeamViewModel

class CoreTeamFragment : Fragment() {

    private var _binding : FragmentCoreTeamBinding ?=  null
    private val binding get() = _binding!!
    private lateinit var viewModel: CoreTeamViewModel
    private lateinit var adapter: CoreteamAdapter
    private lateinit var dialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[CoreTeamViewModel::class.java]

        binding.rvCoreTeam.layoutManager = GridLayoutManager(context,2)
        binding.rvCoreTeam.setHasFixedSize(true)
        adapter = CoreteamAdapter(emptyList())
        binding.rvCoreTeam.adapter = adapter
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

        viewModel.coreTeamData.observe(viewLifecycleOwner) { data ->
            adapter.setData(data)
        }

        viewModel.fetchData()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoreTeamBinding.inflate(layoutInflater)
        return binding.root
    }
}