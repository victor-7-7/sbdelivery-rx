package ru.skillbranch.sbdelivery.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.core.BaseViewModel
import ru.skillbranch.sbdelivery.core.adapter.ProductItemState
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifier
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent
import ru.skillbranch.sbdelivery.domain.SearchUseCase
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val useCase: SearchUseCase,
    private val mapper: DishesMapper,
    private val notifier: BasketNotifier
) : BaseViewModel() {
    private val action = MutableLiveData<SearchState>()
    val state: LiveData<SearchState>
        get() = action

    fun initState() {
        // Извлекаем блюда из БД девайса
        useCase.getDishes()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { action.value = SearchState.Loading }
            .observeOn(Schedulers.io())
            // Иммитируем задержку при загрузке для тестов
            .delay(800L, TimeUnit.MILLISECONDS)
            .map { dishes -> mapper.mapDtoToState(dishes) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val newState = SearchState.Result(it)
                action.value = newState
            }, {
                if (it is EmptyDishesError) {
                    // Для прохождения теста
                    // `when use case error data should value return in SearchState Error`
                    action.value = SearchState.Error(it.messageDishes)
                } else {
                    action.value = SearchState.Error("Что то пошло не по плану")
                }
                it.printStackTrace()
            }).track()
    }

    // Метод вызывается один раз при создании search-фрагмента.
    // При изменении текста в поисковой строке объект searchEvent
    // шлет нотификации подписчику
    fun setSearchEvent(searchEvent: Observable<String>) {
        // https://startandroid.ru/ru/blog/516-rxjava-primery.html#id600
        searchEvent
            .debounce(800L, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                action.value = SearchState.Loading
            }
            .observeOn(Schedulers.io())
            .switchMap { useCase.findDishesByName(it) }
            .delay(800L, TimeUnit.MILLISECONDS)
            .map { mapper.mapDtoToState(it) }
            .observeOn(AndroidSchedulers.mainThread())
            /*.map {
                Log.d("XXX", "call map link: $it")
                if (it.isEmpty()) {
                    // После ошибки источник перестает работать
                    throw EmptyDishesError("Данные не получены")
                }
                else it
            }*/
            .subscribe({
                if (it.isEmpty()) {
                    action.value = SearchState.Empty
                } else {
                    val newState = SearchState.Result(it)
                    action.value = newState
                }
                /*val newState = SearchState.Result(it)
                action.value = newState*/

            }, {
                if (it is EmptyDishesError) {
                    action.value = SearchState.Error(it.messageDishes)
                } else {
                    action.value = SearchState.Error("Что то пошло не по плану")
                }
                it.printStackTrace()
            }).track()
    }

    fun handleAddBasket(item: ProductItemState) {
        notifier.putDishes(BasketEvent.AddDish(item.id, item.title, item.price))
    }
    // blockingSubscribe vs subscribe
    // https://stackoverflow.com/questions/54128079/rxjava2-blockingsubscribe-vs-subscribe

}