package com.alirezahr.products.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailProductScreen(
    detailProductViewModel: DetailProductViewModel = hiltViewModel(),
    productId: Int
) {
    val state by detailProductViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        detailProductViewModel.handleIntent(DetailProductIntent.LoadDetailProduct(productId))
    }

    if (state.product != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(56.dp))

            GlideImage(
                model = state.product?.image,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.product?.briefTitle(16)!!,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        if (state.product!!.isBookMark) {
                            Icons.Default.Favorite
                        } else Icons.Default.FavoriteBorder, contentDescription = null,
                        modifier = Modifier.clickable {
                            detailProductViewModel.handleIntent(DetailProductIntent.BookMarkClick)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${state.product!!.price}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = state.product?.description!!)
            }
        }
    }
}