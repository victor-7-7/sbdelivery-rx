package ru.skillbranch.sbdelivery.repository.error

class EmptyDishesError(val messageDishes: String = "") : Throwable(messageDishes) {
}