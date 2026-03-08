package fi.monami.finnish_parliament_app.ui.screens

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.monami.finnish_parliament_app.ParliamentTopAppBar
import fi.monami.finnish_parliament_app.data.ParliamentMemberEntity
import fi.monami.finnish_parliament_app.navigation.NavigationDestination
import kotlinx.coroutines.launch

object FavoriteMemberDestination: NavigationDestination{
    override val route: String = "favorite"
    override val title: String = "Your Top‑Rated Representative"
    const val personIdArg = "personId"
    const val scoreArg = "score"
    val routeWithArgs ="$route/{$personIdArg}/{$scoreArg}"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteMemberScreen ( navigateBack: () -> Unit, modifier: Modifier = Modifier){
    val viewModel: FavoriteMemberViewModel= viewModel()
    val memberUiState by viewModel.detailMemberUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val assessmentList by viewModel.assessmentListUiState.collectAsState()
    val score = viewModel.scoreUiState

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ParliamentTopAppBar(
                title = FavoriteMemberDestination.title ,
                canNavigateBack = true,
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->

        FavoriteMemberBody(
            score,
            memberUiState.partyMember,
            assessmentList,
            Modifier.padding(innerPadding)
        )


    }
}

@Composable
fun FavoriteMemberBody(
    score: ScoreUiState,
    member: ParliamentMemberEntity?,
    assessmentList: AssessmentListUiState,
    modifier: Modifier = Modifier
) {
    if (member == null) return

    Column(modifier.fillMaxWidth()) {

        ProfileCard(member)
        Spacer(Modifier.height(8.dp))
        ScoreCard(score.score)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(assessmentList.assessments) { assessment ->
                AssessmentRow(assessment)
            }
        }
    }
}


@Composable
fun ScoreCard(score: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Your Score",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

