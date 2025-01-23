package com.example.baydesigns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _selectedTool = MutableStateFlow<String?>(null) // No default tool
    val selectedTool: StateFlow<String?> get() = _selectedTool

    fun selectTool(tool: String) {
            viewModelScope.launch {
                _selectedTool.emit(tool)
            }
    }
}