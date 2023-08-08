@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.example.caseapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.caseapp.R
import com.example.caseapp.domain.Error
import com.example.caseapp.domain.Loading
import com.example.caseapp.domain.Success
import com.example.caseapp.domain.model.ArticleUIModel
import com.example.caseapp.utils.ScreenRoutes
import com.example.caseapp.utils.heightPercent
import com.example.caseapp.utils.toParsedString
import com.example.caseapp.utils.widthPercent
import java.lang.reflect.Type
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen(
    openDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val articles by viewModel.articles.collectAsStateWithLifecycle()

    when (articles) {
        is Error -> {
            LoadingDialog(isShowingDialog = false)
            Toast.makeText(
                context,
                (articles as Error<List<ArticleUIModel>>).errorMessage ?: "error",
                Toast.LENGTH_LONG
            ).show()
            EmptyUI {
                viewModel.getArticles(null, null)
            }

        }

        is Loading -> {
            LoadingDialog(isShowingDialog = true)
        }

        is Success -> {
            LoadingDialog(isShowingDialog = false)

            val response = (articles as Success<List<ArticleUIModel>>).response
            if (response.isNotEmpty()) {
                stateLessHomeScreen(articles = response, onDateSelected = { start, end ->
                    viewModel.getArticles(start, end)
                }, openDetail = openDetail)
            } else {
                EmptyUI {
                    viewModel.getArticles(null, null)
                }
            }

        }

    }
}


@Composable
fun stateLessHomeScreen(
    articles: List<ArticleUIModel>,
    onDateSelected: (Date, Date) -> Unit,
    openDetail: (String) -> Unit
) {
    val context = LocalContext.current
    var showPicker by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier) {
        TopBar(
            title = "News Headlines",
            showBackButton = false,
            onBackClick = { },
            showFilterButton = true
        ) {
            showPicker = !showPicker
        }
        if (showPicker) {
            CreatePicker { start, end ->
                Toast.makeText(
                    context,
                    "Start Date ${start.toGMTString()} \nEnd Date ${end.toGMTString()}",
                    Toast.LENGTH_LONG
                ).show()
                onDateSelected(start, end)
                showPicker = false
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(
                items = articles,
                key = { article ->
                    article.title ?: ""
                }) { value ->
                NewsItem(value, openDetail = {
                    openDetail(
                        ScreenRoutes.Detail.replace(
                            oldValue = "{id}",
                            newValue = it
                        )
                    )
                })
            }
        }

    }
}

@Composable
fun CreatePicker(onDateSelected: (Date, Date) -> Unit) {
    val state = rememberDateRangePickerState(

    )

    if (state.selectedStartDateMillis != null && state.selectedEndDateMillis != null) {
        onDateSelected(Date(state.selectedStartDateMillis!!), Date(state.selectedEndDateMillis!!))
    }

    DateRangePicker(state = state,
        dateValidator = { current ->
            Calendar.getInstance().apply { clear(Calendar.HOUR) }.apply { clear(Calendar.MINUTE) }
                .apply { clear(Calendar.SECOND).apply { clear(Calendar.MILLISECOND) } }.timeInMillis >= current
        })

}

@Composable
fun NewsItem(article: ArticleUIModel, openDetail: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openDetail(article.id.toString()) }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            loadImage(
                url = article.urlToImage ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.title ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.description ?: "",
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = article.publishedAt?.toParsedString() ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun loadImage(url: String, modifier: Modifier) {


    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        GlideImage(
            model = url,
            contentDescription = "loadImage",
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        ) {
            it.error(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .load(url)

        }
    }
}


@Composable
fun EmptyUI(onGoBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Veriler bulunamadı.",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onGoBack() }) {
            Text(text = "Önceki Seçimlere Dön")
        }
    }
}

@Composable
fun TopBar(
    title: String,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    showFilterButton: Boolean,
    onFavoriteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Gray),
        color = White
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Black
                    )
                }
            }

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Black
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (showFilterButton) {
                IconButton(
                    onClick = { onFavoriteClick() },
                    modifier = Modifier.size(40.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Filter,
                        contentDescription = "Favorites",
                        tint = Black
                    )
                }
            }
        }
    }
}


@Composable
fun LoadingDialog(
    isShowingDialog: Boolean,
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false
) {
    if (isShowingDialog) {
        Dialog(
            onDismissRequest = { },
            DialogProperties(
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside
            )
        ) {
            DialogContent()
        }
    }
}

@Composable
fun DialogContent(
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        restartOnPlay = true,
        speed = 1f

    )
    val configuration = LocalConfiguration.current
    Card(
        modifier = Modifier
            .widthPercent(0.22f, configuration)
            .heightPercent(0.16f, configuration),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(White),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .padding(all = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Loading...",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                maxLines = 1,
            )
        }

    }
}