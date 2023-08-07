package com.example.caseapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caseapp.domain.Error
import com.example.caseapp.domain.Loading
import com.example.caseapp.domain.Success
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.ui.home.TopBar
import com.example.caseapp.ui.home.loadImage
import com.example.caseapp.utils.toParsedString

@Composable
fun DetailScreen(
    onBackPressed: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val article by viewModel.article.collectAsStateWithLifecycle()

    when (article) {
        is Error -> {}
        is Loading -> {}
        is Success -> {
            val response = (article as Success<ArticleUIModel>).response
            StatelessDetailScreen(article = response, onBackPressed = {
                onBackPressed(it)
            })

        }
    }
}

@Composable
fun StatelessDetailScreen(article: ArticleUIModel,onBackPressed: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(
            title = article.title.toString(),
            showBackButton = true,
            onBackClick = {onBackPressed("-1") },
            showFilterButton = false,
            onFavoriteClick = {}
        )
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            loadImage(
                url = article.urlToImage ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.title ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.publishedAt?.toParsedString() ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.content ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }

}