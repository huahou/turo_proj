package com.mobisigma.pizzabeer.ui.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mobisigma.pizzabeer.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun SearchField(
    searchPlaceholder: String,
    onSearch: (String) -> Unit
) {
    val searchQuery = remember { MutableStateFlow("") }

    LaunchedEffect(Unit) {
        searchQuery
            .debounce(1000)
            .collect { query ->
                if (query.isNotEmpty()) {
                    onSearch(query)
                }
            }
    }

    TextField(
        value = searchQuery.collectAsState().value,
        onValueChange = { newText ->
            searchQuery.value = newText
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(text = stringResource(id = R.string.search_placeholder_text))
        },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        maxLines = 1,
        modifier = Modifier.fillMaxWidth()
    )
}