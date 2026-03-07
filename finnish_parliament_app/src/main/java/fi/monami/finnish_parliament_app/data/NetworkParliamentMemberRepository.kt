package fi.monami.finnish_parliament_app.data




import fi.monami.finnish_parliament_app.network.ParliamentMember
import fi.monami.finnish_parliament_app.network.retrofitService


class NetworkParliamentMemberRepository(){
    suspend fun getParliamentMembers(): List<ParliamentMember>{
        return retrofitService.getParliamentMembers()
    }
}