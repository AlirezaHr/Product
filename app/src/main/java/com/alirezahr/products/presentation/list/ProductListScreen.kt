package com.alirezahr.products.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.presentation.common.InCenterParent
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val filteredProducts by remember {
        derivedStateOf {
            if (state.searchQuery.isBlank()) state.products
            else state.products.filter {
                it.title.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductListIntent.LoadProduct)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp, 56.dp, 18.dp, 18.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Product List",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(Icons.Filled.FavoriteBorder, contentDescription = "BookMark")

        }

        Column(Modifier.padding(16.dp)) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.handleIntent(ProductListIntent.SearchChanged(it)) },
                onSearch = { query ->
                    viewModel.handleIntent(ProductListIntent.SearchChanged(query))
                },
            )
            Spacer(Modifier.height(4.dp))
        }

        if (state.isLoading){
            InCenterParent {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
            }
        }else if(state.error!=null){
            Text(
                textAlign = TextAlign.Center,
                text = "Error : ${state.error}",
                fontSize = 18.sp,
            )
        }else{
            if (filteredProducts.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                    items(filteredProducts) { product ->
                        ProductItem(product) {
                            onProductClick.invoke(product)
                        }
                    }
                }
            } else {
                InCenterParent{
                    Text(text = "Product not found")
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(product: Product,onClick:()->Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick.invoke() }
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            GlideImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = product.briefTitle(), maxLines = 1, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "$${product.price}")

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search…",
    enabled: Boolean = true,
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                onSearch(query)
            }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
    }
}

