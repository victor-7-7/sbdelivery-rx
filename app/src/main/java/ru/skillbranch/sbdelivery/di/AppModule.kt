package ru.skillbranch.sbdelivery.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.skillbranch.sbdelivery.core.ResourceManager
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifier
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifierImpl
import ru.skillbranch.sbdelivery.domain.SearchUseCase
import ru.skillbranch.sbdelivery.domain.SearchUseCaseImpl
import ru.skillbranch.sbdelivery.repository.DishesRepository
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.database.DatabaseProvider
import ru.skillbranch.sbdelivery.repository.database.SBDeliveryRoomDatabase
import ru.skillbranch.sbdelivery.repository.http.client.DeliveryRetrofitProvider
import ru.skillbranch.sbdelivery.repository.mapper.CategoriesMapper
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapperImpl
import ru.skillbranch.sbdelivery.ui.main.MainViewModel
import ru.skillbranch.sbdelivery.ui.search.SearchViewModel

object AppModule {
    fun appModule() = module {
        single { DeliveryRetrofitProvider.createRetrofit() }
        single<DishesRepositoryContract> { DishesRepository(api = get(), mapper = get(), dishesDao = get()) }
        single { ResourceManager(context = get()) }
        single<SearchUseCase> { SearchUseCaseImpl(get()) }
        single<DishesMapper> { DishesMapperImpl() }
        single<BasketNotifier> { BasketNotifierImpl() }
        single { CategoriesMapper() }
    }

    fun databaseModule() = module {
        single { DatabaseProvider.newInstance(context = get()) }
        single { get<SBDeliveryRoomDatabase>().dishesDao() }
    }

    fun viewModelModule() = module {
        viewModel { MainViewModel(repository = get(), dishesMapper = get(), categoriesMapper = get(), notifier = get()) }
        viewModel { SearchViewModel(useCase = get(), mapper = get()) }
    }
}