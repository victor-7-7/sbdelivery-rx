package ru.skillbranch.sbdelivery.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.core.adapter.CategoriesDelegate
import ru.skillbranch.sbdelivery.core.adapter.ProductDelegate
import ru.skillbranch.sbdelivery.core.decor.GridPaddingItemDecoration
import ru.skillbranch.sbdelivery.databinding.FragmentMainBinding
import ru.skillbranch.sbdelivery.ui.basket.BasketFragment
import ru.skillbranch.sbdelivery.ui.search.SearchFragment

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()
    private val productAdapter by lazy {
        ProductDelegate().createAdapter {
            viewModel.handleAddBasket(it)
        }
    }

    private val categoriesAdapter by lazy {
        CategoriesDelegate().createAdapter {
            // TODO handle click
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        binding.rvProductGrid.adapter = productAdapter
        binding.rvCategories.adapter = categoriesAdapter
        binding.rvCategories.addItemDecoration(GridPaddingItemDecoration(17))
        binding.rvProductGrid.addItemDecoration(GridPaddingItemDecoration(17))
        binding.btnRetry.setOnClickListener {
            viewModel.loadDishes()
        }
        btnBasket.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, BasketFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun renderState(state: MainState) {
        binding.progressProduct.isVisible = state == MainState.Loader

        binding.rvProductGrid.isVisible = state is MainState.Result
        binding.rvCategories.isVisible = state is MainState.Result && state.categories.isNotEmpty()
        binding.toolbar.isVisible = state is MainState.Result
        binding.tvErrorMessage.isVisible = state is MainState.Error
        binding.btnRetry.isVisible = state is MainState.Error
        if (state is MainState.Result) {
            btnBasket.isVisible = true
            categoriesAdapter.items = state.categories
            productAdapter.items = state.productItems
            productAdapter.notifyDataSetChanged()
        } else if (state is MainState.Error) {
            binding.tvErrorMessage.text = state.message
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dishes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSearchDishes -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroyView() {
        binding.rvProductGrid.adapter = null
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
        _binding = null
        super.onDestroyView()
    }

}