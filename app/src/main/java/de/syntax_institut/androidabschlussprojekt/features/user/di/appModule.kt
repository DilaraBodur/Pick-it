package de.syntax_institut.androidabschlussprojekt.features.user.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax_institut.androidabschlussprojekt.features.user.remote.UsernameApi
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UsernameRepository
import de.syntax_institut.androidabschlussprojekt.features.auth.service.AuthService
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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

    viewModelOf(::AuthViewModel)
}
