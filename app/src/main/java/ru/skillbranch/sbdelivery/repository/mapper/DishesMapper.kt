package ru.skillbranch.sbdelivery.repository.mapper

import ru.skillbranch.sbdelivery.core.adapter.ProductItemState
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.database.entity.DishPersistEntity
import ru.skillbranch.sbdelivery.repository.models.Dish

interface DishesMapper {
    fun mapDtoToState(dishEntity: List<DishEntity>): List<ProductItemState>
    fun mapDtoToEntity(dishesDto: List<Dish>): List<DishEntity>
    fun mapDtoToPersist(dishesDto: List<Dish>): List<DishPersistEntity>
    fun mapPersistToEntity(dishesPersist: List<DishPersistEntity>): List<DishEntity>
}