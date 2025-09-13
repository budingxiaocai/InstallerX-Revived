package com.rosan.installer.ui.page.main.settings.preferred

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.rosan.installer.R
import com.rosan.installer.build.Level
import com.rosan.installer.build.RsConfig
import com.rosan.installer.data.settings.model.room.entity.ConfigEntity
import com.rosan.installer.ui.icons.AppIcons
import com.rosan.installer.ui.page.main.settings.SettingsScreen
import com.rosan.installer.ui.page.main.widget.dialog.ErrorDisplayDialog
import com.rosan.installer.ui.page.main.widget.setting.BottomSheetContent
import com.rosan.installer.ui.page.main.widget.setting.ClearCache
import com.rosan.installer.ui.page.main.widget.setting.DefaultInstaller
import com.rosan.installer.ui.page.main.widget.setting.DisableAdbVerify
import com.rosan.installer.ui.page.main.widget.setting.IgnoreBatteryOptimizationSetting
import com.rosan.installer.ui.page.main.widget.setting.LabelWidget
import com.rosan.installer.ui.page.main.widget.setting.SettingsAboutItemWidget
import com.rosan.installer.ui.page.main.widget.setting.SettingsNavigationItemWidget
import com.rosan.installer.util.openUrl
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferredPage(
    navController: NavController,
    windowInsets: WindowInsets,
    viewModel: PreferredViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        // repeatOnLifecycle will ensure that the action is dispatched only when the lifecycle is in RESUMED state
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            // Dispatch the action to refresh the ignore battery optimization status
            // This will be called when the lifecycle is resumed, ensuring the status is up-to-date
            viewModel.dispatch(PreferredViewAction.RefreshIgnoreBatteryOptimizationStatus)
        }
    }

    val revLevel = when (RsConfig.LEVEL) {
        Level.STABLE -> stringResource(id = R.string.stable)
        Level.PREVIEW -> stringResource(id = R.string.preview)
        Level.UNSTABLE -> stringResource(id = R.string.unstable)
    }

    val snackBarHostState = remember { SnackbarHostState() }
    var errorDialogInfo by remember { mutableStateOf<PreferredViewEvent.ShowErrorDialog?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            snackBarHostState.currentSnackbarData?.dismiss() // Dismiss any existing snackbar
            when (event) {
                is PreferredViewEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }

                is PreferredViewEvent.ShowErrorDialog -> {
                    val snackbarResult = snackBarHostState.showSnackbar(
                        message = event.title,
                        actionLabel = context.getString(R.string.details),
                        duration = SnackbarDuration.Short
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        errorDialogInfo = event
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        contentWindowInsets = windowInsets,
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(text = stringResource(id = R.string.preferred))
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { paddingValues ->
        when (state.progress) {
            is PreferredViewState.Progress.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ContainedLoadingIndicator(
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                        Text(
                            text = stringResource(id = R.string.loading),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }

            is PreferredViewState.Progress.Loaded -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item { LabelWidget(stringResource(R.string.global)) }
                    // pkg { DataAuthorizerWidget(viewModel) }
                    item {
                        SettingsNavigationItemWidget(
                            icon = AppIcons.Theme,
                            title = stringResource(R.string.theme_settings),
                            description = stringResource(R.string.theme_settings_desc),
                            onClick = {
                                // Navigate using NavController instead of changing state
                                navController.navigate(SettingsScreen.Theme.route)
                            }
                        )
                    }
                    item {
                        SettingsNavigationItemWidget(
                            icon = AppIcons.InstallMode,
                            title = stringResource(R.string.installer_settings),
                            description = stringResource(R.string.installer_settings_desc),
                            onClick = {
                                // Navigate using NavController
                                navController.navigate(SettingsScreen.InstallerGlobal.route)
                            }
                        )
                    }
                    item { LabelWidget(stringResource(R.string.basic)) }
                    item {
                        DisableAdbVerify(
                            checked = !state.adbVerifyEnabled,
                            isError = state.authorizer == ConfigEntity.Authorizer.Dhizuku,
                            enabled = state.authorizer != ConfigEntity.Authorizer.Dhizuku,
                            onCheckedChange = { isDisabled ->
                                viewModel.dispatch(
                                    PreferredViewAction.SetAdbVerifyEnabledState(!isDisabled)
                                )
                            }
                        )
                    }
                    item {
                        IgnoreBatteryOptimizationSetting(
                            checked = state.isIgnoringBatteryOptimizations,
                            enabled = !state.isIgnoringBatteryOptimizations,
                        ) { viewModel.dispatch(PreferredViewAction.RequestIgnoreBatteryOptimization) }
                    }
                    item { DefaultInstaller(true) { viewModel.dispatch(PreferredViewAction.SetDefaultInstaller(true)) } }
                    item { DefaultInstaller(false) { viewModel.dispatch(PreferredViewAction.SetDefaultInstaller(false)) } }
                    item { ClearCache() }
                    // pkg { LabelWidget(label = stringResource(id = R.string.more)) }
                    // pkg { UserTerms() }
                    // pkg { PrivacyPolicy() }
                    item { LabelWidget(stringResource(R.string.other)) }
                    item {
                        SettingsAboutItemWidget(
                            imageVector = AppIcons.Info,
                            headlineContentText = stringResource(R.string.about_detail),
                            supportingContentText = "$revLevel ${RsConfig.VERSION_NAME}",
                            onClick = { navController.navigate(SettingsScreen.About.route) }
                        )
                    }
                    item {
                        SettingsAboutItemWidget(
                            imageVector = AppIcons.Update,
                            headlineContentText = stringResource(R.string.get_update),
                            supportingContentText = stringResource(R.string.get_update_detail),
                            onClick = { showBottomSheet = true }
                        )
                    }
                    // Temporarily Disable This
                    /*                    pkg {
                                            SettingsAboutItemWidget(
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_telegram),
                                                headlineContentText = stringResource(R.string.telegram_group),
                                                supportingContentText = stringResource(R.string.telegram_group_desc),
                                                onClick = {
                                                    openUrl(context, "https://t.me/installerx_revived")
                                                }
                                            )
                                        }*/
                }
            }
        }
    }
    errorDialogInfo?.let { dialogInfo ->
        ErrorDisplayDialog(
            exception = dialogInfo.exception,
            onDismissRequest = { errorDialogInfo = null },
            onRetry = {
                errorDialogInfo = null // Dismiss dialog
                viewModel.dispatch(dialogInfo.retryAction) // Dispatch the retry action
            },
            title = dialogInfo.title
        )
    }
    if (showBottomSheet) ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
        BottomSheetContent(
            title = stringResource(R.string.get_update)
        ) {
            val context = LocalContext.current
            val haptic = LocalHapticFeedback.current

            // GitHub 按钮
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                    // 点击按钮时调用 openUrl 工具函数
                    context.openUrl("https://github.com/wxxsfxyzm/InstallerX-Revived/releases")
                },
                modifier = Modifier.fillMaxWidth() // 按钮填充横向宽度
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_github),
                    contentDescription = "GitHub Icon", // 辅助功能描述
                    modifier = Modifier.size(24.dp) // 图标大小
                )
                Spacer(modifier = Modifier.width(8.dp)) // 图标与文字之间的间隔
                Text(text = "GitHub") // 按钮文本
            }
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                    context.openUrl("https://t.me/installerx_revived")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_telegram),
                    contentDescription = "Telegram Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Telegram") // 按钮文本
            }
        }
    }
}

/*@Composable
fun UserTerms() {
    val context = LocalContext.current
    BaseWidget(
        icon = Icons.TwoTone.Gavel,
        title = stringResource(id = R.string.user_terms),
        onClick = {
            openUrl(context, "https://iamr0s.github.io/InstallerXDocs/terms")
        }
    ) {}
}*/

/*
@Composable
fun PrivacyPolicy() {
    val context = LocalContext.current
    BaseWidget(
        icon = Icons.TwoTone.PrivacyTip,
        title = stringResource(id = R.string.privacy_policy),
        onClick = {
            openUrl(context, "https://iamr0s.github.io/InstallerXDocs/privacy")
        }
    ) {}
}*/