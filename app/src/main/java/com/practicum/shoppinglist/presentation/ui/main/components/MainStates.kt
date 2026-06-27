package com.practicum.shoppinglist.presentation.ui.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens

@Composable
fun MainLoadingState(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun MainErrorState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.Main.emptyStateTextHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.main_error_message),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(Dimens.Main.errorRetryButtonTopPadding))
        TextButton(
            onClick = onRetryClick,
            shape = RoundedCornerShape(Dimens.Main.errorRetryButtonCornerRadius),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        ) {
            Text(text = stringResource(id = R.string.main_retry_button))
        }
    }
}

@Composable
fun MainEmptyState(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(
                id = if (isDarkTheme) {
                    R.drawable.main_empty_state_illustration_dark
                } else {
                    R.drawable.main_empty_state_illustration
                },
            ),
            contentDescription = stringResource(
                id = R.string.main_empty_state_illustration_content_description,
            ),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Main.emptyStateHorizontalPadding)
                .aspectRatio(Dimens.Main.emptyStateIllustrationAspectRatio),
        )
        Spacer(modifier = Modifier.height(Dimens.Main.emptyStateTitleTopPadding))
        Text(
            text = stringResource(id = R.string.main_empty_state_title),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = Dimens.Main.emptyStateTextHorizontalPadding,
            ),
        )
        Spacer(modifier = Modifier.height(Dimens.Main.emptyStateBodyTopPadding))
        Text(
            text = stringResource(id = R.string.main_empty_state_subtitle),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = Dimens.Main.emptyStateTextHorizontalPadding,
            ),
        )
    }
}
