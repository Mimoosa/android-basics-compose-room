package fi.monami.finnish_parliament_app.ui.screens



import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle

import androidx.lifecycle.viewModelScope
import fi.monami.finnish_parliament_app.data.AssessmentEntity
import fi.monami.finnish_parliament_app.data.DefaultParliamentRepository
import fi.monami.finnish_parliament_app.data.NetworkParliamentMemberRepository
import fi.monami.finnish_parliament_app.data.OfflineParliamentMemberRepository

import fi.monami.finnish_parliament_app.data.ParliamentMemberDatabase
import fi.monami.finnish_parliament_app.data.ParliamentMemberEntity

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Provides ApplicationContext needed to initialize the Room database.
class DetailMemberViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) :  AndroidViewModel(application){

    val memberId: Int =
        checkNotNull(savedStateHandle[DetailMemberDestination.memberIdArg])
    private val parliamentMemberDB = ParliamentMemberDatabase.getDatabase(application)

    private val offlineParliamentMemberRepository = OfflineParliamentMemberRepository(parliamentMemberDB.parliamentMemberDao(), assessmentDao = parliamentMemberDB.assessmentDao())
    private val networkParliamentMemberRepository = NetworkParliamentMemberRepository()

    private val parliamentMemberRepository = DefaultParliamentRepository(offlineParliamentMemberRepository, networkParliamentMemberRepository)

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val detailMemberUiState: StateFlow<DetailMemberUiState> =
        parliamentMemberRepository.getParliamentMemberStream(memberId)
            .filterNotNull()
            .map { DetailMemberUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailMemberUiState()
            )

    var assessmentUiState by mutableStateOf(AssessmentUiState())
       private set

    fun onClick(indicator: Int){
        val updatedAssessment = assessmentUiState.assessment.copy(indicator = indicator)
        updateUiState(updatedAssessment)
    }

    fun onValueChange(text: String){
        val updatedAssessment = assessmentUiState.assessment.copy(text = text)
        updateUiState(updatedAssessment)
    }

    fun updateUiState(assessment: Assessment){

        assessmentUiState = AssessmentUiState(assessment = assessment, isEntryValid = validateInput(assessment))
    }

    private fun validateInput(uiState: Assessment = assessmentUiState.assessment): Boolean{
        return with(uiState){
            personNumber!= -1 && indicator != 0 && text.isNotBlank()
        }
    }

    suspend fun saveAssessment(){
        val assessment = Assessment(
            personNumber = memberId,
            indicator = assessmentUiState.assessment.indicator,
            text = assessmentUiState.assessment.text
        )
        if(validateInput(assessment)){
            parliamentMemberRepository.insertAssessment(assessment.toAssessmentEntity())
        }
    }

    val assessmentListUiState: StateFlow<AssessmentListUiState> =
        parliamentMemberRepository.getAllAssessmentsForMember(memberId)
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

data class DetailMemberUiState(val partyMember: ParliamentMemberEntity? = null)

data class AssessmentUiState(
    var assessment: Assessment = Assessment(),
    var isEntryValid: Boolean = false
)
data class Assessment(
    val personNumber: Int = -1,
    val indicator: Int = 0,
    val text: String = "",
    val timestamp: String = ""
)

data class AssessmentListUiState(val assessments: List<Assessment> = listOf())

// Converts a database entity into a domain-layer Assessment model
fun AssessmentEntity.toAssessment(): Assessment = Assessment(
    personNumber = personNumber,
    indicator = indicator,
    text = text,
    timestamp = createdAt.toReadableDate()
)

// Converts a domain-layer Assessment model back into a Room entity.
fun Assessment.toAssessmentEntity(): AssessmentEntity = AssessmentEntity(
    personNumber = personNumber,
    indicator = indicator,
    text = text
)

fun Long.toReadableDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}