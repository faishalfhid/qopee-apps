package com.dicoding.tugasakhircompose.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dicoding.tugasakhircompose.R
import com.dicoding.tugasakhircompose.model.OrderFood
import com.dicoding.tugasakhircompose.ui.common.UiState
import com.dicoding.tugasakhircompose.ui.components.OrderButton
import com.dicoding.tugasakhircompose.ui.components.ProductCounter

@Composable
fun FoodDetailScreen(
    viewModel: DetailViewModel,
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var orderCount by rememberSaveable { mutableStateOf(1) }

    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            val orderFood = (uiState as UiState.Success<OrderFood>).data
            val foodName = orderFood.food.name ?: stringResource(R.string.default_food_name)
            val foodPhotoUrl = orderFood.food.photoUrl ?: ""
            val basePrice = orderFood.food.price ?: 0
            val detailFood = orderFood.food.detail ?: stringResource(R.string.default_food_name)
            val foodId = orderFood.food.id ?: 0


            Column(modifier = modifier) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = foodPhotoUrl,
                            contentDescription = foodName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(400.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { navigateBack() }
                                .align(Alignment.TopStart)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = foodName,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                        )
                        Text(
                            text = stringResource(R.string.required_point, basePrice),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = Color.White
                        )
                        Text(
                            text = detailFood,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify,
                        )
                    }
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ProductCounter(
                        orderId = foodId,
                        orderCount = orderCount,
                        onProductIncreased = { orderCount++ },
                        onProductDecreased = { if (orderCount > 0) orderCount-- },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    )
                    val totalPrice = basePrice * orderCount
                    OrderButton(
                        text = stringResource(R.string.add_to_cart, totalPrice),
                        enabled = orderCount > 0,
                        onClick = {
                            viewModel.addToCart(orderFood.food, orderCount)
                            navigateToCart()
                        }
                    )
                }
            }
        }
        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.error_loading_food),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}