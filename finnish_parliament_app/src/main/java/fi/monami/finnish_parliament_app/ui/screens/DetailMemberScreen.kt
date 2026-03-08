package fi.monami.finnish_parliament_app.ui.screens

import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.monami.finnish_parliament_app.navigation.NavigationDestination
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fi.monami.finnish_parliament_app.ParliamentTopAppBar
import fi.monami.finnish_parliament_app.R
import fi.monami.finnish_parliament_app.data.ParliamentMemberEntity

object DetailMemberDestination: NavigationDestination {
    override val route: String = "Detail"
    override val title: String = "Parliament Member Details"

    const val memberIdArg = "memberId"
    val routeWithArgs = "$route/{$memberIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMemberScreen(navigateBack: () -> Unit, modifier: Modifier = Modifier){
    val viewModel: DetailMemberViewModel = viewModel()
    val uiState by viewModel.detailMemberUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ParliamentTopAppBar(
                title = "${ DetailMemberDestination.title } (${uiState.partyMember?.party})",
                canNavigateBack = true,
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->

    DetailMemberBody(
        uiState.partyMember,
        Modifier.padding(innerPadding)
    )


    }
}

@Composable
fun DetailMemberBody(
    member: ParliamentMemberEntity?,
    modifier: Modifier = Modifier
){
    if (member != null) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 44.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(member.imgUrl)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = "Parliament Member's photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.height(16.dp))

                InfoText("Name: ", "${member.first} ${member.last}")
                InfoText("Minister: ", if (member.minister) "Yes" else "No")
                InfoText("Constituency: ", member.constituency)
                InfoText("Seat Number: ", member.seatNumber.toString())
                InfoText("Year of Birth: ", member.bornYear.toString())
                InfoText("Twitter: ", if (member.twitter.isNotEmpty()) member.twitter else "No")
            }
        }
    }
}

@Composable
fun InfoText(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}



