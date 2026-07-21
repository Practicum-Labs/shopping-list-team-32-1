package com.practicum.shoppinglist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.main.MainContentState
import com.practicum.shoppinglist.presentation.ui.main.MainScreen
import com.practicum.shoppinglist.presentation.ui.main.MainUiState
import org.junit.Rule
import org.junit.Test

class SelectedListHighlightTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val lists = listOf(
        ShoppingList(id = 1L, name = "Продукты на выходные", iconName = "list_alt"),
        ShoppingList(id = 2L, name = "Ремонт на кухне", iconName = "hardware"),
    )

    @Test
    fun selectedListIsMarkedSelected() {
        render(selectedListId = 1L, isSearching = false)

        composeRule.onNodeWithText("Продукты на выходные").assertIsSelected()
        composeRule.onNodeWithText("Ремонт на кухне").assertIsNotSelected()
    }

    @Test
    fun selectedListIsMarkedSelectedWhileSearching() {
        render(selectedListId = 1L, isSearching = true)

        composeRule.onNodeWithText("Продукты на выходные").assertIsSelected()
        composeRule.onNodeWithText("Ремонт на кухне").assertIsNotSelected()
    }

    @Test
    fun withoutSelectionNoListIsMarkedSelected() {
        render(selectedListId = null, isSearching = false)

        composeRule.onNodeWithText("Продукты на выходные").assertIsNotSelected()
        composeRule.onNodeWithText("Ремонт на кухне").assertIsNotSelected()
    }

    private fun render(selectedListId: Long?, isSearching: Boolean) {
        composeRule.setContent {
            Theme {
                MainScreen(
                    uiState = MainUiState(
                        contentState = MainContentState.Content(shoppingLists = lists),
                        isSearching = isSearching,
                        searchQuery = if (isSearching) "н" else "",
                    ),
                    selectedListId = selectedListId,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
