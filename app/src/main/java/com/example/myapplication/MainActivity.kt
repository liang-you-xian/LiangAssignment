package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.Database.ApprovalDatabase
import com.example.myapplication.DatabaseApproval.ApprovalViewModel
import com.example.myapplication.DatabaseAttendance.AttendanceDatabase
import com.example.myapplication.DatabaseAttendance.AttendanceViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Approvalscreen.StaffApprovalScreen

class MainActivity : ComponentActivity() {
    private val appr by lazy {
        Room.databaseBuilder(
            applicationContext,
            ApprovalDatabase::class.java,
            "approval.appr"
        ).build()
    }
    private val atte by lazy {
        Room.databaseBuilder(
            applicationContext,
            AttendanceDatabase::class.java,
            "Attendance.atte"
        ).build()
    }
    private val approvalviewModel by viewModels<ApprovalViewModel>(
        factoryProducer = {
            object  : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ApprovalViewModel(appr.apprdao) as T
                }
            }
        }
    )
    private val attendanceviewModel by viewModels<AttendanceViewModel>(
        factoryProducer = {
            object  : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AttendanceViewModel(atte.attdao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val apprstate by approvalviewModel.state.collectAsState()
                val Attesyaye by attendanceviewModel.state.collectAsState()
                StaffApprovalScreen(state = apprstate, onEvent = approvalviewModel::onEvent)
                //app()
            }
        }
    }
}


