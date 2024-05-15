package com.example.myapplication.DatabaseApproval

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Database.Approval
import com.example.myapplication.Database.ApprovalStaffDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ApprovalViewModel (private val dao: ApprovalStaffDao): ViewModel()
{
    private val _sortType = MutableStateFlow(SortType.Staff_Approval)
    private val _approval = _sortType
        .flatMapLatest { sortType -> when(sortType){
            SortType.Staff_Approval ->dao.getSatffApprovallist()
        } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ApprovalState())
    val state = combine(_state,_sortType,_approval){
        state,sortType,approval ->state.copy(
            sortType = sortType,
            approval = approval
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApprovalState())

    fun onEvent(event: ApprovalEvent){
        when(event){
            is ApprovalEvent.DeleteApproval -> {
                viewModelScope.launch {
                    dao.deleteApproval(event.approval)
                }

            }
            ApprovalEvent.Hidelist ->{
                _state.update { it.copy(isAddingApproval = false) }
            }

            ApprovalEvent.SaveApproval -> {
                val staffid = state.value.staffid
                val appreason = state.value.appreason
                val leaveandlate = state.value.leaveandlate
                val stateinfo = state.value.stateinfo
                val apptime = state.value.apptime
                val appdate = state.value.appdate



                if(staffid.isBlank()||appdate.isBlank()||appreason.isBlank()||
                    leaveandlate.isBlank()||stateinfo.isBlank()||apptime.isBlank()){
                    return
                }
                val approval = Approval(
                    staffid = staffid,
                    appreason = appreason,
                    leaveandlate = leaveandlate,
                    stateinfo = stateinfo,
                    apptime = apptime,
                    appdate = appdate
                )
                viewModelScope.launch {
                    dao.insertApproval(approval)
                }
                _state.update { it.copy(
                    isAddingApproval = false,
                    staffid = "",
                    appreason = "",
                    leaveandlate = "",
                    stateinfo = "",
                    apptime = "",
                    appdate = ""
                ) }
            }
            is ApprovalEvent.SetStaffApprovalList -> {
                _state.update { it.copy(staffid = event.staffid) }
            }
            is ApprovalEvent.SetAppReason -> {
                _state.update { it.copy(appreason = event.appreason) }
            }
            is ApprovalEvent.SetLeaveandLate -> {
                _state.update { it.copy(leaveandlate = event.leaveandlate) }
            }
            is ApprovalEvent.SetStateInfo -> {
                _state.update { it.copy(stateinfo = event.stateinfo) }
            }
            is ApprovalEvent.SetAppTime -> {
                _state.update { it.copy(apptime = event.apptime) }
            }
            is ApprovalEvent.SetAppDate -> {
                _state.update { it.copy(appdate = event.appdate) }
            }
            ApprovalEvent.Showlist -> {
                _state.update { it.copy(isAddingApproval = true) }
            }
            is ApprovalEvent.SortApproval -> {
                _sortType.value = event.sortType
            }
        }
    }
}