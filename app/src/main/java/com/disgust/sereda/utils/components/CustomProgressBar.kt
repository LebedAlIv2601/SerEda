package com.disgust.sereda.utils.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomProgressBar() {

    val color = if (isSystemInDarkTheme()) Color.White else Color.Black

    val numDots = 8
    val currentAngle = remember { mutableStateOf(0f) }
    val previousT = remember { mutableStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition()
    val t by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(120),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = Modifier
            .size(58.dp)
            .padding(8.dp)
    ) {
        withTransform(
            {
                if (previousT.value > t) {
                    currentAngle.value += 45f
                }
                previousT.value = t
                rotate(currentAngle.value)
            },
            {
                val listX = listOf(
                    0.0f, -0.70711f, -1.0f, -0.70711f, 0.0f, 0.70711f, 1.0f, 0.70711f
                )
                val listY = listOf(
                    1.0f, 0.70711f, 0.0f, -0.70711f, -1.0f, -0.70711f, 0.0f, 0.70711f
                )

                val radius = size.minDimension / 2f
                val step = .8f / numDots

                repeat(numDots) { index ->
                    val size = 12.dp * (1f - step * index) / 2

                    val x = radius + radius * listX[index]
                    val y = radius - radius * listY[index]

                    drawCircle(
                        color = color,
                        center = Offset(x, y),
                        radius = size.toPx(),
                        style = Fill,
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewCustomProgressBar() {
    Box() {
        CustomProgressBar()
    }
}


