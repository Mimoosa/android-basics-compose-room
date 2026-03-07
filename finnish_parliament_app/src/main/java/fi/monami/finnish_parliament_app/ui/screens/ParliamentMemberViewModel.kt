package fi.monami.finnish_parliament_app.ui.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.monami.finnish_parliament_app.data.DefaultParliamentRepository
import fi.monami.finnish_parliament_app.data.NetworkParliamentMemberRepository
import fi.monami.finnish_parliament_app.data.OfflineParliamentMemberRepository
import fi.monami.finnish_parliament_app.data.ParliamentMemberDao
import fi.monami.finnish_parliament_app.data.ParliamentMemberDatabase
import fi.monami.finnish_parliament_app.data.ParliamentMemberEntity
import kotlinx.coroutines.cancel

import kotlinx.coroutines.launch

// Provides ApplicationContext needed to initialize the Room database.
class ParliamentMemberViewModel(application: Application) :  AndroidViewModel(application){
    private val parliamentMemberDB = ParliamentMemberDatabase.getDatabase(application)

    private val offlineParliamentMemberRepository = OfflineParliamentMemberRepository(parliamentMemberDB.parliamentMemberDao())
    private val networkParliamentMemberRepository = NetworkParliamentMemberRepository()

    private val parliamentMemberRepository = DefaultParliamentRepository(offlineParliamentMemberRepository, networkParliamentMemberRepository)



    init { loadInitialData() }
    fun loadInitialData(){
        viewModelScope.launch {
           parliamentMemberRepository.loadInitialData()

        }
    }
}

