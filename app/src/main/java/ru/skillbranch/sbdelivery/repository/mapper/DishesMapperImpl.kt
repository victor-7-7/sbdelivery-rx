package ru.skillbranch.sbdelivery.repository.mapper

import ru.skillbranch.sbdelivery.core.adapter.ProductItemState
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.database.entity.DishPersistEntity
import ru.skillbranch.sbdelivery.repository.models.Dish

open class DishesMapperImpl : DishesMapper {
    /** Объекты DishEntity из списка, полученные после после преобразования
     * данных из БД или из сети, конвертируются в объекты ProductItemState
     * для последующего отображения информации на UI */
    override fun mapDtoToState(dishEntity: List<DishEntity>): List<ProductItemState> =
        dishEntity.map {
            ProductItemState(
                id = it.id,
                image = it.image,
                price = it.price,
                title = it.title,
                categoryId = it.categoryId
            )
        }

    /** Объекты Dish из списка, основанные на полученных из сети данных о блюдах,
     * конвертируются в объекты DishEntity нового списка (с меньшим числом полей) */
    override fun mapDtoToEntity(dishesDto: List<Dish>): List<DishEntity> =
        dishesDto.map {
            DishEntity(
                it.id ?: "",
                it.category ?: "",
                it.image ?: "",
                "${it.price} ₽",
                it.name ?: ""
            )
        }

    /** Объекты Dish из списка, основанные на полученных из сети данных о блюдах,
     * конвертируются в объекты DishPersistEntity нового списка для сохранения в БД */
    override fun mapDtoToPersist(dishesDto: List<Dish>): List<DishPersistEntity> =
        dishesDto.map {
            DishPersistEntity(
                it.id ?: "",
                it.name ?: "",
                it.description ?: "",
                it.image ?: "",
                it.oldPrice ?: 0,
                it.price ?: 0L,
                it.rating ?: 0F,
                it.likes ?: 0,
                it.category ?: "",
                it.commentsCount ?: 0,
                it.active ?: false,
                it.createdAt ?: "",
                it.updatedAt ?: ""
            )
        }

    /** Объекты DishPersistEntity из списка, основанные на записях из таблицы
     * dishes_table БД девайса, конвертируются в объекты DishEntity нового списка
     * (объекты с урезанным числом полей) */
    override fun mapPersistToEntity(dishesPersist: List<DishPersistEntity>): List<DishEntity> =
        dishesPersist.map {
            DishEntity(
                it.id,
                it.category,
                it.image,
                "${it.price} ₽",
                it.name
            )
        }
}