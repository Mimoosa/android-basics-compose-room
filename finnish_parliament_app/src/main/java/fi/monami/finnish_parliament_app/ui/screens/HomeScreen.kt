package fi.monami.finnish_parliament_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.monami.finnish_parliament_app.navigation.NavigationDestination

import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fi.monami.finnish_parliament_app.ParliamentTopAppBar

object HomeDestination: NavigationDestination{
    override val route = "home"
    override val title = "The Finnish Parliament Parties"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToPartyMemberScreen: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val viewModel: ParliamentMemberViewModel = viewModel()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.parliamentPartyUiState.collectAsState()

    Scaffold(
        // Enables the top app bar to react to scroll events
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ParliamentTopAppBar(
                title = HomeDestination.title,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },

    ) { innerPadding ->
        HomeBody(
            // Convert the Set to a List because LazyColumn's items() requires an ordered collection.
            uiState.parliamentPartySet.toList(),
            navigateToPartyMemberScreen,
            modifier.fillMaxSize(),
            innerPadding
        )
    }

}

@Composable
private fun HomeBody(
    partyList: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    LazyColumn(
        modifier = modifier.padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(partyList) { party ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable{onClick(party)},
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = party,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}



