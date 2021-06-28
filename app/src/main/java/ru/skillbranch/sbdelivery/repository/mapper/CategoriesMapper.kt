package ru.skillbranch.sbdelivery.repository.mapper

import ru.skillbranch.sbdelivery.core.adapter.CategoryItemState
import ru.skillbranch.sbdelivery.repository.models.Category

class CategoriesMapper {

    fun mapDtoToState(dto: List<Category>): List<CategoryItemState> =
        dto.map { CategoryItemState(it.categoryId, it.name) }

}