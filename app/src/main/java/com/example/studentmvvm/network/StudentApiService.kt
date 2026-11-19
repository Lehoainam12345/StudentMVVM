
package com.example.studentmvvm.network

import retrofit2.http.GET

/**
 * Định nghĩa các Endpoint cho Retrofit.
 */
interface StudentApiService {

    // Lấy danh sách từ API mẫu
    @GET("users")
    suspend fun getStudents(): List<StudentDto>
}
