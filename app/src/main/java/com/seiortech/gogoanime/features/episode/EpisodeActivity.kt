package com.seiortech.gogoanime.features.episode

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.seiortech.gogoanime.global.LoadingComponent

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeActivity(
  navController: NavController,
  episodeSlug: String,
  viewModel: EpisodeViewModel = viewModel(),
) {
  Scaffold(
    topBar = {
      TopAppBar(
        colors = topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
          Text("Watch Episode", maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        navigationIcon = {
          IconButton(
            onClick = { navController.popBackStack() }
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        }
      )
    }
  ) { innerPadding ->
    val episodeResponse = viewModel.detailResponse.collectAsState()
    val message = viewModel.message.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(episodeSlug) {
      viewModel.getEpisode(episodeSlug)
    }

    if (isLoading.value) {
      LoadingComponent("Getting Episode Data...")
    } else {
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        if (message.value != null) {
          Log.e("EpisodeActivity", message.value!!)
          Text("Something went wrong, please try again later.")
        }

        val state = rememberWebViewState(url = episodeResponse.value?.data?.urls?.first() ?: "")
        val navigator = rememberWebViewNavigator()
        val chromeClient = remember { CustomChromeClient(ComponentActivity()) }

        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.surface
        ) {
//          WebView(
//            state = state,
//            modifier = Modifier.fillMaxSize(),
//            navigator = navigator,
//            chromeClient = chromeClient, //This is what concerns us for fullscreen mode
//            onCreated = { webView ->
//              webView.settings.javaScriptEnabled = true
//              webView.loadUrl(episodeResponse.value?.data?.urls?.first() ?: "")
//            }
//          )
          AndroidView(
            factory = { context ->
              WebView(context).apply {
                loadUrl(episodeResponse.value?.data?.urls?.first() ?: "")
                settings.javaScriptEnabled = true
              }
            },
          )
        }

        Text(
          text = "Links",
          style = MaterialTheme.typography.titleSmall,
          modifier = Modifier.padding(16.dp, 0.dp),
        )

        episodeResponse.value?.data?.urls?.forEach {
          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            elevation = CardDefaults.cardElevation(
              defaultElevation = 1.dp,
            ),
            modifier = Modifier
              .padding(16.dp, 0.dp)
              .fillMaxWidth()
              .clickable {
                uriHandler.openUri(it)

//                navigator.loadUrl(it)
              }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = "Link ${(episodeResponse.value?.data?.urls?.indexOf(it)?.plus(1)) ?: 0}",
                style = MaterialTheme.typography.titleSmall,
              )
              Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
              )
            }
          }
        }

        Text(
          "Note: For better experience, use browser to watch the episode.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(16.dp, 0.dp),
        )
      }
    }
  }
}

class CustomChromeClient(val activity: ComponentActivity) : AccompanistWebChromeClient() {
  private var customView: View? = null
  override fun onHideCustomView() {
    (activity.window.decorView as FrameLayout).removeView(this.customView)
    this.customView = null
  }

  override fun onShowCustomView(
    paramView: View,
    paramCustomViewCallback: WebChromeClient.CustomViewCallback
  ) {
    if (this.customView != null) {
      onHideCustomView()
      return
    }
    this.customView = paramView
    (activity.window.decorView as FrameLayout).addView(
      this.customView,
      FrameLayout.LayoutParams(-1, -1)
    )
  }
}