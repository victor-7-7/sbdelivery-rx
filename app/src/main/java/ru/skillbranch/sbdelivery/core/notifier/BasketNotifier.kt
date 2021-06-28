package ru.skillbranch.sbdelivery.core.notifier

import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent

interface BasketNotifier {
    fun eventSubscribe(): Observable<BasketEvent>
    fun putDishes(dish: BasketEvent.AddDish)
}