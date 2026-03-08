package fi.monami.finnish_parliament_app.ui.screens

import android.app.Application

import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.viewModelScope
import fi.monami.finnish_parliament_app.data.DefaultParliamentRepository
import fi.monami.finnish_parliament_app.data.NetworkParliamentMemberRepository
import fi.monami.finnish_parliament_app.data.OfflineParliamentMemberRepository

import fi.monami.finnish_parliament_app.data.ParliamentMemberDatabase


import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Provides ApplicationContext needed to initialize the Room database.
class ParliamentMemberViewModel(application: Application) :  AndroidViewModel(application){
    private val parliamentMemberDB = ParliamentMemberDatabase.getDatabase(application)

    private val offlineParliamentMemberRepository = OfflineParliamentMemberRepository(parliamentMemberDB.parliamentMemberDao(), assessmentDao = parliamentMemberDB.assessmentDao())
    private val networkParliamentMemberRepository = NetworkParliamentMemberRepository()

    private val parliamentMemberRepository = DefaultParliamentRepository(offlineParliamentMemberRepository, networkParliamentMemberRepository)

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    val parliamentPartyUiState: StateFlow<ParliamentPartyUiState> =
        parliamentMemberRepository.getAllParties()
            .map { ParliamentPartyUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ParliamentPartyUiState()
            )

    init { loadInitialData() }
    fun loadInitialData(){
        viewModelScope.launch {
           parliamentMemberRepository.loadInitialData()

        }
    }
}



data class ParliamentPartyUiState(val parliamentPartySet: Set<String> = setOf())