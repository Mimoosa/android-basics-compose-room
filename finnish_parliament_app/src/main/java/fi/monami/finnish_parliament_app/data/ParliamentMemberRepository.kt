package fi.monami.finnish_parliament_app.data

import kotlinx.coroutines.flow.Flow

interface ParliamentMemberRepository {
    suspend fun loadInitialData()
    fun getParliamentMemberStream(id: Int): Flow<ParliamentMemberEntity>
    fun getAllParties(): Flow<Set<String>>
    fun getAllPartyMembers(party: String): Flow<List<ParliamentMemberEntity>>
}

class DefaultParliamentRepository(
    private val local: OfflineParliamentMemberRepository,
    private val remote: NetworkParliamentMemberRepository
): ParliamentMemberRepository{
    override suspend fun loadInitialData() {
        val members = remote.getParliamentMembers()
        members.forEach {
            local.insertParliamentMember(
                ParliamentMemberEntity(
                    it.personNumber,
                    it.seatNumber,
                    it.last,
                    it.first,
                    it.party,
                    it.minister,
                    it.twitter,
                    it.bornYear,
                    it.constituency,
                    // Picture URL is constructed manually using last name, first name, and personNumber.
                    "https://users.metropolia.fi/~peterh/edustajakuvat/${it.last}-${it.first}-web-${it.personNumber}.jpg"
                )
            )
        }
    }

    override fun getParliamentMemberStream(id: Int): Flow<ParliamentMemberEntity> {
        return local.getParliamentMember(id)
    }

    override fun getAllParties(): Flow<Set<String>> {
        return local.getParties()
    }

    override fun getAllPartyMembers(party: String): Flow<List<ParliamentMemberEntity>> {
        return local.getAllPartyMembers(party)
    }
}