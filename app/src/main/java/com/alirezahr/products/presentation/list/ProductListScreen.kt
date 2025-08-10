package com.alirezahr.products.presentation.list

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alirezahr.products.domain.model.Product

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductListIntent.LoadProduct)
    }

    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        if (state.isLoading){
            CircularProgressIndicator(modifier = Modifier.size(18.dp))
        }else if(state.error!=null){
            Text(text="Error : ${state.error}", fontSize = 18.sp)
        }else{
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.products) { product->
                    ProductItem(product){
                        onProductClick.invoke(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product,onClick:()->Unit){
    Log.d("product",product.toString())
}