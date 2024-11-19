package com.explore.parakram24.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.explore.parakram24.MatchData
import com.explore.parakram24.R
import com.explore.parakram24.ScoreData
import com.explore.parakram24.adapters.EditableIndividualEventAdapter
import com.explore.parakram24.databinding.FragmentEditableIndividualEventBinding
import com.explore.parakram24.viewmodel.EditableIndividualEventViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditableIndividualEventFragment : Fragment(), OnFieldUpdateListener {

    private var _binding: FragmentEditableIndividualEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EditableIndividualEventViewModel
    private lateinit var adapter: EditableIndividualEventAdapter
    private lateinit var dialog: Dialog
    private lateinit var swipeLayout: SwipeRefreshLayout
    private val args: EventsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditableIndividualEventBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[EditableIndividualEventViewModel::class.java]

        val appBar = activity?.findViewById<ConstraintLayout>(R.id.appBar)
            ?.findViewById<AppBarLayout>(R.id.layoutAppBar)
        val cardView = appBar?.findViewById<CardView>(R.id.cardView)
        val tvTitle = cardView?.findViewById<TextView>(R.id.tvTitle)
        tvTitle?.text = args.fragment

        binding.rvItemEditableEvent.layoutManager = LinearLayoutManager(context)
        binding.rvItemEditableEvent.setHasFixedSize(true)
        adapter = EditableIndividualEventAdapter(emptyList(), this)
        binding.rvItemEditableEvent.adapter = adapter
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

        binding.etButtonAdd.setOnClickListener {
            viewModel.addNewGame(args.fragment)
        }


        viewModel.loading.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        }

        viewModel.etGames.observe(viewLifecycleOwner) { data ->
            data[args.fragment]?.let { adapter.setData(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchData(args.fragment)
        }


        swipeLayout = binding.swipeLayoutEditableEvents
        swipeLayout.setOnRefreshListener {
            viewModel.fetchData(args.fragment)
            swipeLayout.isRefreshing = false
        }


        return binding.root
    }


    override fun onUpdateField(
        fragment: String,
        cardKey: String,
        fieldUpdated: String,
        updatedValue: String
    ) {
        viewModel.update(fragment, cardKey, fieldUpdated, updatedValue)
    }

    override fun openDialog(matchData: MatchData) {
        dialog.show()
        val btmDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.update_card, null)

        view.findViewById<AppCompatEditText>(R.id.uc_et_league).setText(matchData.league)
        view.findViewById<AppCompatEditText>(R.id.uc_et_date).setText(matchData.date)
        view.findViewById<AppCompatEditText>(R.id.uc_et_time).setText(matchData.time)
        view.findViewById<AppCompatEditText>(R.id.uc_et_venue).setText(matchData.venue)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_field1).setText(matchData.score.field1)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_field2).setText(matchData.score.field2)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_field3).setText(matchData.score.field3)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_leftField1)
            .setText(matchData.score.leftField1)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_leftField2)
            .setText(matchData.score.leftField2)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_leftField3)
            .setText(matchData.score.leftField3)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_rightField1)
            .setText(matchData.score.rightField1)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_rightField2)
            .setText(matchData.score.rightField2)
        view.findViewById<AppCompatEditText>(R.id.uc_tv_rightField3)
            .setText(matchData.score.rightField3)
        val spinner1 = view.findViewById<Spinner>(R.id.uc_et_team1name)
        val spinner2 = view.findViewById<Spinner>(R.id.uc_et_team2name)
        val cancel = view.findViewById<AppCompatButton>(R.id.btn_cancel)
        val update = view.findViewById<AppCompatButton>(R.id.btn_update)
        val delete = view.findViewById<AppCompatButton>(R.id.btn_delete)

        val data = viewModel.getColleges()
        var array = emptyArray<String>()

        var t1n= matchData.teamAname
        var t2n= matchData.teamBname
        var t1i=0
        var t2i=0

        viewLifecycleOwner.lifecycleScope.launch {
            delay(750L)
            dialog.dismiss()
            var count =0
            for (k in data) {

                if(k.key==t1n) t1i= count
                if(k.key==t2n) t2i=count
                count++
                array += k.key
            }
            Log.i("array",array.size.toString())
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, array)
            spinner1.adapter = arrayAdapter
            spinner2.adapter = arrayAdapter
            spinner1.setSelection(t1i)
            spinner2.setSelection(t2i)
            cancel.setOnClickListener {
                btmDialog.dismiss()
            }
            update.setOnClickListener {
                val updatedLeague = view.findViewById<AppCompatEditText>(R.id.uc_et_league).text.toString()
                val updatedDate = view.findViewById<AppCompatEditText>(R.id.uc_et_date).text.toString()
                val updatedTime = view.findViewById<AppCompatEditText>(R.id.uc_et_time).text.toString()
                val updatedVenue = view.findViewById<AppCompatEditText>(R.id.uc_et_venue).text.toString()
                val updatedField1 = view.findViewById<AppCompatEditText>(R.id.uc_tv_field1).text.toString()
                val updatedField2 = view.findViewById<AppCompatEditText>(R.id.uc_tv_field2).text.toString()
                val updatedField3 = view.findViewById<AppCompatEditText>(R.id.uc_tv_field3).text.toString()
                val updatedLeftField1 =
                    view.findViewById<AppCompatEditText>(R.id.uc_tv_leftField1).text.toString()
                val updatedLeftField2 =
                    view.findViewById<AppCompatEditText>(R.id.uc_tv_leftField2).text.toString()
                val updatedLeftField3 =
                    view.findViewById<AppCompatEditText>(R.id.uc_tv_leftField3).text.toString()
                val updatedRightField1 =
                    view.findViewById<AppCompatEditText>(R.id.uc_tv_rightField1).text.toString()
                val updatedRightField2 =
                    view.findViewById<AppCompatEditText>(R.id.uc_tv_rightField2).text.toString()
                val updatedRightField3 =
                    view.findViewById<AppCompatEditText>(R.id.uc_tv_rightField3).text.toString()
                val newData = MatchData(
                    key = matchData.key,
                    league = updatedLeague,
                    time = updatedTime,
                    date = updatedDate,
                    venue = updatedVenue,
                    teamAname = spinner1.selectedItem.toString(),
                    teamBname = spinner2.selectedItem.toString(),
                    score = ScoreData(
                        field1 = updatedField1,
                        field2 = updatedField2,
                        field3 = updatedField3,
                        leftField1 = updatedLeftField1,
                        leftField2 = updatedLeftField2,
                        leftField3 = updatedLeftField3,
                        rightField1 = updatedRightField1,
                        rightField2 = updatedRightField2,
                        rightField3 = updatedRightField3,

                    ),
                    teamAImage = data[spinner1.selectedItem.toString()] ?: "emrale",
                    teamBImage = data[spinner2.selectedItem.toString()] ?: "malli emarale"
                )
                Log.i("data before images", newData.toString())
                btmDialog.dismiss()
                viewModel.update(args.fragment, newData)
            }
            delete.setOnClickListener {
                viewModel.delete(args.fragment, matchData.key)
                btmDialog.dismiss()
            }
            btmDialog.setCancelable(false)
            btmDialog.setContentView(view)
            btmDialog.show()
        }



    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


interface OnFieldUpdateListener {
    fun onUpdateField(fragment: String, cardKey: String, fieldUpdated: String, updatedValue: String)
    fun openDialog(matchData: MatchData)
}
