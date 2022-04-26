package com.osman.materials.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import com.domain.common.model.FileStatus
import com.domain.common.model.FileType
import com.domain.common.model.MaterialFile
import com.domain.common.viewstate.DownloadFileStateResult
import com.domain.common.viewstate.FilesStateResult
import com.domain.core.viewstate.BaseVS
import com.domain.core.viewstate.Empty
import com.domain.core.viewstate.Failure
import com.domain.core.viewstate.Loading
import com.osman.materials.R
import com.osman.materials.base.BaseFragment
import com.osman.materials.databinding.FragmentHomeBinding
import com.osman.materials.ui.home.adapter.MaterialsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, HomeIntent>() {

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

        binding.swipeRefresh.setOnRefreshListener(::onRefresh)
        onRefresh()
    }

    private fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        sendIntent(HomeIntent.GetAllFiles)
    }

    private fun onMaterialClickListener(position: Int, file: MaterialFile) {
        if (file.status is FileStatus.Downloaded) {
            val mimeType = when (file.type) {
                is FileType.PDF -> "application/pdf"
                is FileType.VIDEO -> "video/mp4"
                else -> ""
            }
            if (mimeType.isEmpty()) return

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(file.url.toUri(), mimeType)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        } else if (file.status is FileStatus.Idle) {
            if (file.type is FileType.PDF) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://docs.google.com/gview?embedded=true&url=${file.url}".toUri()
                )
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            } else if (file.type is FileType.VIDEO) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(file.url.toUri(), "video/mp4")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
        }
    }

    private fun onMaterialDownloadClickListener(position: Int, file: MaterialFile) {
        sendIntent(HomeIntent.DownloadFile(file))
    }

    override fun render(state: BaseVS) {
        super.render(state)

        when (state) {
            is Loading -> {
                if (state.type == FilesStateResult.TYPE) {
                    binding.swipeRefresh.isRefreshing = true
                } else {

                }
            }

            is Failure -> {
                if (state.type == FilesStateResult.TYPE) {
                    binding.swipeRefresh.isRefreshing = false
                    binding.errorMsgItem.text = state.message
                    binding.recycler.visibility = View.GONE
                    binding.errorMsgItem.visibility = View.VISIBLE
                    binding.retryBtn.visibility = View.VISIBLE
                } else {

                }

            }

            is Empty -> {
                binding.swipeRefresh.isRefreshing = false
                binding.errorMsgItem.setText(R.string.no_data)
                binding.recycler.visibility = View.GONE
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.retryBtn.visibility = View.GONE
            }

            is FilesStateResult -> {
                binding.swipeRefresh.isRefreshing = false
                materialsAdapter.values = state.files.toMutableList()
                binding.recycler.visibility = View.VISIBLE
                binding.errorMsgItem.visibility = View.GONE
                binding.retryBtn.visibility = View.GONE
            }

            is DownloadFileStateResult -> {
                materialsAdapter.updateFile(state.file)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}