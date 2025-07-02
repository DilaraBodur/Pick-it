package de.syntax_institut.androidabschlussprojekt.features.user.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax_institut.androidabschlussprojekt.features.auth.service.AuthService
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.data.remote.SymbolsApi
import de.syntax_institut.androidabschlussprojekt.features.game.data.repositories.SymbolsRepository
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.InventoryViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.ShopViewModel
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UsernameRepository
import de.syntax_institut.androidabschlussprojekt.features.user.remote.UsernameApi
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val appModule = module {
    single { AuthService() }
    single { UserRepository() }

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        OkHttpClient.Builder().build()
    }

    single<UsernameApi> {
        Retrofit.Builder()
            .baseUrl("https://usernameapiv1.vercel.app/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
            .create(UsernameApi::class.java)
    }

    single { UsernameRepository(get()) }

    single<SymbolsApi> {
        Retrofit.Builder()
            .baseUrl("https://pick-it-6d499.web.app/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
            .create(SymbolsApi::class.java)
    }

    single { SymbolsRepository(get()) }

    viewModel {
        AuthViewModel(get(), get(), get())
    }

    viewModel {
        ShopViewModel(
            symbolsRepository = get(),
            userRepository = get(),
            authViewModel = get()
        )
    }

    viewModel {
        InventoryViewModel(get(), get())
    }
}
