package com.mobisigma.pizzabeer.ui.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
            FlickrApp(viewModel = viewModel)
        }
    }
}

@Composable
private fun FlickrApp(viewModel: SearchBusinessViewModel) {
    val photosState = viewModel.searchResult.observeAsState()
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
                        searchState = photosState.value,
                        onSearch = { viewModel.search(it) },
                        onBusinessClick = { index ->
                            navController.navigateSingleTopTo("${BusinessDetail.baseRoute}/${index}")
                        }
                    )
                }

                composable(
                    route = BusinessDetail.route,
                    arguments = BusinessDetail.arguments
                ) { navBackStackEntry ->
                    val photoIndex = navBackStackEntry.arguments?.getInt(BusinessDetail.businessIndexArg)
                    photoIndex?.let{
                        val image = (photosState as SearchBusinessUseCase.SearchUiState.Success).data[photoIndex]
                        BusinessDetailScreen()
                    }
                }
            }
        }
    }
}


