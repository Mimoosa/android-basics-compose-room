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
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fi.monami.finnish_parliament_app.ParliamentTopAppBar
import kotlinx.coroutines.launch

object HomeDestination: NavigationDestination{
    override val route = "home"
    override val title = "The Finnish Parliament Parties"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToPartyMemberScreen: (String) -> Unit,
    navigateToFavoriteMemberScreen: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
){
    val viewModel: ParliamentMemberViewModel = viewModel()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.parliamentPartyUiState.collectAsState()
    val favoriteMemberUiState by viewModel.favoriteMemberUiState.collectAsState()

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
            { navigateToFavoriteMemberScreen(favoriteMemberUiState.personNumber, favoriteMemberUiState.score) },
            favoriteMemberUiState.personNumber,
            modifier.fillMaxSize(),
            innerPadding
        )
    }

}

@Composable
private fun HomeBody(
    partyList: List<String>,
    onClick: (String) -> Unit,
    onFavoriteMemberClick: () -> Unit,
    favoriteMemberId: Int,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){

    Column(modifier) {
        LazyColumn(
            modifier = Modifier.padding(contentPadding).weight(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(partyList) { party ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable { onClick(party) },
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

        Button(
            onClick = onFavoriteMemberClick,
            enabled = favoriteMemberId != -1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)) {
            Text("See your most liked member" )
        }
    }
}



