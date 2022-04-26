package com.osman.materials.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domain.common.model.FileStatus
import com.domain.common.model.FileType
import com.domain.common.model.MaterialFile
import com.osman.customviews.DownloadButtonProgress
import com.osman.materials.R
import com.osman.materials.databinding.ItemFileBinding
import com.osman.materials.di.GlideApp

class MaterialsAdapter(
    private val onItemDownloadClick: (position: Int, file: MaterialFile) -> Unit,
    private val onItemClick: (position: Int, file: MaterialFile) -> Unit,
) : RecyclerView.Adapter<MaterialsAdapter.MaterialViewHolder>() {

    var filterType: FileType = FileType.UNKNOWN
        set(value) {
            field = value
            filteredValues = values.filterTo(ArrayList()) {
                when (value) {
                    is FileType.PDF -> it.type == FileType.PDF
                    is FileType.VIDEO -> it.type == FileType.VIDEO
                    is FileType.UNKNOWN -> it.type == FileType.VIDEO || it.type == FileType.PDF
                }
            }
        }

    var values: MutableList<MaterialFile> = ArrayList()
        set(value) {
            field = value
            filteredValues = value.filterTo(ArrayList()) {
                when (filterType) {
                    is FileType.PDF -> it.type == FileType.PDF
                    is FileType.VIDEO -> it.type == FileType.VIDEO
                    is FileType.UNKNOWN -> it.type == FileType.VIDEO || it.type == FileType.PDF
                }
            }
        }

    private var filteredValues: MutableList<MaterialFile> = ArrayList()
        set(value) {
            val oldList = ArrayList(field)
            field.clear()
            field.addAll(value)
            val newList = ArrayList(field)
            val diffCallback = MaterialsDiffCallback(oldList, newList)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
        }

    fun updateFile(file: MaterialFile) {
        filteredValues.forEachIndexed { index, materialFile ->
            if (materialFile.id == file.id) {
                filteredValues[index] = file
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        return MaterialViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = filteredValues[position]
        holder.binding.rootView.setOnClickListener { onItemClick(position, material) }

        holder.binding.icon.setImageResource(
            when (material.type) {
                is FileType.PDF -> R.drawable.ic_file_pdf
                is FileType.VIDEO -> R.drawable.ic_file_video
                else -> R.drawable.logo_small
            }
        )

        val context = holder.itemView.context
        GlideApp.with(context).load(material.url).into(holder.binding.thumbnail)

        holder.binding.title.text = material.title

        when (val status = material.status) {
            FileStatus.Idle -> {
                holder.binding.btnDownload.setIdle()
                holder.binding.downloadProgress.text = "0%"
                holder.binding.downloadProgress.visibility = View.GONE
            }
            is FileStatus.Ready -> {
                holder.binding.btnDownload.setIndeterminate()
                holder.binding.downloadProgress.text = "0%"
                holder.binding.downloadProgress.visibility = View.VISIBLE
            }
            is FileStatus.Downloading -> {
                val progress = status.progress
                holder.binding.btnDownload.setDeterminate()
                holder.binding.btnDownload.currentProgress = progress
                holder.binding.downloadProgress.text = "$progress%"
                holder.binding.downloadProgress.visibility = View.VISIBLE
            }
            is FileStatus.Downloaded -> {
                holder.binding.btnDownload.setFinish()
                holder.binding.downloadProgress.text = "100%"
                holder.binding.downloadProgress.visibility = View.GONE
            }
            is FileStatus.Error -> {
                holder.binding.btnDownload.setIdle()
                holder.binding.downloadProgress.text = "0%"
                holder.binding.downloadProgress.visibility = View.GONE
            }
        }

        holder.binding.btnDownload.addOnClickListener(object :
            DownloadButtonProgress.OnClickListener {
            override fun onIdleButtonClick(view: View?) {
                holder.binding.btnDownload.setIndeterminate()
                onItemDownloadClick(holder.adapterPosition, material)
            }

            override fun onCancelButtonClick(view: View?) {

            }

            override fun onFinishButtonClick(view: View?) {
            }

        })
    }

    override fun getItemCount(): Int = filteredValues.size

    class MaterialViewHolder(val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): MaterialViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemFileBinding.inflate(inflater, parent, false)
                return MaterialViewHolder(binding)
            }
        }
    }

    private class MaterialsDiffCallback(
        private val oldList: List<MaterialFile>,
        private val newList: List<MaterialFile>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] === newList[newPosition]
        }

        @Nullable
        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return super.getChangePayload(oldPosition, newPosition)
        }
    }
}