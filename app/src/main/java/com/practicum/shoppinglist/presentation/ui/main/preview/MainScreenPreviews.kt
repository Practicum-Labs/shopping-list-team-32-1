package com.practicum.shoppinglist.presentation.ui.main.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.MainContentState
import com.practicum.shoppinglist.presentation.ui.main.MainScreen
import com.practicum.shoppinglist.presentation.ui.main.MainScreenActions
import com.practicum.shoppinglist.presentation.ui.main.MainUiState
import com.practicum.shoppinglist.presentation.ui.common.ConfirmationDialogContent
import com.practicum.shoppinglist.presentation.ui.main.components.AddShoppingListDialogContent
import com.practicum.shoppinglist.presentation.ui.main.components.RenameShoppingListDialogContent
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListIconPickerContent

@Preview(
    name = "Light",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun MainScreenLightPreview() {
    Theme {
        MainScreen(
            uiState = MainUiState(contentState = MainContentState.Empty),
        )
    }
}

@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun MainScreenDarkPreview() {
    Theme(darkTheme = true) {
        MainScreen(
            uiState = MainUiState(contentState = MainContentState.Empty),
            isDarkTheme = true,
        )
    }
}

@Preview(
    name = "With Lists",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun MainScreenWithListsPreview() {
    Theme {
        MainScreen(
            uiState = MainUiState(
                contentState = MainContentState.Content(
                    shoppingLists = listOf(
                        ShoppingList(
                            id = 1L,
                            name = "Список 1",
                            iconName = "list_alt",
                        ),
                        ShoppingList(
                            id = 2L,
                            name = "Список 2",
                            iconName = "shopping_bag",
                        ),
                    ),
                ),
            ),
        )
    }
}

@Preview(
    name = "Loading",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun MainScreenLoadingPreview() {
    Theme {
        MainScreen(uiState = MainUiState(contentState = MainContentState.Loading))
    }
}

@Preview(
    name = "Error Light",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun MainScreenErrorLightPreview() {
    MainScreenErrorPreviewContent(isDarkTheme = false)
}

@Preview(
    name = "Error Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun MainScreenErrorDarkPreview() {
    MainScreenErrorPreviewContent(isDarkTheme = true)
}

@Composable
private fun MainScreenErrorPreviewContent(
    isDarkTheme: Boolean,
) {
    Theme(darkTheme = isDarkTheme) {
        MainScreen(
            uiState = MainUiState(contentState = MainContentState.Error),
            isDarkTheme = isDarkTheme,
            actions = MainScreenActions(onRetryClick = {}),
        )
    }
}

@Preview(
    name = "Add List Dialog",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun AddShoppingListDialogPreview() {
    Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            AddShoppingListDialogContent(
                listName = "",
                isCreatingList = false,
                onListNameChange = {},
                onDismiss = {},
                onCreateClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Main.addDialogHorizontalPadding),
            )
        }
    }
}

@Preview(
    name = "Add List Dialog Dark",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun AddShoppingListDialogDarkPreview() {
    Theme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            AddShoppingListDialogContent(
                listName = "",
                isCreatingList = false,
                onListNameChange = {},
                onDismiss = {},
                onCreateClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Main.addDialogHorizontalPadding),
            )
        }
    }
}

@Preview(
    name = "Icon Picker",
    showBackground = true,
    widthDp = 428,
    heightDp = 460,
)
@Composable
private fun ShoppingListIconPickerPreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.iconPickerSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListIconPickerContent(
                isUpdatingIcon = false,
                isErrorVisible = false,
                onIconSelected = {},
                modifier = Modifier.height(460.dp),
            )
        }
    }
}

@Preview(
    name = "Icon Picker Error",
    showBackground = true,
    widthDp = 428,
    heightDp = 460,
)
@Composable
private fun ShoppingListIconPickerErrorPreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.iconPickerSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListIconPickerContent(
                isUpdatingIcon = false,
                isErrorVisible = true,
                onIconSelected = {},
                modifier = Modifier.height(460.dp),
            )
        }
    }
}

@Preview(
    name = "Icon Picker Dark",
    showBackground = true,
    widthDp = 428,
    heightDp = 460,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ShoppingListIconPickerDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.iconPickerSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListIconPickerContent(
                isUpdatingIcon = false,
                isErrorVisible = false,
                onIconSelected = {},
                modifier = Modifier.height(460.dp),
            )
        }
    }
}

@Preview(
    name = "Rename List Dialog",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun RenameShoppingListDialogPreview() {
    Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            RenameShoppingListDialogContent(
                listName = "Покупки на выходные",
                isRenamingList = false,
                onListNameChange = {},
                onDismiss = {},
                onSaveClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Main.addDialogHorizontalPadding),
            )
        }
    }
}

@Preview(
    name = "Rename List Dialog Dark",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun RenameShoppingListDialogDarkPreview() {
    Theme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            RenameShoppingListDialogContent(
                listName = "Покупки на выходные",
                isRenamingList = false,
                onListNameChange = {},
                onDismiss = {},
                onSaveClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Main.addDialogHorizontalPadding),
            )
        }
    }
}

@Preview(
    name = "Confirmation Dialog Light",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun ConfirmationDialogLightPreview() {
    Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            ConfirmationDialogContent(
                title = "Удалить все списки?",
                confirmText = "Удалить",
                cancelText = "Отмена",
                onConfirm = {},
                onDismiss = {},
            )
        }
    }
}

@Preview(
    name = "Confirmation Dialog Dark",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ConfirmationDialogDarkPreview() {
    Theme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            ConfirmationDialogContent(
                title = "Удалить все списки?",
                confirmText = "Удалить",
                cancelText = "Отмена",
                onConfirm = {},
                onDismiss = {},
            )
        }
    }
}
