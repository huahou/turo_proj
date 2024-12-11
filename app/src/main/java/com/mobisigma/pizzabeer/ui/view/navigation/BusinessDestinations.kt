package com.mobisigma.pizzabeer.ui.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface BusinessDestination {
    val icon: ImageVector
    val route: String
}

object BusinessList : BusinessDestination {
    override val icon = Icons.Filled.Home
    override val route = "search"
}

object BusinessDetail : BusinessDestination {
    override val icon = Icons.Filled.Details
    const val businessIndexArg = "business_index"
    override val route = "business_detail/{$businessIndexArg}"
    const val baseRoute = "business_detail"
    val arguments = listOf(navArgument(businessIndexArg){ type = NavType.IntType})
}

val businessScreens = listOf(BusinessList, BusinessDetail)