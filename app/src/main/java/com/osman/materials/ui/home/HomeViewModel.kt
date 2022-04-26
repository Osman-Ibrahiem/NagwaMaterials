package com.osman.materials.ui.home

import androidx.lifecycle.viewModelScope
import com.domain.common.usecase.DownloadFileUseCase
import com.domain.common.usecase.GetFilesUseCase
import com.domain.common.viewstate.DownloadFileStateResult
import com.domain.common.viewstate.FilesStateResult
import com.domain.core.viewstate.Empty
import com.domain.core.viewstate.Loading
import com.osman.materials.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val getFilesUseCase: GetFilesUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
) : BaseViewModel<HomeIntent>() {
    override fun processIntents(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetAllFiles -> getAllFiles()
            is HomeIntent.DownloadFile -> downloadFile(intent)
        }
    }

    private fun getAllFiles() {
        viewModelScope.launch {
            getFilesUseCase(
            ).onStart {
                emit(Loading())
            }.map {
                var vs = it
                if (vs is FilesStateResult && vs.files.isEmpty()) vs = Empty()
                return@map vs.apply { type = FilesStateResult.TYPE }
            }.collect(::sendState)
        }
    }

    private fun downloadFile(intent: HomeIntent.DownloadFile) {
        viewModelScope.launch {
            downloadFileUseCase(intent.materialFile
            ).onStart {
                emit(Loading())
            }.map {
                it.apply { type = DownloadFileStateResult.TYPE }
            }.collect(::sendState)
        }
    }
}