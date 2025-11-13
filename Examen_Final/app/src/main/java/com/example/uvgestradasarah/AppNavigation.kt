package com.example.uvgestradasarah

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uvgestradasarah.screens.AssetsScreen
import com.example.uvgestradasarah.screens.ProfileScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AssetsScreen().toString(),
        modifier = Modifier
    ){
        composable<AssetsScreen>(
            onAssetClick = { asset ->
                navController.navigate(
                    ProfileScreen(id = asset.id)
                )
            }
        )

        composable<ProfileScreen>( backStackEntry ->
            val profile = backStackEntry.toRoute<ProfileScreen>()
            ProfileScreen(
                profile = profile,
                onBack = {
                    navController.popBackStack()
                }
            )
    }
}
