package com.alirezahr.products.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alirezahr.products.presentation.detail.DetailProductScreen
import com.alirezahr.products.presentation.list.ProductListScreen

@Composable
fun ProductsNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "productList") {
        composable("productList") {
            ProductListScreen { product ->
                navController.navigate("productDetail/${product.id}")
            }
        }

        composable(
            route = "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("productId")?.let {
                DetailProductScreen(productId=it.toInt()){
                    navController.navigateUp()
                }
            }
        }
    }
}