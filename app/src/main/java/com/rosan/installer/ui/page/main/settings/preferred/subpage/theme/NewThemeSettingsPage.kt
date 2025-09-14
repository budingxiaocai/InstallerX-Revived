package com.rosan.installer.ui.page.main.settings.preferred.subpage.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kieronquinn.monetcompat.core.MonetCompat
import com.rosan.installer.R
import com.rosan.installer.ui.icons.AppIcons
import com.rosan.installer.ui.page.main.settings.preferred.PreferredViewAction
import com.rosan.installer.ui.page.main.settings.preferred.PreferredViewModel
import com.rosan.installer.ui.page.main.widget.dialog.HideLauncherIconWarningDialog
import com.rosan.installer.ui.page.main.widget.setting.AppBackButton
import com.rosan.installer.ui.page.main.widget.setting.BaseWidget
import com.rosan.installer.ui.page.main.widget.setting.BottomSheetContent
import com.rosan.installer.ui.page.main.widget.setting.SelectableSettingItem
import com.rosan.installer.ui.page.main.widget.setting.SplicedColumnGroup
import com.rosan.installer.ui.page.main.widget.setting.SwitchWidget

// This is now a top-level composable, likely in its own file.
// It takes NavController instead of an onBack lambda.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewThemeSettingsPage(
    navController: NavController,
    viewModel: PreferredViewModel,
) {
    val state = viewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showHideLauncherIconDialog by remember { mutableStateOf(false) }
    var showWallpaperColorPickerDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var wallpaperColors by remember { mutableStateOf<List<Int>?>(null) }

    fun getBlackLevel(color: Int): Int {
        val r = (color shr 16) and 0xFF
        val g = (color shr 8)  and 0xFF
        val b =  color         and 0xFF
        return (r * 299 + g * 587 + b * 114) / 1000
    }

    HideLauncherIconWarningDialog(
        show = showHideLauncherIconDialog,
        onDismiss = { showHideLauncherIconDialog = false },
        onConfirm = {
            showHideLauncherIconDialog = false
            viewModel.dispatch(PreferredViewAction.ChangeShowLauncherIcon(false))
        }
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            TopAppBar(
                windowInsets = TopAppBarDefaults.windowInsets.add(WindowInsets(left = 12.dp)),
                title = { Text(stringResource(R.string.theme_settings)) },
                navigationIcon = {
                    AppBackButton(
                        onClick = { navController.navigateUp() },
                        icon = Icons.AutoMirrored.TwoTone.ArrowBack,
                        modifier = Modifier.size(36.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceBright
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SplicedColumnGroup(
                    title = stringResource(R.string.theme_settings_ui_style),
                    content = listOf(
                        {
                            // Option 1: Google UI
                            SelectableSettingItem(
                                title = stringResource(R.string.theme_settings_google_ui),
                                description = stringResource(R.string.theme_settings_google_ui_desc),
                                selected = !state.showMiuixUI,
                                onClick = {
                                    if (state.showMiuixUI) { // Only dispatch if changing state
                                        viewModel.dispatch(PreferredViewAction.ChangeUseMiuix(false))
                                    }
                                }
                            )
                        },
                        {
                            // Option 2: MIUIX UI
                            SelectableSettingItem(
                                title = stringResource(R.string.theme_settings_miuix_ui),
                                description = stringResource(R.string.theme_settings_miuix_ui_desc),
                                selected = state.showMiuixUI,
                                onClick = {
                                    if (!state.showMiuixUI) { // Only dispatch if changing state
                                        viewModel.dispatch(PreferredViewAction.ChangeUseMiuix(true))
                                    }
                                }
                            )
                        }
                    )
                )
            }

            // --- Group 2: Google UI Style Options (Legacy vs Expressive) ---
            // Only show this section if Google UI is selected
            if (!state.showMiuixUI) {
                item {
                    SplicedColumnGroup(
                        title = stringResource(R.string.theme_settings_google_ui),
                        content = listOf {
                            SwitchWidget(
                                icon = AppIcons.Theme,
                                title = stringResource(R.string.theme_settings_use_expressive_ui),
                                description = stringResource(R.string.theme_settings_use_expressive_ui_desc),
                                checked = state.showExpressiveUI,
                                onCheckedChange = {
                                    viewModel.dispatch(PreferredViewAction.ChangeShowExpressiveUI(it))
                                }
                            )
                        }
                    )
                }
            }
            item {
                SplicedColumnGroup(
                    title = stringResource(R.string.theme_settings_launcher_icons),
                    content = listOf {
                        SwitchWidget(
                            icon = AppIcons.BugReport,
                            title = stringResource(R.string.theme_settings_hide_launcher_icon),
                            description = stringResource(R.string.theme_settings_hide_launcher_icon_desc),
                            checked = !state.showLauncherIcon,
                            onCheckedChange = { newCheckedState ->
                                if (newCheckedState) {
                                    showHideLauncherIconDialog = true
                                } else {
                                    viewModel.dispatch(PreferredViewAction.ChangeShowLauncherIcon(true))
                                }
                            }
                        )
                    }
                )
            }
            item {
                SplicedColumnGroup(
                    title = stringResource(R.string.theme_settings_ui_color),
                    content = listOf {
                        BaseWidget(
                            icon = AppIcons.PaintBrush,
                            title = stringResource(R.string.theme_settings_wallpaper_color_picker),
                            description = stringResource(R.string.theme_settings_wallpaper_color_picker_desc),
                            onClick = {
                                showWallpaperColorPickerDialog = true
                            }
                        ) {}
                    }
                )
            }
        }
    }

    if (showWallpaperColorPickerDialog) {
        LaunchedEffect(Unit) {
            wallpaperColors = MonetCompat.getInstance().getAvailableWallpaperColors()
        }

        selectedIndex = if (wallpaperColors?.contains(state.wallpaperColor) == true) {
            wallpaperColors!!.indexOf(state.wallpaperColor)
        } else 0

        ModalBottomSheet(
            onDismissRequest = {
                showWallpaperColorPickerDialog = false
                wallpaperColors?.let {
                    if (selectedIndex < 0 || selectedIndex >= it.size) selectedIndex = 0
                    viewModel.dispatch(PreferredViewAction.ChangeWallpaperColor(it[selectedIndex]))
                }

                selectedIndex = -1
                wallpaperColors = null
            }
        ) {
            BottomSheetContent(
                title = stringResource(R.string.theme_settings_wallpaper_color_picker)
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp)
                ) {
                    wallpaperColors?.forEachIndexed { index, wallpaperColor ->
                        val isSelected = index == selectedIndex

                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(Color(wallpaperColor))
                                .clickable {
                                    selectedIndex = index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.animation.AnimatedVisibility(visible = isSelected) {
                                Icon(
                                    imageVector = AppIcons.Check,
                                    contentDescription = null,
                                    tint = if (getBlackLevel(wallpaperColor) > 128) Color.Black else Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}