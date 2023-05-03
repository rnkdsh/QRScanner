package com.rnkdsh.qrscanner.ui.settings

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rnkdsh.qrscanner.common.DevicePreviews
import com.rnkdsh.qrscanner.ui.theme.MyApplicationTheme

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    SettingsScreen(
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
) {

}

@DevicePreviews
@Composable
fun SettingsScreenPreview() {
    BoxWithConstraints {
        MyApplicationTheme {
            SettingsScreen()
        }
    }
}
