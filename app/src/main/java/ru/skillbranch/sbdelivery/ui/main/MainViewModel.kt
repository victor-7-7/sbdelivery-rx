package ru.skillbranch.sbdelivery.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.core.BaseViewModel
import ru.skillbranch.sbdelivery.core.adapter.CategoryItemState
import ru.skillbranch.sbdelivery.core.adapter.ProductItemState
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifier
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent
import ru.skillbranch.sbdelivery.domain.filter.CategoriesFilterUseCase
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError
import ru.skillbranch.sbdelivery.repository.mapper.CategoriesMapper
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper

class MainViewModel(
    private val repository: DishesRepositoryContract,
    private val dishesMapper: DishesMapper,
    private val categoriesMapper: CategoriesMapper,
    private val notifier: BasketNotifier
) : BaseViewModel() {

    private val defaultState = MainState.Loader
    private val action = MutableLiveData<MainState>()
    val state: LiveData<MainState>
        get() = action

    private var categorySelected = false

    init {
        loadDishes()
    }

    fun loadDishes() {
        // Получаем с сервера список всех блюд
        repository.getDishes()
            // Пока они загружаются показываем на экране кружок ProgressBar
            .doOnSubscribe { action.value = defaultState }
            // Получив список всех блюд, загружаем с сервера список категорий и
            // формируем пару из списка категорий и списка блюд
            .flatMap { dishes -> repository.getCategories().map { it to dishes } }
            // Преобразуем два списка пары в стейтовый вид (для отображения на экране)
            .map { categoriesMapper.mapDtoToState(it.first) to dishesMapper.mapDtoToState(it.second) }
            .subscribe({
                // При успешном выполнении всех операторов
                val newState = MainState.Result(it.second, it.first)
                // Обновляем стейт модели, чтобы обновился экран
                action.value = newState
            }, {
                if (it is EmptyDishesError) {
                    action.value = MainState.Error(it.messageDishes, it)
                } else {
                    action.value = MainState.Error("Что то пошло не по плану", it)
                }
                it.printStackTrace()
            }).track()
    }

    fun handleAddBasket(item: ProductItemState) {
        notifier.putDishes(BasketEvent.AddDish(item.id, item.title, item.price))
    }

    fun handleCategorySelection(categoryState: CategoryItemState) {
        // Юзер выбрал категорию из списка категорий, я фильтрую список блюд
        // и оставляю одну категорию в списке категорий. Юзер тапнул уже выбранную
        // категорию, я показываю список всех блюд и список всех категорий
        if (categorySelected) {
            // Снимаем фильтр
            categorySelected = false
            repository.getCachedDishes()
                .flatMap { dishes -> repository.getCategories().map { it to dishes } }
                .map { categoriesMapper.mapDtoToState(it.first) to dishesMapper.mapDtoToState(it.second) }
        } else {
            // Ставим фильтр
            categorySelected = true
            CategoriesFilterUseCase(repository).categoryFilterDishes(categoryState.categoryId)
                .flatMap { dishes ->
                    Single.just(listOf(categoryState) to dishesMapper.mapDtoToState(dishes))}
        }
        .subscribe({
                if (it.second.isEmpty())
                    action.value = MainState.Error("Блюда отсутствуют", EmptyDishesError())
                else
                    action.value = MainState.Result(it.second, it.first)
            }, {
                if (it is EmptyDishesError) {
                    action.value = MainState.Error(it.messageDishes, it)
                } else {
                    action.value = MainState.Error("Что то пошло не по плану", it)
                }
                it.printStackTrace()
            }).track()
    }
}