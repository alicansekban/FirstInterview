package com.example.caseapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caseapp.domain.model.BaseUIModel
import com.example.caseapp.domain.model.Loading
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.domain.useCase.GetArticleDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailUseCase: GetArticleDetailUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _article = MutableStateFlow<BaseUIModel<ArticleUIModel>>(Loading())
    val article: StateFlow<BaseUIModel<ArticleUIModel>> get() = _article

    val id = checkNotNull(savedStateHandle.get<String>("id"))
    init {
        // savedStateHandle aracılığı ile argümana ulaşıp veri istek işlemini buradan yönetiyoruz, ui'a sadece sonucu gönderiyoruz.
        getArticleDetail(id.toInt())
    }

    private fun getArticleDetail(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            detailUseCase(id).collectLatest {
                _article.emit(it)
            }
        }
    }

}