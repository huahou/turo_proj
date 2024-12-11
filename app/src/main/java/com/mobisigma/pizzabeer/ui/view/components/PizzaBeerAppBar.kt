package com.mobisigma.pizzabeer.ui.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobisigma.pizzabeer.PizzaBeerApp
import com.mobisigma.pizzabeer.R
import com.mobisigma.pizzabeer.ui.view.navigation.BusinessDestination
import com.mobisigma.pizzabeer.ui.view.navigation.BusinessDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaBeerAppBar(currentScreen: BusinessDestination, onBack: () -> Unit) {
    val titleText = when (currentScreen) {
        BusinessDetail -> {
            stringResource(id = R.string.business_detail)
        }
        else -> {
            // Home screen
            stringResource(id = R.string.app_name)
        }
    }
    TopAppBar(
        title = {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            if (currentScreen.route == BusinessDetail.route) {
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            } else {
                IconButton(onClick = { /* Handle home icon click */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            }
        }
    )
}