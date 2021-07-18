package ru.skillbranch.sbdelivery.ui.search

import ru.skillbranch.sbdelivery.core.adapter.ProductItemState

sealed class SearchState {
    object Loading : SearchState()
    object Empty : SearchState()
    data class Error(val errorDescription: String) : SearchState()
    data class Result(
        val productItems: List<ProductItemState>
    ) : SearchState()
}