package fi.monami.finnish_parliament_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.monami.finnish_parliament_app.ParliamentTopAppBar
import fi.monami.finnish_parliament_app.data.ParliamentMemberEntity
import fi.monami.finnish_parliament_app.navigation.NavigationDestination


object PartyMemberDestination: NavigationDestination{
    override val route: String = "partyMember"
    override val title: String = "Members of the Party"

    const val partyArg = "party"
    val routeWithArgs = "$route/{$partyArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyMemberScreen(
    navigateBack: () -> Unit,
    navigationToDetailMemberScreen: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val viewModel: PartyMemberViewModel = viewModel()
    val uiState by viewModel.partyMemberUiState.collectAsState()

    Scaffold(
       modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
       topBar = {
           ParliamentTopAppBar(
               title = PartyMemberDestination.title,
               canNavigateBack = true,
               modifier = Modifier.fillMaxWidth(),
               scrollBehavior = scrollBehavior,
               navigateUp = navigateBack
           )
       },
    ) { innerPadding ->

       PartyMemberBody(
           uiState.partyMemberList,
           navigationToDetailMemberScreen,
           modifier= Modifier,
           contentPadding = innerPadding
       )


    }
}

@Composable
fun PartyMemberBody(
memberList: List<ParliamentMemberEntity>,
onClick: (Int) -> Unit,
modifier: Modifier = Modifier,
contentPadding: PaddingValues = PaddingValues(0.dp),
) {
LazyColumn(
   modifier = modifier.padding(contentPadding),
   horizontalAlignment = Alignment.CenterHorizontally
) {

   items(memberList) { member ->
       Card(
           modifier = Modifier
               .padding(horizontal = 16.dp, vertical = 8.dp)
               .fillMaxWidth()
               .clickable { onClick(member.personNumber) },
           shape = RoundedCornerShape(4.dp),
           elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
       ) {
           Text(
               text = "${member.first} ${member.last}",
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



