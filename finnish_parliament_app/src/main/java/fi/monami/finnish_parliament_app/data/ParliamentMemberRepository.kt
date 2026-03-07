package fi.monami.finnish_parliament_app.data

interface ParliamentMemberRepository {
    suspend fun loadInitialData()
}

class DefaultParliamentRepository(
    private val local: OfflineParliamentMemberRepository,
    private val remote: NetworkParliamentMemberRepository
): ParliamentMemberRepository{
    override suspend fun loadInitialData() {
        val members = remote.getParliamentMembers()
        members.forEach {
            local.insertBasicParliamentMember(
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
                    ""
                )
            )
        }
    }
}