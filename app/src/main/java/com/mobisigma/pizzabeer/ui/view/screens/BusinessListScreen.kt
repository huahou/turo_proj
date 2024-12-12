package com.mobisigma.pizzabeer.ui.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
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
import com.mobisigma.pizzabeer.domain.model.Location
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import com.mobisigma.pizzabeer.ui.view.components.CurrentLocationField
import com.mobisigma.pizzabeer.ui.view.components.SearchField

@Composable
fun BusinessListScreen(
    searchState: SearchBusinessUseCase.SearchUiState?,
    isLoading: Boolean,
    onSearch: (Location) -> Unit,
    onBusinessClick: (Int) -> Unit = {},
    onLoadMore: () -> Unit
){
    Column(modifier = Modifier.fillMaxSize()) {

        SearchSection(onSearch)

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        when (searchState) {
            is SearchBusinessUseCase.SearchUiState.Success -> {
                BusinessEntitiesUI2(searchState.data, onBusinessClick, onLoadMore)
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
fun SearchSection(onSearch: (Location) -> Unit){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchField(stringResource(id = R.string.search_placeholder_text)) { address ->
            onSearch(Location(address = address))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "or",
                style = typography.bodyMedium
            )
        }
        CurrentLocationField {lat, lng ->
            onSearch(Location(latitude = lat, longitude = lng))
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
private fun BusinessEntitiesUI(
    businessEntities: List<BusinessEntity>,
    onBusinessClick: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    if (businessEntities.isEmpty()) {
        EmptyResultUI()
    } else {
        val gridState = rememberLazyStaggeredGridState()
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(200.dp),
                verticalItemSpacing = 2.dp,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                state = gridState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                itemsIndexed(businessEntities) { index, businessEntity ->
                    BusinessCellUI(index = index, businessEntity = businessEntity, onBusinessClick)
                }
            }

            // Infinite scroll handler
            LaunchedEffect(gridState) {
                snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
                    .collect { visibleItems ->
                        val lastVisibleItem = visibleItems.lastOrNull()
                        val totalItems = gridState.layoutInfo.totalItemsCount

                        if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 2) {
                            onLoadMore()
                        }
                    }
            }
        }
    }
}

@Composable
private fun BusinessEntitiesUI2(
    businessEntities: List<BusinessEntity>,
    onBusinessClick: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    if (businessEntities.isEmpty()) {
        EmptyResultUI()
    } else {
        val listState = rememberLazyListState()

        val isScrollToEnd by remember {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
            }
        }

        if (isScrollToEnd) {
            onLoadMore()
        }
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(businessEntities.size) {
                BusinessCellUI(index = it, businessEntity = businessEntities[it], onBusinessClick)
            }
        }
    }
}

@Composable
fun BusinessCellUI(
    index: Int,
    businessEntity: BusinessEntity,
    onBusinessClick: (Int) -> Unit
){
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(businessEntity.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        onBusinessClick.invoke(index)
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
                        text = stringResource(id = R.string.review_numbers, businessEntity.reviewCount),
                        style = typography.bodySmall,
                        color = Color.White
                    )
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