package com.example.uvgestradasarah

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.uvgestradasarah.navigation.Assets
import com.example.uvgestradasarah.navigation.Profile
import com.example.uvgestradasarah.screens.AssetsScreen
import com.example.uvgestradasarah.screens.ProfileScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Assets,
        modifier = modifier
    ){
        composable<Assets> {
            AssetsScreen(
                onAssetClick = { asset ->
                    navController.navigate(
                        Profile(id = asset.id)
                    )
                }
            )
        }

        composable<Profile> { backStackEntry ->
            val profile = backStackEntry.toRoute<Profile>()
            ProfileScreen(
                id = profile.id,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}