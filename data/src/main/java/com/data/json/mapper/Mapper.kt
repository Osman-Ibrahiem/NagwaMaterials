package com.data.json.mapper

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <JSON> the remote model input type
 * @param <ENTITY> the entity model output type
 */
interface Mapper<in JSON, out ENTITY> {

    fun mapFromJson(type: JSON): ENTITY
}
