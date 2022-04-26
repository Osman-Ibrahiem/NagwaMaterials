package com.domain.common.usecase

import com.domain.common.model.MaterialFile
import com.domain.common.repository.MaterialsRepository
import com.domain.core.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DownloadFileUseCase @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val repository: MaterialsRepository,
) {

    operator fun invoke(materialFile: MaterialFile) =
        repository.downloadFile(materialFile).flowOn(dispatchers.io)
}