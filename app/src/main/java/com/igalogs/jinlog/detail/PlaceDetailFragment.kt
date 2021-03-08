package com.igalogs.jinlog.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.igalogs.jinlog.R
import com.igalogs.jinlog.databinding.FragmentPlaceDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.abs


@AndroidEntryPoint
class PlaceDetailFragment : Fragment() {

    private var placeId: String? = null

    private val viewModel: PlaceDetailViewModel by viewModels()

    private lateinit var binding: FragmentPlaceDetailBinding

    private val listItemController by lazy { PlaceDetailItemController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeId = it.getString(ARG_PLACE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_place_detail, container, false)
        binding = FragmentPlaceDetailBinding.bind(view).also {
            it.viewModel = viewModel
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupToolbar()
        setupRecyclerView()

        if (placeId != null) {
            viewModel.load(placeId!!)
        }
    }

    private fun setupRecyclerView() {
        val spanCount = 2
        binding.recyclerView.apply {
            this.adapter = listItemController.adapter
//            this.addItemDecoration(
//                DividerItemDecoration(
//                    requireContext(),
//                    DividerItemDecoration.VERTICAL
//                )
//            )
            this.layoutManager = GridLayoutManager(requireContext(), spanCount).apply {
                listItemController.spanCount = spanCount
                spanSizeLookup = listItemController.spanSizeLookup
                setItemSpacingDp(6)
            }

//            this.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            viewModel.log.observe(viewLifecycleOwner, Observer {
                listItemController.setData(it)
            })
        }

    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.toolBar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
        }

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val per = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            binding.title.alpha = per
        })

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val ARG_PLACE_ID = "place_id"

        @JvmStatic
        fun newInstance(placeId: String) =
            PlaceDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PLACE_ID, placeId)
                }
            }
    }
}