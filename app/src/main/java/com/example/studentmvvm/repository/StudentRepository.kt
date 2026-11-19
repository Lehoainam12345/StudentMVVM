
package com.example.studentmvvm.repository

import com.example.studentmvvm.data.StudentDao
import com.example.studentmvvm.model.Student
import com.example.studentmvvm.network.RetrofitClient
import com.example.studentmvvm.network.StudentApiService
import com.example.studentmvvm.network.toEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository là tầng trung gian giữa
 * ViewModel, Room Database và REST API.
 *
 * Single Source of Truth: UI chỉ đọc từ Room.
 */
class StudentRepository(
    private val studentDao: StudentDao,
    private val apiService: StudentApiService =
        RetrofitClient.apiService
) {

    // Quan sát danh sách sinh viên từ Room
    val allStudents: Flow<List<Student>> =
        studentDao.getAllStudents()

    // Thêm sinh viên
    suspend fun insert(student: Student): Long {
        return studentDao.insertStudent(student)
    }

    // Xóa sinh viên
    suspend fun delete(student: Student) {
        studentDao.deleteStudent(student)
    }

    // Cập nhật sinh viên
    suspend fun update(student: Student) {
        studentDao.updateStudent(student)
    }

    /**
     * Đồng bộ dữ liệu từ REST API về Room.
     */
    suspend fun syncFromCloud(): Result<Int> {
        return try {

            // Bước 1: Lấy dữ liệu trên mạng
            val remoteList = apiService.getStudents()

            // Bước 2: Chuyển DTO thành Entity
            val entities = remoteList.map {
                it.toEntity()
            }

            // Bước 3: Lưu hoặc cập nhật dữ liệu Room
            for (student in entities) {
                val existing = studentDao.getStudentByCode(
                    student.studentCode
                )

                if (existing == null) {
                    studentDao.insertStudent(student)
                } else {
                    studentDao.updateStudent(
                        student.copy(id = existing.id)
                    )
                }
            }

            // Bước 4: Trả về số bản ghi đã đồng bộ
            Result.success(entities.size)

        } catch (e: Exception) {

            // Trả về lỗi nhưng vẫn giữ dữ liệu cục bộ
            Result.failure(e)
        }
    }
}
