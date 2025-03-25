package com.example.csiappcompose.pages.Chat

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import com.example.csiappcompose.R
import kotlin.Int

@Composable
fun popUpWindowAnimated(
    isPopUpOpen: MutableState<Boolean> = mutableStateOf(true),
    selected: MutableState<String> = mutableStateOf("Nothing"),
    id:Int?,
    reactMessage:(String, Int)-> Unit,

) {
    val list =listOf("Like", "Cool","HaHa", "Heart", "Anger",  "Smile", "Sad")

    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 0),
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = {
            Log.d("PopupEvent", "Popup Dismissed")
            isPopUpOpen.value = false
        }
    ) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .scrollable(orientation = Orientation.Horizontal, state = rememberScrollState()),
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                list.forEach { emoji ->
                    emojiAnimation(emoji, emoji, isPopUpOpen, selected,id,reactMessage)
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Log.i("SEL", "popUpWindowAnimated: ${selected.value}")


            }
        }
    }
}


fun vibrateDevice(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(100)
    }
}




@Composable
fun emojiAnimation(
    animation: String,
    label: String,
    isPopUpOpen: MutableState<Boolean> = mutableStateOf(true),
    selected: MutableState<String> = mutableStateOf("Nothing"),
    id:Int?,
    reactMessage:(String, Int)-> Unit,
) {

    var isPlaying = remember { mutableStateOf(true) }
    val compositionval = rememberLottieComposition(LottieCompositionSpec.RawRes(getLottieResId(animation)))
    val composition=compositionval.value
    var restartTrigger = remember { mutableStateOf(false) } // Restart trigger

    val animationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying.value
    )


    LaunchedEffect(animationState.isAtEnd, restartTrigger.value) {
        if (animationState.isAtEnd) {
            isPlaying.value = false
            delay(3000) // Pause for 3 seconds
            isPlaying.value = true
            restartTrigger.value = !restartTrigger.value
        }
    }
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.5f))
            .clickable {
                Log.d("ClickEvent", "Emoji Clicked: $label")

                selected.value = label
                isPopUpOpen.value = false

                Log.d("ClickEvent", "Emoji Clicked: ${selected.value}")
              if(id!=null)
                reactMessage(label,id)
                isPlaying.value=false
            },
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { animationState.progress }
        )
    }
}


@Composable
fun getLottieResId(fileName: String): Int {
    return when (fileName) {
        "Like"-> R.raw.thumbsup1_anim
        "Anger"->R.raw.angry_anim
        "HaHa"->R.raw.ha_ha_anim
        "Heart"->R.raw.heart_anim
        "Smile"->R.raw.smile_anim
        "Sad"->R.raw.sad_anim
        "Cool"->R.raw.cool_anim
        else -> R.raw.heart_anim
    }

}
