package com.mobisigma.pizzabeer.ui.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobisigma.pizzabeer.R
import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import com.mobisigma.pizzabeer.ui.view.components.SearchField

@Composable
fun BusinessListScreen(
    searchState: SearchBusinessUseCase.SearchUiState?,
    onSearch: (String) -> Unit,
    onBusinessClick: (Int) -> Unit = {}

){
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(stringResource(id = R.string.search_placeholder_text), onSearch)

        when (searchState) {
            is SearchBusinessUseCase.SearchUiState.Loading -> {
                LoadingUI()
            }

            is SearchBusinessUseCase.SearchUiState.Success -> {
                BusinessEntitiesUI(searchState.data, onBusinessClick)
            }

            is SearchBusinessUseCase.SearchUiState.Failure -> {
                ErrorUI()
            }

            else -> {
                InitialUI()
            }

        }
    }
}


@Composable
private fun InitialUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pizza_beer),
            contentDescription = "Flickr logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun LoadingUI() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun BusinessEntitiesUI(
    businessEntities: List<BusinessEntity>,
    onImageClick: (Int) -> Unit
) {
    if (businessEntities.isEmpty()) {
        EmptyResultUI()
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 2.dp,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            itemsIndexed(businessEntities) { index, businessEntity ->
                Box(
                    contentAlignment = Alignment.BottomStart
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(businessEntity.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onImageClick.invoke(index)
                            }
                    )
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(6.dp)
                    ) {
                        Text(
                            text = businessEntity.name,
                            style = typography.titleSmall,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                            )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.five_star),
                                contentDescription = "rating",
                                modifier = Modifier.height(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${businessEntity.reviewCount}",
                                style = typography.bodySmall, 
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyResultUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.no_result_found),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorUI() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.error_loading),
            style = MaterialTheme.typography.bodyMedium,
        )
    }

}