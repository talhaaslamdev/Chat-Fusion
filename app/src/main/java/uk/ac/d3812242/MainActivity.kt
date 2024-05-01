package uk.ac.d3812242

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.d3812242.ui.ChatListScreen
import uk.ac.d3812242.ui.LoginScreen
import uk.ac.d3812242.ui.ProfileScreen
import uk.ac.d3812242.ui.SignupScreen
import uk.ac.d3812242.ui.SingleChatScreen
import uk.ac.d3812242.ui.SingleStatusScreen
import uk.ac.d3812242.ui.StatusListScreen
import uk.ac.d3812242.ui.theme.ChatAppCloneTheme

sealed class DestinationScreen(val route: String) {
    object Signup : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}") {
        fun createRoute(id: String) = "singleChat/$id"
    }
    object StatusList : DestinationScreen("statusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId}") {
        fun createRoute(userId: String?) = "singleStatus/$userId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppCloneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }
}

@Composable
fun ChatAppNavigation() {
    val navController = rememberNavController()
    val vm = hiltViewModel<CAViewModel>()

    NotificationMessage(vm = vm)

    NavHost(navController = navController, startDestination = DestinationScreen.Signup.route) {
        composable(DestinationScreen.Signup.route) {
            SignupScreen(navController, vm)
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController, vm)
        }
        composable(DestinationScreen.Profile.route) {
            ProfileScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.StatusList.route) {
            StatusListScreen(navController = navController, vm)
        }
        composable(DestinationScreen.SingleStatus.route) {
            val userId = it.arguments?.getString("userId")
            userId?.let {
                SingleStatusScreen(navController = navController, vm = vm, userId = userId)
            }
        }
        composable(DestinationScreen.ChatList.route) {
            ChatListScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.SingleChat.route) {
            val chatId = it.arguments?.getString("chatId")
            chatId?.let {
                SingleChatScreen(navController = navController, vm = vm, chatId = it)
            }
        }
    }
}





