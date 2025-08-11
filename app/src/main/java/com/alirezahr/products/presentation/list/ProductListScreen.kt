package com.alirezahr.products.presentation.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alirezahr.products.R
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

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductListIntent.LoadProduct)
    }

    val filteredProducts by remember(state.products, state.searchQuery) {
        derivedStateOf {
            if (state.searchQuery.isBlank()) state.products
            else state.products.filter {
                it.title.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp, 56.dp, 18.dp, 18.dp)
    ) {
        Text(
            text = stringResource(R.string.product_list),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge
        )

        Column(Modifier.padding(16.dp)) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.handleIntent(ProductListIntent.SearchChanged(it)) },
                onSearch = { query ->
                    viewModel.handleIntent(ProductListIntent.SearchChanged(query))
                },
            )
        }

        Spacer(Modifier.height(4.dp))

        BookMarkSwitch(state.isBookMark) {
            viewModel.handleIntent(ProductListIntent.SwitchChange)
        }

        Spacer(modifier = Modifier.height(8.dp))

        ProductListContent(
            state,
            filteredProducts,
            listState,
            onProductClick = { product -> onProductClick.invoke(product) },
            onRetry = {viewModel.handleIntent(ProductListIntent.RetryClick)}
        )
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
    placeholder: String = stringResource(R.string.search),
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
                Icon(Icons.Filled.Search, contentDescription =null)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = null)
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

@Composable
fun BookMarkSwitch(state: Boolean, onCheckedChange: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.bookmarks_only),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = state,
            onCheckedChange = { onCheckedChange.invoke() }
        )
    }
}

@Composable
fun ProductListContent(
    state: ProductListState,
    filteredProducts: List<Product>,
    listState: LazyListState,
    onProductClick: (Product) -> Unit,
    onRetry: () -> Unit
) {
    if (state.isLoading) {
        InCenterParent {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }
    } else if (state.error != null) {
        InCenterParent {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = state.error.name,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = {
                onRetry.invoke()
            }) {
                Text(text = stringResource(R.string.on_retry))
            }
        }
    } else if (filteredProducts.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredProducts) { product ->
                ProductItem(product) {
                    onProductClick.invoke(product)
                }
            }
        }
    } else if (state.hasLoaded && (state.searchQuery.isNotEmpty()||filteredProducts.isEmpty())) {
        InCenterParent {
            Text(text = stringResource(R.string.product_not_found))
        }
    }
}
