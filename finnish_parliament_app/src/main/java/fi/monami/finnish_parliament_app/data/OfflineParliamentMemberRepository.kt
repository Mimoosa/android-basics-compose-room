package fi.monami.finnish_parliament_app.data

import kotlinx.coroutines.flow.Flow

class OfflineParliamentMemberRepository(private val parliamentMemberDao: ParliamentMemberDao) {
    suspend fun insertBasicParliamentMember(parliamentMemberEntity: ParliamentMemberEntity) = parliamentMemberDao.insertBasicInfo(parliamentMemberEntity)


}