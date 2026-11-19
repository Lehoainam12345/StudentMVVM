
package com.example.studentmvvm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit Singleton dùng để kết nối REST API.
 */
object RetrofitClient {

    private const val BASE_URL =
        "https://jsonplaceholder.typicode.com/"

    val apiService: StudentApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(StudentApiService::class.java)
    }
}
