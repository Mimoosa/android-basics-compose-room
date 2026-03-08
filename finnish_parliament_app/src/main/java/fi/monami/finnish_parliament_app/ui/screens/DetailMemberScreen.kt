package fi.monami.finnish_parliament_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.lifecycle.viewmodel.compose.viewModel
import fi.monami.finnish_parliament_app.navigation.NavigationDestination
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll


import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource


import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fi.monami.finnish_parliament_app.ParliamentTopAppBar
import fi.monami.finnish_parliament_app.R
import fi.monami.finnish_parliament_app.data.ParliamentMemberEntity
import kotlinx.coroutines.launch

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
    val memberUiState by viewModel.detailMemberUiState.collectAsState()
    val assessmentUiState = viewModel.assessmentUiState
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val assessmentList by viewModel.assessmentListUiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ParliamentTopAppBar(
                title = "${ DetailMemberDestination.title } (${memberUiState.partyMember?.party})",
                canNavigateBack = true,
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->

    DetailMemberBody(
        memberUiState.partyMember,
        assessmentUiState.assessment,
        { viewModel.onClick(it) },
        { viewModel.onValueChange(it) },
        {
            coroutineScope.launch {
                viewModel.saveAssessment()
                viewModel.updateUiState(Assessment(viewModel.memberId, 0, ""))
            }

        },
        assessmentList.assessments,
        Modifier.padding(innerPadding)
    )


    }
}

@Composable
fun DetailMemberBody(
    member: ParliamentMemberEntity?,
    value: Assessment,
    onClick: (Int) -> Unit,
    onValueChange: (String) -> Unit,
    onSaveButtonClick: () -> Unit,
    assessmentList: List<Assessment>,
    modifier: Modifier = Modifier
){
    if (member != null) {
        LazyColumn(modifier) {
            item{ProfileCard(member, modifier)}
            item{AssessmentLayout(value, onClick, onValueChange, onSaveButtonClick)}
            items(assessmentList){ assessment ->
                AssessmentRow(assessment)
            }
        }

    }
}

@Composable
fun ProfileCard(member: ParliamentMemberEntity, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
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

@Composable
fun AssessmentLayout(
    value: Assessment,
    onClick: (Int) -> Unit,
    onValueChange: (String) -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value.text,
            shape = shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
            ),
            onValueChange = onValueChange,
            label = {
                Text("Write your note about this member")
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End
        ){
            IconButton(onClick = { onClick(+1) }) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Thumbs up (positive assessment)",
                    tint = if (value.indicator == +1) Color.Green else Color.Gray
                )
            }

            IconButton(onClick = { onClick(-1) }) {
                Icon(
                    imageVector = Icons.Default.ThumbDown,
                    contentDescription = "Thumbs down (negative assessment)",
                    tint = if (value.indicator == -1) Color.Red else Color.Gray
                )
            }

        }

        Button(
            onClick = onSaveButtonClick,
            enabled = value.indicator != 0 && value.text.isNotBlank()
        ) {
            Text("Save")
        }
    }
}

@Composable
fun AssessmentRow(assessment: Assessment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = if (assessment.indicator == 1) Icons.Default.ThumbUp else Icons.Default.ThumbDown,
                contentDescription = null,
                tint = if (assessment.indicator == 1) Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assessment.text,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (assessment.indicator == 1) "Positive feedback" else "Negative feedback",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                // Timestamp on the right
                Text(
                    text = assessment.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

