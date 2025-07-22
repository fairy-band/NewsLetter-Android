package com.nexters.knownknowns.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

class NavController(val backStack: NavBackStack) {

    fun navigate(dest: NavKey) {
        backStack.add(dest)
    }

    fun pop() {
        backStack.removeLastOrNull()
    }
}

@Composable
fun rememberNavController(): NavController {
    val backStack = rememberNavBackStack(Screen.Main)

    // TODO: rememberSaveable 로 바꿔야 함
    return remember { NavController(backStack) }
}