package com.osman.materials.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.domain.model.MaterialFile
import com.osman.materials.R
import com.osman.materials.databinding.FragmentHomeBinding
import com.osman.materials.ui.home.adapter.MaterialsAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private lateinit var materialsAdapter: MaterialsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        materialsAdapter = MaterialsAdapter(
            onItemDownloadClick = ::onMaterialDownloadClickListener,
            onItemClick = ::onMaterialClickListener
        )
        with(binding.recycler) {
            val spanCount = resources.getInteger(R.integer.span_count)
            layoutManager = GridLayoutManager(context, spanCount)
            setHasFixedSize(true)
            adapter = materialsAdapter
        }
    }

    private fun onMaterialClickListener(position: Int, file: MaterialFile) {

    }

    private fun onMaterialDownloadClickListener(position: Int, file: MaterialFile) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}