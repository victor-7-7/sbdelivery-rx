package ru.skillbranch.sbdelivery.core.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.skillbranch.sbdelivery.databinding.ItemCategoryBinding


class CategoriesDelegate {
    // https://medium.com/@cryptograph2013/перевод-статьи-великолепный-побег-джо-из-кромешного-ада-адаптеров-bb50f8389f69
    // https://github.com/sockeqwe/AdapterDelegates
    fun createAdapter(itemClick: (CategoryItemState) -> Unit) =
        ListDelegationAdapter(createCategories(itemClick))

    private fun createCategories(itemClick: (CategoryItemState) -> Unit) =
        adapterDelegateViewBinding<CategoryItemState, CategoryItemState, ItemCategoryBinding>(
            { layoutInflater, parent ->
            ItemCategoryBinding.inflate(layoutInflater, parent, false)
        }) {
            itemView.setOnClickListener { itemClick.invoke(item) }
            bind {
                binding.tvTitle.text = item.title
            }
        }
}