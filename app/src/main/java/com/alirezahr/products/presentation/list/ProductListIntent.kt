package com.alirezahr.products.presentation.list

import androidx.compose.ui.window.PopupProperties

sealed class ProductListIntent {
    object LoadProduct:ProductListIntent()
    object SwitchChange:ProductListIntent()
    data class SearchChanged(val query:String):ProductListIntent()
}