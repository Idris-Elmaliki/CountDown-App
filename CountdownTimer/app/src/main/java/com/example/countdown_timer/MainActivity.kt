package com.example.countdown_timer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.app.Activity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.countdown_timer.ui.MainViewModel
import com.example.countdown_timer.ui.theme.CountdownTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountdownTimerTheme {
                Surface {
                    CountDown()
                }
            }
        }
    }
}

@Composable
fun CountDown() {
    val viewModel = viewModel<MainViewModel>()
    val time = viewModel.countDownFlow.collectAsState(initial = 10)

    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val mp = remember {
        MediaPlayer.create(context, R.raw.confetti)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if(time.value == 0) {
            mp.start()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                TextButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "\uD83C\uDF8AHooray\uD83C\uDF8A",
                            fontSize = 30.sp,
                        )
                        Text(
                            text = "You made it to the End!",
                            fontSize = 30.sp,
                        )
                        Text(
                            text = "(Now click on me to continue)",
                            fontSize = 15.sp
                        )
                    }
                }

                if(expanded)
                    QuitPopup { activity.finish() }
            }

        }
        else {
            Text(
                text = time.value.toString(),
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mp.release()
        }
    }
}

@Composable
private fun QuitPopup(
    onQuit : () -> Unit,
) {
    Dialog(
        onDismissRequest = onQuit
    ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Thank you for your time! Please click the button to quit!",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onQuit) {
                        Text(text = "Quit")
                    }
                }
            }
        }
    }
}