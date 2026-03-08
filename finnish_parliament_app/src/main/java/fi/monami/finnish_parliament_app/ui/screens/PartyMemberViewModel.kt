package fi.monami.finnish_parliament_app.ui.screens

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

// Provides ApplicationContext needed to initialize the Room database.
class PartyMemberViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
    ) :  AndroidViewModel(application){

    private val party: String =
        checkNotNull(savedStateHandle[PartyMemberDestination.partyArg])
    private val parliamentMemberDB = ParliamentMemberDatabase.getDatabase(application)

    private val offlineParliamentMemberRepository = OfflineParliamentMemberRepository(parliamentMemberDB.parliamentMemberDao())
    private val networkParliamentMemberRepository = NetworkParliamentMemberRepository()

    private val parliamentMemberRepository = DefaultParliamentRepository(offlineParliamentMemberRepository, networkParliamentMemberRepository)

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val partyMemberUiState: StateFlow<PartyMemberUiState> =
        parliamentMemberRepository.getAllPartyMembers(party)
            .filterNotNull()
            .map{ PartyMemberUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PartyMemberUiState()
            )


}

data class PartyMemberUiState(val partyMemberList: List<ParliamentMemberEntity> = listOf())
