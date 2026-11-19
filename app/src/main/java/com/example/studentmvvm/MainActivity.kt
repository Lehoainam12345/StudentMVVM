
package com.example.studentmvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.studentmvvm.data.AppDatabase
import com.example.studentmvvm.repository.StudentRepository
import com.example.studentmvvm.ui.StudentListFragment
import com.example.studentmvvm.viewmodel.StudentViewModelFactory

/**
 * MainActivity đóng vai trò Host Container.
 *
 * Kiến trúc:
 * UI -> ViewModel -> Repository -> Room / REST API
 */
class MainActivity : AppCompatActivity() {

    // 1. Khởi tạo Room Database
    private val database by lazy {
        AppDatabase.getDatabase(applicationContext)
    }

    // 2. Khởi tạo Repository
    private val repository by lazy {
        StudentRepository(database.studentDao())
    }

    // 3. Khởi tạo ViewModelFactory
    private val studentViewModelFactory by lazy {
        StudentViewModelFactory(repository)
    }

    /**
     * Cung cấp Factory cho các Fragment.
     *
     * StudentListFragment và AddStudentDialogFragment
     * cùng sử dụng activityViewModels().
     */
    override val defaultViewModelProviderFactory:
            ViewModelProvider.Factory
        get() = studentViewModelFactory

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        // 4. Hiển thị layout chính
        setContentView(R.layout.activity_main)

        // 5. Xử lý khoảng trống thanh trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.fragment_container_view)
        ) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // 6. Hiển thị StudentListFragment
        if (savedInstanceState == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container_view,
                    StudentListFragment()
                )
                .commit()
        }
    }
}
