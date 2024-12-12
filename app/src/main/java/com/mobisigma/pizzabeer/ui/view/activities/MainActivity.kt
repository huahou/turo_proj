package com.mobisigma.pizzabeer.ui.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: SearchBusinessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = hiltViewModel<SearchBusinessViewModel>()
            MainApp(viewModel = viewModel)
        }
    }
}

@Composable
private fun MainApp(viewModel: SearchBusinessViewModel) {
    val searchResultState = viewModel.searchResult.observeAsState()
    val loadingState = viewModel.isLoading.observeAsState()

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
                        isLoading = loadingState.value!!,
                        onSearch = { location ->
                            viewModel.search(location)
                        },
                        onBusinessClick = { index ->
                            navController.navigateSingleTopTo("${BusinessDetail.baseRoute}/${index}")
                        },
                        onLoadMore = {
                            viewModel.searchMore()
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


