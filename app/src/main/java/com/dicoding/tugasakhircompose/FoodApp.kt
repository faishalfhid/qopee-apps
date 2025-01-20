package com.dicoding.tugasakhircompose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.tugasakhircompose.data.FoodRepository
import com.dicoding.tugasakhircompose.di.Injection
import com.dicoding.tugasakhircompose.ui.screen.ViewModelFactory
import com.dicoding.tugasakhircompose.ui.screen.cart.CartScreen
import com.dicoding.tugasakhircompose.ui.screen.detail.DetailViewModel
import com.dicoding.tugasakhircompose.ui.screen.detail.FoodDetailScreen
import com.dicoding.tugasakhircompose.ui.screen.home.FoodListScreen
import com.dicoding.tugasakhircompose.ui.screen.home.FoodViewModel
import com.dicoding.tugasakhircompose.ui.screen.navigation.NavigationItem
import com.dicoding.tugasakhircompose.ui.screen.navigation.Screen
import com.dicoding.tugasakhircompose.ui.screen.profile.ProfileScreen
import com.dicoding.tugasakhircompose.ui.theme.CoffeeBrown
import com.dicoding.tugasakhircompose.ui.theme.TugasAkhirComposeTheme

@Composable
fun FoodApp(
    modifier: Modifier = Modifier,
    viewModel: FoodViewModel = viewModel(factory = ViewModelFactory(FoodRepository()))
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detail.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                FoodListScreen(
                    viewModel = viewModel,
                    navigateToDetail = { foodId ->
                        navController.navigate(Screen.Detail.createRoute(foodId))
                    }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("foodId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val foodId = backStackEntry.arguments?.getInt("foodId") ?: return@composable
                val viewModel: DetailViewModel = viewModel(
                    factory = ViewModelFactory(
                        Injection.provideRepository()
                    )
                )
                viewModel.getRewardById(foodId)

                FoodDetailScreen(
                    viewModel = viewModel,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    navigateToCart = {
                        navController.popBackStack()
                        navController.navigate(Screen.Cart.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Screen.Cart.route) {
                val context = LocalContext.current
                CartScreen(
                    onOrderButtonClicked = { message ->
                        shareOrder(context, message)
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}


private fun shareOrder(context: Context, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, context.getString(com.dicoding.tugasakhircompose.R.string.malang_kuliner))
        putExtra(Intent.EXTRA_TEXT, summary)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(com.dicoding.tugasakhircompose.R.string.malang_kuliner)
        )
    )
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(com.dicoding.tugasakhircompose.R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(com.dicoding.tugasakhircompose.R.string.menu_cart),
                icon = Icons.Default.ShoppingCart,
                screen = Screen.Cart
            ),
            NavigationItem(
                title = stringResource(com.dicoding.tugasakhircompose.R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            ),
        )
        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CoffeeBrown,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodAppPreview() {
    TugasAkhirComposeTheme {
        FoodApp()
    }
}