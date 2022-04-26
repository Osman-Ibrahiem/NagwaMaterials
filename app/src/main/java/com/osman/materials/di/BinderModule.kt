package com.osman.materials.di

import com.data.core.datasource.json.MaterialJsonDataSource
import com.data.core.datasource.remote.FileRemoteDataSource
import com.data.core.repository.MaterialsRepositoryImp
import com.data.json.source.MaterialJsonDataSourceImp
import com.data.remote.source.FileRemoteDataSourceImp
import com.domain.common.repository.MaterialsRepository
import com.domain.core.dispatchers.CoroutineDispatchers
import com.domain.core.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class BinderModule {

    @Binds
    abstract fun bindCoroutineDispatchers(coroutineDispatchersImpl: CoroutineDispatchersImpl): CoroutineDispatchers

    @Binds
    abstract fun bindMaterialsRepository(materialsRepositoryImp: MaterialsRepositoryImp): MaterialsRepository

    @Binds
    abstract fun bindMaterialJsonDataSource(materialJsonDataSourceImp: MaterialJsonDataSourceImp): MaterialJsonDataSource

    @Binds
    abstract fun bindFileRemoteDataSource(fileRemoteDataSourceImp: FileRemoteDataSourceImp): FileRemoteDataSource

}
