package com.practicum.shoppinglist.presentation.ui.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.theme.colors

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(Dimens.Onboarding.logoTopPadding))
        Image(
            painter = painterResource(
                id = if (isDarkTheme) {
                    R.drawable.onboarding_logo_dark
                } else {
                    R.drawable.onboarding_logo
                },
            ),
            contentDescription = stringResource(id = R.string.onboarding_logo_content_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Onboarding.logoHorizontalPadding)
                .aspectRatio(Dimens.Onboarding.logoAspectRatio),
        )
        Spacer(modifier = Modifier.height(Dimens.Onboarding.illustrationTopPadding))
        Image(
            painter = painterResource(
                id = if (isDarkTheme) {
                    R.drawable.onboarding_illustration_dark
                } else {
                    R.drawable.onboarding_illustration
                },
            ),
            contentDescription = stringResource(id = R.string.onboarding_illustration_content_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Onboarding.illustrationHorizontalPadding)
                .aspectRatio(Dimens.Onboarding.illustrationAspectRatio),
        )
        Spacer(modifier = Modifier.height(Dimens.Onboarding.titleTopPadding))
        Text(
            text = stringResource(id = R.string.onboarding_welcome_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Dimens.Onboarding.textHorizontalPadding),
        )
        Spacer(modifier = Modifier.height(Dimens.Onboarding.bodyTopPadding))
        Text(
            text = stringResource(id = R.string.onboarding_welcome_subtitle),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Dimens.Onboarding.textHorizontalPadding),
        )
        Spacer(modifier = Modifier.weight(1f))
        CircularProgressIndicator(
            modifier = Modifier
                .padding(bottom = Dimens.Onboarding.loaderBottomPadding)
                .size(Dimens.Onboarding.loaderSize),
            color = MaterialTheme.colors.authButtonLoader,
            strokeWidth = Dimens.Onboarding.loaderStrokeWidth,
        )
    }
}

@Preview(
    name = "Light",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun OnboardingScreenLightPreview() {
    Theme {
        OnboardingScreen()
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
private fun OnboardingScreenDarkPreview() {
    Theme(darkTheme = true) {
        OnboardingScreen(isDarkTheme = true)
    }
}
