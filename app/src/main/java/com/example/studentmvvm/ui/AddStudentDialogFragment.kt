
package com.example.studentmvvm.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

import com.example.studentmvvm.databinding.DialogAddStudentBinding
import com.example.studentmvvm.model.Student
import com.example.studentmvvm.viewmodel.StudentViewModel

/**
 * Hộp thoại thêm sinh viên mới.
 */
class AddStudentDialogFragment : DialogFragment() {

    private val viewModel: StudentViewModel by activityViewModels()

    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {

        val binding = DialogAddStudentBinding.inflate(
            LayoutInflater.from(requireContext())
        )

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Thêm Sinh Viên Mới")
            .setView(binding.root)
            .setPositiveButton("Lưu vào Room", null)
            .setNegativeButton("Hủy", null)
            .create()

        dialog.setOnShowListener {

            dialog.getButton(
                AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener {

                // Đọc dữ liệu nhập
                val name = binding.etFullName.text
                    .toString().trim()

                val code = binding.etStudentCode.text
                    .toString().trim()

                val email = binding.etEmail.text
                    .toString().trim()

                val gpaText = binding.etGpa.text
                    .toString().trim()

                // Xóa thông báo lỗi cũ
                binding.tilFullName.error = null
                binding.tilStudentCode.error = null
                binding.tilGpa.error = null

                // Kiểm tra họ tên
                if (name.isEmpty()) {

                    binding.tilFullName.error =
                        "Vui lòng nhập họ tên"

                    return@setOnClickListener
                }

                // Kiểm tra MSSV
                if (code.isEmpty()) {

                    binding.tilStudentCode.error =
                        "Vui lòng nhập mã sinh viên"

                    return@setOnClickListener
                }

                // Kiểm tra GPA
                val parsedGpa = if (gpaText.isEmpty()) {
                    0.0
                } else {
                    gpaText.toDoubleOrNull()
                }

                if (parsedGpa == null) {

                    binding.tilGpa.error =
                        "Điểm GPA không hợp lệ"

                    return@setOnClickListener
                }

                // Tạo đối tượng sinh viên
                val student = Student(
                    fullName = name,
                    studentCode = code,

                    email = if (email.isNotEmpty()) {
                        email
                    } else {
                        "${code.lowercase()}@ute.udn.vn"
                    },

                    gpa = parsedGpa,
                    major = "Công Nghệ Thông Tin"
                )

                // Gọi ViewModel lưu dữ liệu vào Room
                viewModel.insert(student)

                // Đóng hộp thoại
                dialog.dismiss()
            }
        }

        return dialog
    }
}
