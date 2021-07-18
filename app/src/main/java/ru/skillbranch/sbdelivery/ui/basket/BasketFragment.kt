package ru.skillbranch.sbdelivery.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.android.ext.android.inject
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifier
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent
import ru.skillbranch.sbdelivery.databinding.FragmentBasketBinding

class BasketFragment : Fragment() {
    companion object {
        fun newInstance() = BasketFragment()
    }

    private lateinit var ds: Disposable

    private val notifier: BasketNotifier by inject()

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ds = notifier.eventSubscribe()
            .subscribe {
                if (it is BasketEvent.AddDish) {
                    val str = "${binding.tvDishes.text}\n\n ${it.title} стоимость ${it.price}"
                    binding.tvDishes.text = str
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!ds.isDisposed) ds.dispose()
    }
}