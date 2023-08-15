package com.example.caseapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caseapp.domain.model.BaseUIModel
import com.example.caseapp.domain.model.Loading
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.domain.useCase.GetArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: GetArticleUseCase) : ViewModel() {

    private val _articles = MutableStateFlow<BaseUIModel<List<ArticleUIModel>>>(Loading())
    val articles: StateFlow<BaseUIModel<List<ArticleUIModel>>> get() = _articles

    init {
         getArticles(null, null)
    }

    fun getArticles(startDate: Date?, endDate: Date?) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase(startDate, endDate).collect {
                _articles.emit(it)
            }
        }
    }
}