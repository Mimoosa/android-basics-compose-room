package fi.monami.finnish_parliament_app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class OfflineParliamentMemberRepository(private val parliamentMemberDao: ParliamentMemberDao, private val assessmentDao: AssessmentDao) {
    suspend fun insertParliamentMember(parliamentMemberEntity: ParliamentMemberEntity) = parliamentMemberDao.insert(parliamentMemberEntity)

    fun getParliamentMember(id: Int): Flow<ParliamentMemberEntity> = parliamentMemberDao.getParliamentMember(id)

    // Returns a Flow that emits the set of all unique party names.
    fun getParties(): Flow<Set<String>> = parliamentMemberDao.getAllParliamentMembers()
        .transform { parliamentMembers -> emit(
            parliamentMembers.map{it.party}.toSet()
        ) }

    fun getAllPartyMembers(party: String): Flow<List<ParliamentMemberEntity>> = parliamentMemberDao.getAllPartyMembers(party)

    suspend fun insertAssessment(assessmentEntity: AssessmentEntity) = assessmentDao.insert(assessmentEntity)
    fun getAllAssessmentsForMember(personNumber: Int): Flow<List<AssessmentEntity>> = assessmentDao.getAllAssessmentsForMember(personNumber)

    fun getFavoritePoliticians(): Flow<FavoritePoliticianScore> = assessmentDao.getFavoritePolitician()
}