package fi.monami.finnish_parliament_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import fi.monami.finnish_parliament_app.ui.screens.DetailMemberDestination
import fi.monami.finnish_parliament_app.ui.screens.DetailMemberScreen
import fi.monami.finnish_parliament_app.ui.screens.HomeDestination
import fi.monami.finnish_parliament_app.ui.screens.HomeScreen
import fi.monami.finnish_parliament_app.ui.screens.PartyMemberDestination
import fi.monami.finnish_parliament_app.ui.screens.PartyMemberScreen


/**
 * Provides Navigation graph for the application.
 */
@Composable
fun ParliamentNavHost(
    navController: NavController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController as NavHostController,
        startDestination = HomeDestination.route,
        modifier = modifier,
    ){
        composable(route = HomeDestination.route){
            HomeScreen(
                navigateToPartyMemberScreen = {
                navController.navigate("${PartyMemberDestination.route}/$it")
                }
            )
        }

        composable(
            route = PartyMemberDestination.routeWithArgs,
            arguments = listOf(navArgument(PartyMemberDestination.partyArg){
                type = NavType.StringType
            })
            ) {
            PartyMemberScreen(
                navigateBack = { navController.popBackStack() },
                navigationToDetailMemberScreen = {
                    navController.navigate("${DetailMemberDestination.route}/$it")
                }
            )
        }

        composable(
            route = DetailMemberDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailMemberDestination.memberIdArg){
                type = NavType.IntType
            })
            ) {
            DetailMemberScreen(
                navigateBack = { navController.popBackStack()}
            )
        }
    }
}

