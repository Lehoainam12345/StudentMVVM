
package com.example.studentmvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.studentmvvm.databinding.ItemStudentRoomBinding
import com.example.studentmvvm.model.Student

import java.util.Locale

/**
 * StudentAdapter hiển thị danh sách sinh viên.
 * Sử dụng ListAdapter + DiffUtil để tối ưu
 * cập nhật giao diện RecyclerView.
 */
class StudentAdapter(
    private val onStudentClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(
    StudentDiffCallback()
) {

    /**
     * Tạo ViewHolder từ layout XML.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentViewHolder {

        val binding = ItemStudentRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return StudentViewHolder(binding)
    }

    /**
     * Gắn dữ liệu sinh viên vào ViewHolder.
     */
    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder quản lý giao diện của một sinh viên.
     */
    inner class StudentViewHolder(
        private val binding: ItemStudentRoomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {

            // Hiển thị họ tên
            binding.tvFullName.text = student.fullName

            // Hiển thị mã sinh viên
            binding.tvStudentCode.text =
                "MSSV: ${student.studentCode}"

            // Hiển thị Email
            binding.tvEmail.text = student.email

            // Hiển thị ngành học
            binding.tvMajor.text = student.major

            // Hiển thị điểm GPA
            binding.tvGpa.text = "GPA: ${
                String.format(
                    Locale.US,
                    "%.2f",
                    student.gpa
                )
            }"

            // Avatar: Chữ cái đầu của tên
            binding.tvAvatar.text =
                student.fullName.trim()
                    .take(1)
                    .uppercase()

            // Nhấn vào sinh viên để xem chi tiết
            binding.root.setOnClickListener {
                onStudentClick(student)
            }

            // Nhấn vào nút xóa
            binding.btnDelete.setOnClickListener {
                onDeleteClick(student)
            }
        }
    }

    /**
     * DiffUtil giúp RecyclerView chỉ cập nhật
     * những dòng có thay đổi dữ liệu.
     */
    class StudentDiffCallback :
        DiffUtil.ItemCallback<Student>() {

        override fun areItemsTheSame(
            oldItem: Student,
            newItem: Student
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Student,
            newItem: Student
        ): Boolean {
            return oldItem == newItem
        }
    }
}
