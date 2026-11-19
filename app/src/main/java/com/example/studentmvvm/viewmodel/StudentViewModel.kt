
package com.example.studentmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import com.example.studentmvvm.model.Student
import com.example.studentmvvm.repository.StudentRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel quản lý dữ liệu và trạng thái UI.
 * Giao tiếp với Repository thay vì truy cập
 * trực tiếp Room Database.
 */
class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    // Chuyển Flow từ Room thành LiveData
    val allStudents: LiveData<List<Student>> =
        repository.allStudents.asLiveData(Dispatchers.IO)

    // Thông báo trạng thái cho giao diện
    private val _statusMessage = MutableLiveData<String>()

    val statusMessage: LiveData<String>
        get() = _statusMessage

    /**
     * Thêm sinh viên vào Room Database.
     */
    fun insert(student: Student) =
        viewModelScope.launch(Dispatchers.IO) {

            try {
                repository.insert(student)

                _statusMessage.postValue(
                    "Đã thêm sinh viên: ${student.fullName}"
                )

            } catch (e: Exception) {

                _statusMessage.postValue(
                    "Lỗi khi thêm: ${e.message}"
                )
            }
        }

    /**
     * Xóa sinh viên khỏi Room Database.
     */
    fun delete(student: Student) =
        viewModelScope.launch(Dispatchers.IO) {

            try {
                repository.delete(student)

                _statusMessage.postValue(
                    "Đã xóa sinh viên: ${student.fullName}"
                )

            } catch (e: Exception) {

                _statusMessage.postValue(
                    "Lỗi khi xóa: ${e.message}"
                )
            }
        }

    /**
     * Đồng bộ dữ liệu từ REST API.
     */
    fun syncFromCloud() =
        viewModelScope.launch(Dispatchers.IO) {

            val result = repository.syncFromCloud()

            if (result.isSuccess) {

                _statusMessage.postValue(
                    "Đã đồng bộ ${result.getOrNull()} sinh viên từ máy chủ!"
                )

            } else {

                _statusMessage.postValue(
                    "Chưa thể kết nối máy chủ Cloud, sử dụng dữ liệu cục bộ."
                )
            }
        }
}
