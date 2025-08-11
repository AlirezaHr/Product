package com.alirezahr.products.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alirezahr.products.domain.model.Product
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailProductScreen(
    detailProductViewModel: DetailProductViewModel = hiltViewModel(),
    productId: Int,
    onBackClicked: () -> Unit
) {
    val state by detailProductViewModel.state.collectAsState()

    LaunchedEffect(state.onBackClicked) {
        if (state.onBackClicked) {
            onBackClicked()
        }
    }

    LaunchedEffect(Unit) {
        detailProductViewModel.handleIntent(DetailProductIntent.LoadDetailProduct(productId))
    }


    state.product?.let { product ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item{Spacer(modifier = Modifier.height(56.dp)) }
            item {
                AppBar(
                    product = product,
                    onBackClicked = { detailProductViewModel.handleIntent(DetailProductIntent.OnBackClick) },
                    onFavClicked = { detailProductViewModel.handleIntent(DetailProductIntent.BookMarkClick) }
                )
            }

            item {
                GlideImage(
                    model = product.image,
                    contentDescription = "Product Image",
                modifier = Modifier.fillMaxWidth()
                )
            }
            item{Spacer(modifier = Modifier.height(8.dp))}

            item{ContentDetailProduct(product) {
                detailProductViewModel.handleIntent(DetailProductIntent.BookMarkClick)
            }}
        }
    }
}

@Composable
fun AppBar(product: Product, onBackClicked: () -> Unit, onFavClicked: () -> Unit) {
    Row(modifier = Modifier.padding(22.dp)) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(34.dp)
                .clickable { onBackClicked() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = if (product.isBookMark) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Toggle Bookmark",
            tint = Color.Red,
            modifier = Modifier
                .size(32.dp)
                .clickable { onFavClicked() }
        )
    }
}

@Composable
fun ContentDetailProduct(product: Product, onBookMarkClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.briefTitle(16),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.weight(1f))


        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$${product.price}",
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


            Text(
                text = product.category,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Description",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = product.description)
    }
}