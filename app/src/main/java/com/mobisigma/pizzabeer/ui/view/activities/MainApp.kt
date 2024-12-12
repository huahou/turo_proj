package com.mobisigma.pizzabeer.ui.view.activities

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import com.mobisigma.pizzabeer.ui.theme.PizzaBeerTheme
import com.mobisigma.pizzabeer.ui.view.components.PizzaBeerAppBar
import com.mobisigma.pizzabeer.ui.view.navigation.BusinessDetail
import com.mobisigma.pizzabeer.ui.view.navigation.BusinessList
import com.mobisigma.pizzabeer.ui.view.navigation.businessScreens
import com.mobisigma.pizzabeer.ui.view.screens.BusinessDetailScreen
import com.mobisigma.pizzabeer.ui.view.screens.BusinessListScreen
import com.mobisigma.pizzabeer.ui.viewmodel.SearchBusinessViewModel
import com.mobisigma.pizzabeer.utils.ext.navigateSingleTopTo

@Composable
fun MainApp(viewModel: SearchBusinessViewModel) {
    val searchResultState = viewModel.searchResult.collectAsState()
    val loadingState = viewModel.isLoading.collectAsState()

    PizzaBeerTheme {
        val navController: NavHostController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()

        Scaffold(
            topBar = {
                PizzaBeerAppBar(
                    currentScreen = businessScreens.find {
                        it.route == currentBackStack?.destination?.route
                    } ?: BusinessList
                ){
                    navController.navigateUp()
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BusinessList.route,
                modifier = Modifier.padding(innerPadding)
            ){
                composable(route = BusinessList.route) {
                    BusinessListScreen(
                        searchState = searchResultState.value,
                        isLoading = loadingState.value,
                        onSearch = { location ->
                            viewModel.search(location)
                        },
                        onBusinessClick = { index ->
                            navController.navigateSingleTopTo("${BusinessDetail.baseRoute}/${index}")
                        },
                        onLoadMore = {
                            viewModel.search()
                        }
                    )
                }

                composable(
                    route = BusinessDetail.route,
                    arguments = BusinessDetail.arguments
                ) { navBackStackEntry ->
                    val businessIndex = navBackStackEntry.arguments?.getInt(BusinessDetail.businessIndexArg)
                    businessIndex?.let{
                        val businessEntity = (searchResultState.value as SearchBusinessUseCase.SearchUiState.Success).data[businessIndex]
                        BusinessDetailScreen(businessEntity)
                    }
                }
            }
        }
    }
}