package com.valkiria.uicomponents.bricks.connectivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.theme.Carnation
import com.valkiria.uicomponents.theme.DodgerBlue
import com.valkiria.uicomponents.theme.Shark
import com.valkiria.uicomponents.theme.TreePoppy

/**
 * Tells the crew the app is working without signal and how much is still waiting to go out.
 *
 * Draws nothing when there is nothing to say, so with signal and an empty queue every screen is
 * laid out exactly as it was before. It sits above the screens and pads for the status bar
 * itself, since most screens do not.
 */
@Composable
fun ConnectivityBanner(
    isOffline: Boolean,
    pendingCount: Int,
    rejectedCount: Int,
    modifier: Modifier = Modifier
) {
    val notice = connectivityNotice(isOffline, pendingCount, rejectedCount) ?: return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(notice.containerColor)
            .statusBarsPadding()
    ) {
        Text(
            text = notice.text(),
            color = notice.contentColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

/** Whether the banner shows at all: the same rule the banner itself follows. */
fun isConnectivityBannerVisible(isOffline: Boolean, pendingCount: Int, rejectedCount: Int) =
    connectivityNotice(isOffline, pendingCount, rejectedCount) != null

private class ConnectivityNotice(
    val containerColor: Color,
    val contentColor: Color,
    val text: @Composable () -> String
)

/**
 * Rejected records come first: they will not fix themselves and need someone to look at them.
 * Then being offline, which changes how the crew should expect the app to behave. Records being
 * sent last, as it is the one that resolves on its own.
 */
private fun connectivityNotice(
    isOffline: Boolean,
    pendingCount: Int,
    rejectedCount: Int
): ConnectivityNotice? = when {
    rejectedCount > 0 -> ConnectivityNotice(Carnation, Color.White) {
        pluralStringResource(R.plurals.connectivity_rejected, rejectedCount, rejectedCount)
    }

    isOffline && pendingCount > 0 -> ConnectivityNotice(TreePoppy, Shark) {
        pluralStringResource(R.plurals.connectivity_offline_pending, pendingCount, pendingCount)
    }

    isOffline -> ConnectivityNotice(TreePoppy, Shark) {
        stringResource(R.string.connectivity_offline)
    }

    pendingCount > 0 -> ConnectivityNotice(DodgerBlue, Shark) {
        pluralStringResource(R.plurals.connectivity_syncing, pendingCount, pendingCount)
    }

    else -> null
}

@Preview
@Composable
fun ConnectivityBannerOfflinePreview() {
    ConnectivityBanner(isOffline = true, pendingCount = 0, rejectedCount = 0)
}

@Preview
@Composable
fun ConnectivityBannerOfflinePendingPreview() {
    ConnectivityBanner(isOffline = true, pendingCount = 3, rejectedCount = 0)
}

@Preview
@Composable
fun ConnectivityBannerSyncingPreview() {
    ConnectivityBanner(isOffline = false, pendingCount = 1, rejectedCount = 0)
}

@Preview
@Composable
fun ConnectivityBannerRejectedPreview() {
    ConnectivityBanner(isOffline = false, pendingCount = 0, rejectedCount = 2)
}
