package com.fairyband.soak.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

class NavController(val backStack: NavBackStack) {

    fun replace(dest: NavKey) {
        backStack.add(dest)
        backStack.removeAt(backStack.size - 2) // 원래 화면
    }

    fun navigate(dest: NavKey) {
        backStack.add(dest)
    }

    fun pop() {
        backStack.removeLastOrNull()
    }
}

@Composable
fun rememberNavController(): NavController {
    val backStack = rememberNavBackStack(Screen.Splash)

    return remember { NavController(backStack) }
}