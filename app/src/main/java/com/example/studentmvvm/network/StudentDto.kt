
package com.example.studentmvvm.network

import com.example.studentmvvm.model.Student

/**
 * DTO nhận dữ liệu JSON từ REST API.
 */
data class StudentDto(
    val id: Int,
    val name: String,
    val email: String
)

/**
 * Chuyển dữ liệu API thành Student Entity
 * để lưu trong Room Database.
 */
fun StudentDto.toEntity(): Student {
    return Student(
        id = 0,
        fullName = name,
        studentCode = "API" + id.toString().padStart(5, '0'),
        email = email,
        gpa = 0.0,
        major = "Dữ liệu từ REST API"
    )
}
