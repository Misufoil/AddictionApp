package dev.misufoil.core_utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import dev.misufoil.addictions.uikit.R as uikitR

@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    initialValue: LocalDateTime,  // Change this to LocalDateTime
    circleColor: Color,
    secondaryCircleColor: Color,
    circleRadius: Float,
    textStyleInCircle: TextStyle,
    textStyleUnderCircle: TextStyle,
    smallCircle: Boolean,
    onPositionChange: (LocalDateTime) -> Unit // Change this to LocalDateTime
) {
    // Define the milestones
    val milestones = MilestonesProvider.milestones

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var currentTime by remember {
        mutableStateOf(LocalDateTime.now())
    }

    // Use LaunchedEffect to update the time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000L)
        }
    }

    // Calculate time passed since initialValue
    val timePassed = Duration.between(initialValue, currentTime)

    // Find the closest milestone
    val milestoneIndex = milestones.indexOfLast { timePassed >= it }.coerceAtLeast(0)
    val nextMilestone = milestones.getOrNull(milestoneIndex + 1)

    // Calculate progress towards the next milestone
    val progress = if (nextMilestone != null) {
        (timePassed.toMillis().toFloat() / nextMilestone.toMillis().toFloat()) * 100
    } else {
        100f
    }

    val sweepAngle = progress * 3.6f

    val milestoneText = if (nextMilestone != null) {
        stringResource(id = uikitR.string.goal_days, nextMilestone.toDays())
    } else {
        stringResource(id = uikitR.string.goal_achieved)
    }

    var textSize by remember { mutableStateOf(24.sp) }

    Column {
        Box(
            modifier = modifier
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val width = size.width
                val height = size.height
                val circleThickness = width / 20f
                circleCenter = Offset(x = width / 2f, y = height / 2f)


                drawCircle(
                    style = Stroke(
                        width = circleThickness
                    ),
                    color = secondaryCircleColor,
                    radius = circleRadius,
                    center = circleCenter
                )

                drawArc(
                    color = circleColor,
                    startAngle = 90f,
                    sweepAngle = sweepAngle,
                    style = Stroke(
                        width = circleThickness,
                        cap = StrokeCap.Round
                    ),
                    useCenter = false,
                    size = Size(
                        width = circleRadius * 2f,
                        height = circleRadius * 2f
                    ),
                    topLeft = Offset(
                        (width - circleRadius * 2f) / 2f,
                        (height - circleRadius * 2f) / 2f
                    )
                )

                val x =
                    -(circleRadius * sin(Math.toRadians(sweepAngle.toDouble()))).toFloat() + (width / 2)
                val y =
                    (circleRadius * cos(Math.toRadians(sweepAngle.toDouble()))).toFloat() + (height / 2)

                drawCircle(
                    color = Color.White,
                    radius = if (smallCircle) 2.dp.toPx() else 3.dp.toPx(),
                    center = Offset(x, y)
                )

                if (!smallCircle) {
                    val outerRadius = circleRadius + circleThickness / 2f
                    val gap = 15f
                    for (i in 0..100) {
                        val color =
                            if (i < progress) circleColor else circleColor.copy(alpha = 0.3f)
                        val angleInDegrees = i * 3.6f
                        val angleInRad = angleInDegrees * PI / 180f + PI / 2f

                        val yGapAdjustment = cos(angleInDegrees * PI / 180f) * gap
                        val xGapAdjustment = -sin(angleInDegrees * PI / 180f) * gap

                        val start = Offset(
                            x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                            y = (outerRadius * sin(angleInRad) + circleCenter.y + yGapAdjustment).toFloat()
                        )

                        val end = Offset(
                            x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                            y = (outerRadius * sin(angleInRad) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
                        )

                        rotate(
                            angleInDegrees,
                            pivot = start
                        ) {
                            drawLine(
                                color = color,
                                start = start,
                                end = end,

                                strokeWidth = 3.dp.toPx()
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)  // Center Box inside the outer Box
            ) {
                Text(
                    text = formatDurationStyled(
                        timePassed,
                        textStyleInCircle,
                        smallCircle,
                        circleColor
                    ),
                    style = textStyleInCircle
                )
            }
        }
        Text(
            text = milestoneText,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = textStyleUnderCircle,
        )
    }
}


@Composable
fun formatDurationStyled(
    duration: Duration,
    textStyleInCircle: TextStyle,
    circleIsSmall: Boolean,
    circleColor: Color
): AnnotatedString {
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    val letterFontSize =
        (textStyleInCircle.fontSize.value / 2).sp  // Размер шрифта для букв пропорционально меньше

    val letterColor = circleColor.copy(alpha = 0.7f)

    return buildAnnotatedString {
        withStyle(style = textStyleInCircle.toSpanStyle().copy(color = circleColor)) {
            append("%02d".format(days))
        }
        withStyle(
            style = textStyleInCircle.toSpanStyle()
                .copy(color = letterColor, fontSize = letterFontSize)
        ) {
            append(stringResource(id = uikitR.string.D))

        }
        withStyle(style = textStyleInCircle.toSpanStyle().copy(color = circleColor)) {
            append("%02d".format(hours))
        }
        withStyle(
            style = textStyleInCircle.toSpanStyle()
                .copy(color = letterColor, fontSize = letterFontSize)
        ) {
            append(stringResource(id = uikitR.string.H))
        }
        withStyle(style = textStyleInCircle.toSpanStyle().copy(color = circleColor)) {
            append("%02d".format(minutes))
        }
        withStyle(
            style = textStyleInCircle.toSpanStyle()
                .copy(color = letterColor, fontSize = letterFontSize)
        ) {
            append(stringResource(id = uikitR.string.M))
        }
        if (!circleIsSmall) {
            withStyle(style = textStyleInCircle.toSpanStyle().copy(color = circleColor)) {
                append("%02d".format(seconds))
            }
            withStyle(
                style = textStyleInCircle.toSpanStyle()
                    .copy(color = letterColor, fontSize = letterFontSize)
            ) {
                append(stringResource(id = uikitR.string.S))
            }
        }
    }
}