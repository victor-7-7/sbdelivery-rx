package ru.skillbranch.sbdelivery.core.notifier.event

sealed class BasketEvent {
    data class AddDish(val id: String, val title: String, val price: String) : BasketEvent()
}