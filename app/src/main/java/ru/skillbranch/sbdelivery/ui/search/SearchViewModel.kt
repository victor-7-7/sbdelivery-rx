package ru.skillbranch.sbdelivery.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.core.BaseViewModel
import ru.skillbranch.sbdelivery.domain.SearchUseCase
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val useCase: SearchUseCase,
    private val mapper: DishesMapper
) : BaseViewModel() {
    private val action = MutableLiveData<SearchState>()
    val state: LiveData<SearchState>
        get() = action

    fun initState() {
        useCase.getDishes()
            .map { dishes -> mapper.mapDtoToState(dishes) }
            .subscribe({
                val newState = SearchState(it)
                action.value = newState
            }, {
                it.printStackTrace()
            }).track()
    }

    fun setSearchEvent(searchEvent: Observable<String>) {
        searchEvent
            .debounce(800L, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap { useCase.findDishesByName(it) }
            .map { mapper.mapDtoToState(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val newState = SearchState(it)
                action.value = newState
            }, {
                it.printStackTrace()
            }).track()

    }

}