package fi.monami.finnish_parliament_app.ui.screens

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fi.monami.finnish_parliament_app.data.DefaultParliamentRepository
import fi.monami.finnish_parliament_app.data.NetworkParliamentMemberRepository
import fi.monami.finnish_parliament_app.data.OfflineParliamentMemberRepository
import fi.monami.finnish_parliament_app.data.ParliamentMemberDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteMemberViewModel (
    application: Application,
    savedStateHandle: SavedStateHandle
) :  AndroidViewModel(application){

    val personId: Int =
        checkNotNull(savedStateHandle[FavoriteMemberDestination.personIdArg])
    val score: Int =
        checkNotNull(savedStateHandle[FavoriteMemberDestination.scoreArg])
    private val parliamentMemberDB = ParliamentMemberDatabase.getDatabase(application)

    private val offlineParliamentMemberRepository = OfflineParliamentMemberRepository(parliamentMemberDB.parliamentMemberDao(), assessmentDao = parliamentMemberDB.assessmentDao())
    private val networkParliamentMemberRepository = NetworkParliamentMemberRepository()

    private val parliamentMemberRepository = DefaultParliamentRepository(offlineParliamentMemberRepository, networkParliamentMemberRepository)

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val detailMemberUiState: StateFlow<DetailMemberUiState> =
        parliamentMemberRepository.getParliamentMemberStream(personId)
            .filterNotNull()
            .map { DetailMemberUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailMemberUiState()
            )

    val scoreUiState: ScoreUiState = ScoreUiState(score)



    val assessmentListUiState: StateFlow<AssessmentListUiState> =
        parliamentMemberRepository.getAllAssessmentsForMember(personId)
            .filterNotNull()
            .map { list ->
                AssessmentListUiState(
                    assessments = list.map { it.toAssessment() }
                ) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AssessmentListUiState()
            )
}

data class ScoreUiState(val score:Int = 0)
