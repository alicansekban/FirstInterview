package com.example.caseapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caseapp.data.local.dbModel.ArticleEntity
import com.example.caseapp.domain.BaseUIModel
import com.example.caseapp.domain.Loading
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.domain.useCase.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: GetDataUseCase) : ViewModel() {

    private val _articles = MutableStateFlow<BaseUIModel<List<ArticleUIModel>>>(Loading())
    val articles: StateFlow<BaseUIModel<List<ArticleUIModel>>> get() = _articles

    init {
         getArticles(null, null)
    }

    fun getArticles(startDate: Date?, endDate: Date?) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase(startDate, endDate).collectLatest {
                _articles.emit(it)
            }
        }
    }
}