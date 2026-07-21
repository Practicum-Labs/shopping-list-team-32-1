package com.practicum.shoppinglist.presentation.navigation

internal const val PRODUCTS_ROUTE_ARG_LIST_ID = "listId"
internal const val PRODUCTS_ROUTE = "products/{$PRODUCTS_ROUTE_ARG_LIST_ID}"

internal fun productsRoutePath(listId: Long) = "products/$listId"
