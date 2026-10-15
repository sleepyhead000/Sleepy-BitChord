package com.music.bitchord.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SwitchAccount
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.music.bitchord.R
import com.music.bitchord.data.model.Account

/**
 * The Account tab: who is signed in, which profile is listening, and the ways
 * out to Settings and Account & scrobbling.
 *
 * The account's own card heads the page the way it heads the settings sheet —
 * tap either it or the "Listen as" row to raise the switcher, which stays a
 * sheet over the whole app rather than becoming a page of its own: switching
 * profiles is a pick from a short list, not somewhere to live.
 *
 * Signed out, the same card is the sign-in prompt: one tap starts the Google
 * round trip, and every group below it — profiles, settings links — is simply
 * not there until there is an account to apply them to.
 */
@Composable
fun AccountScreen(
    signedIn: Boolean,
    account: Account?,
    channelName: String?,
    onSignIn: () -> Unit,
    onSwitchChannel: () -> Unit,
    onSignOut: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAccountScrobbling: () -> Unit,
    listState: LazyListState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        item(key = "account:heading") {
            Text(
                text = stringResource(R.string.account),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 14.dp),
            )
        }

        // Who is signed in and which profile is listening, with the way out.
        item(key = "account:who") {
            SettingsGroup(
                footer = if (signedIn) stringResource(R.string.account_profiles_help) else null,
                topSpacing = 0.dp,
            ) {
                AccountCard(
                    signedIn = signedIn,
                    account = account,
                    onSignIn = onSignIn,
                    onClick = if (signedIn) onSwitchChannel else null,
                    grouped = true,
                )
                if (signedIn) {
                    RowDivider()
                    SettingsRow(
                        icon = Icons.Rounded.SwitchAccount,
                        title = stringResource(R.string.listen_as),
                        subtitle = channelName ?: stringResource(R.string.default_youtube_profile),
                        onClick = onSwitchChannel,
                    )
                    RowDivider()
                    DestructiveRow(label = stringResource(R.string.sign_out), onClick = onSignOut)
                }
            }
        }

        // The same two pages Settings reaches, one tap closer from here: the
        // account's integrations and its scrobbler credentials live on their
        // own screen rather than inside the settings sheet.
        if (signedIn) {
            item(key = "account:links") {
                SettingsGroup {
                    SettingsRow(
                        icon = Icons.Rounded.Settings,
                        title = stringResource(R.string.settings),
                        onClick = onOpenSettings,
                    )
                    RowDivider()
                    SettingsRow(
                        icon = Icons.Rounded.Person,
                        title = stringResource(R.string.account_scrobbling),
                        onClick = onOpenAccountScrobbling,
                    )
                }
            }
        }

        item(key = "account:spacer") { Spacer(Modifier.height(24.dp)) }
    }
}
