package ru.skillbranch.sbdelivery.core.notifier

import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent

class BasketNotifierImpl : BasketNotifier {
    override fun eventSubscribe(): Observable<BasketEvent> {
        TODO("Not yet implemented")
    }

    override fun putDishes(dish: BasketEvent.AddDish) {
        // TODO("Not yet implemented")
    }


}