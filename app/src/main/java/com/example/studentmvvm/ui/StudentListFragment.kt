
package com.example.studentmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.studentmvvm.R
import com.example.studentmvvm.databinding.FragmentStudentListBinding
import com.example.studentmvvm.model.Student
import com.example.studentmvvm.ui.adapter.StudentAdapter
import com.example.studentmvvm.viewmodel.StudentViewModel

/**
 * Fragment hiển thị danh sách sinh viên.
 * Sử dụng MVVM + Room + LiveData.
 */
class StudentListFragment : Fragment() {

    // Backing Property chống rò rỉ bộ nhớ
    private var _binding: FragmentStudentListBinding? = null

    private val binding
        get() = _binding!!

    // Chia sẻ ViewModel với MainActivity
    private val viewModel: StudentViewModel by activityViewModels()

    private lateinit var studentAdapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStudentListBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupListeners()
    }

    /**
     * Cấu hình RecyclerView.
     */
    private fun setupRecyclerView() {

        studentAdapter = StudentAdapter(
            onStudentClick = { student ->
                showStudentDetailDialog(student)
            },

            onDeleteClick = { student ->
                confirmDelete(student)
            }
        )

        binding.rvStudents.apply {
            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = studentAdapter
            setHasFixedSize(true)
        }
    }

    /**
     * Quan sát dữ liệu từ ViewModel.
     */
    private fun observeViewModel() {

        viewModel.allStudents.observe(
            viewLifecycleOwner
        ) { studentList ->

            studentAdapter.submitList(studentList)

            // Cập nhật số lượng sinh viên
            binding.tvStudentCountBadge.text =
                "${studentList.size} sinh viên"

            // Hiển thị trạng thái danh sách trống
            binding.layoutEmpty.visibility =
                if (studentList.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        // Hiển thị thông báo từ ViewModel
        viewModel.statusMessage.observe(
            viewLifecycleOwner
        ) { message ->

            if (!message.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Xử lý sự kiện người dùng.
     */
    private fun setupListeners() {

        // Nhấn nút + để thêm sinh viên
        binding.fabAdd.setOnClickListener {

            AddStudentDialogFragment().show(
                childFragmentManager,
                "ADD_STUDENT_DIALOG"
            )
        }

        // Đồng bộ dữ liệu từ REST API
        binding.toolbar.setOnMenuItemClickListener {
                menuItem ->

            when (menuItem.itemId) {

                R.id.action_sync -> {
                    viewModel.syncFromCloud()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * Hiển thị hộp thoại xác nhận xóa.
     */
    private fun confirmDelete(student: Student) {

        AlertDialog.Builder(requireContext())
            .setTitle("Xác Nhận Xóa")
            .setMessage(
                "Bạn có chắc muốn xóa sinh viên " +
                        "${student.fullName} khỏi CSDL Room?"
            )
            .setPositiveButton("Xóa") { _, _ ->

                viewModel.delete(student)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    /**
     * Xem thông tin chi tiết sinh viên.
     */
    private fun showStudentDetailDialog(
        student: Student
    ) {

        val information = """
            ID: ${student.id}
            Họ tên: ${student.fullName}
            MSSV: ${student.studentCode}
            Email: ${student.email}
            GPA: ${student.gpa}
            Ngành: ${student.major}
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Thông Tin Sinh Viên")
            .setMessage(information)
            .setPositiveButton("Đóng", null)
            .show()
    }

    /**
     * Giải phóng ViewBinding khi View bị hủy.
     */
    override fun onDestroyView() {

        binding.rvStudents.adapter = null
        _binding = null

        super.onDestroyView()
    }
}
