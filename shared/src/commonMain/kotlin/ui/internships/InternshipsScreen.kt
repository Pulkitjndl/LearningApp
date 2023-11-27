@file:OptIn(ExperimentalResourceApi::class)

package ui.internships

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.compose.NavigationContainer
import com.chrynan.navigation.compose.rememberNavigator
import com.chrynan.navigation.popDestination
import com.chrynan.navigation.push
import data.InternshipApiModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.PagingListUI

class InternshipsScreen : KoinComponent {
    private val viewModel: InternshipViewModel by inject()

    @OptIn(ExperimentalNavigationApi::class)
    @Composable
    fun NavigationMain() {
        val navigator = rememberNavigator("internships")
        var bundle: InternshipApiModel.Internship? = null

        MaterialTheme {
            NavigationContainer(
                navigator = navigator,
                modifier = Modifier.fillMaxSize()
            ) { (destination) ->
                when (destination) {
                    "internships" -> InternshipScreen(
                        loginClick = { internships ->
                            bundle = internships
                            navigator.push("internship_detail")
                        },
                        searchClick = {
                            viewModel.searchInternships("")
                            navigator.push("searchScreen")
                        }
                    )

                    "internship_detail" -> bundle?.let {
                        InternshipDetailScreen(
                            it,
                            onBackPress = { navigator.popDestination() }
                        )
                    }

                    "searchScreen" -> {
                        InternshipSearchScreen(
                            onBackPress = { navigator.popDestination() },
                            onSearch = { query ->
                                viewModel.searchInternships(query)
                                navigator.popDestination()
                            },
                            onCardClick = { internships ->
                                bundle = internships
                                navigator.push("internship_detail")
                            }
                        ) 
                    }
                }
            }
        }
    }

    @Composable
    fun InternshipScreen(
        loginClick: (internship: InternshipApiModel.Internship) -> Unit,
        searchClick: () -> Unit
    ) {
        val result by rememberUpdatedState(viewModel.internships.collectAsLazyPagingItems())
        return Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Internships") },
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(onClick = { println("Drawer clicked") }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            println("Search Internships!")
                            searchClick.invoke()
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    backgroundColor = Color.White
                )
            },
            content = {
                PagingListUI(data = result) { internship, _ ->
                    InternshipCard(internship, onClick = loginClick)
                }
            }
        )
    }

    @Composable
    fun InternshipSearchScreen(
        onSearch: (String) -> Unit,
        onBackPress: () -> Unit,
        onCardClick: (internship: InternshipApiModel.Internship) -> Unit
    ) {
        val result by rememberUpdatedState(viewModel.internships.collectAsLazyPagingItems())
        var searchText by remember { mutableStateOf(TextFieldValue()) }
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPress,
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.searchInternships(searchText.text)
                    },
                    label = { Text("Search Internships") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Button(
                    onClick = {
                        onSearch(searchText.text)
                    }
                ) {
                    Text("Search")
                }
            }

            PagingListUI(data = result) { internship, _ ->
                InternshipCard(internship, onClick = { onCardClick(internship) })
            }
        }
    }
}