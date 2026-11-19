
package com.example.studentmvvm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.studentmvvm.model.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    // Thêm một sinh viên
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    // Thêm nhiều sinh viên
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<Student>)

    // Cập nhật sinh viên
    @Update
    suspend fun updateStudent(student: Student)

    // Xóa sinh viên
    @Delete
    suspend fun deleteStudent(student: Student)

    // Xóa theo ID
    @Query("DELETE FROM students_table WHERE id = :id")
    suspend fun deleteStudentById(id: Int)

    // Lấy toàn bộ sinh viên
    @Query("SELECT * FROM students_table ORDER BY id DESC")
    fun getAllStudents(): Flow<List<Student>>

    // Tìm sinh viên theo mã
    @Query("SELECT * FROM students_table WHERE student_code = :code LIMIT 1")
    suspend fun getStudentByCode(code: String): Student?
}
