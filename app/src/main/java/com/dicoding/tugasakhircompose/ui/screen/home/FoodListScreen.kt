package com.dicoding.tugasakhircompose.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dicoding.tugasakhircompose.R
import com.dicoding.tugasakhircompose.model.Food
import com.dicoding.tugasakhircompose.ui.common.UiState
import com.dicoding.tugasakhircompose.ui.components.FoodHeader
import com.dicoding.tugasakhircompose.ui.components.FoodListItem
import com.dicoding.tugasakhircompose.ui.components.ScrollToTopButton
import com.dicoding.tugasakhircompose.ui.components.SearchBar
import com.dicoding.tugasakhircompose.ui.theme.CoffeeBrown
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodListScreen(
    viewModel: FoodViewModel,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
) {
    val groupedFoodState by viewModel.groupedFoodState.collectAsState()
    val query by viewModel.query.collectAsState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showButton: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(query) {
        listState.scrollToItem(0)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            SearchBar(
                query = query,
                onQueryChange = viewModel::search,
                modifier = Modifier
                    .fillMaxWidth()
            )

            when (groupedFoodState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is UiState.Success -> {
                    val groupedFood = (groupedFoodState as UiState.Success<Map<Char, List<Food>>>).data
                    if (groupedFood.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = stringResource(R.string.error_message),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            groupedFood.forEach { (initial: Char, foodList: List<Food>) ->
                                stickyHeader {
                                    FoodHeader(initial)
                                }
                                items(foodList, key = { it.id }) { food ->
                                    FoodListItem(
                                        name = food.name,
                                        photoUrl = food.photoUrl,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navigateToDetail(food.id)
                                            }
                                            .animateItemPlacement(tween(durationMillis = 100))
                                    )
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = (groupedFoodState as UiState.Error).errorMessage,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(
                onClick = {
                    scope.launch {
                        listState.scrollToItem(index = 0)
                    }
                },
                buttonColor = CoffeeBrown
            )
        }

    }
}