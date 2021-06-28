package ru.skillbranch.sbdelivery.core.adapter

import androidx.core.view.setPadding
import coil.load
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemProductBinding

class ProductDelegate {

    fun createAdapter(addClick: (ProductItemState) -> Unit) =
        ListDelegationAdapter(productItem(addClick))


    private fun productItem(addClick: (ProductItemState) -> Unit) =
        adapterDelegateViewBinding<ProductItemState, ProductItemState, ItemProductBinding>({ layoutInflater, parent ->
            ItemProductBinding.inflate(layoutInflater, parent, false)
        }) {
            binding.btnAddBasket.setOnClickListener { addClick.invoke(item) }
            bind {
                binding.tvPrice.text = item.price
                binding.tvTitle.text = item.title
                binding.ivProductPhoto.load(item.image) {
                    error(R.drawable.img_empty_place_holder)
                    listener(onError = { _, _ ->
                        binding.ivProductPhoto.setPadding(72)
                    }, onStart = {
                        binding.ivProductPhoto.setPadding(0)
                    })
                }
            }
        }

}