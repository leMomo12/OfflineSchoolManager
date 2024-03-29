package com.mnowo.offlineschoolmanager.core.feature_core.presentation.dialogs

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.mnowo.offlineschoolmanager.R
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.domain.models.Subject
import com.mnowo.offlineschoolmanager.core.theme.LightBlue
import com.mnowo.offlineschoolmanager.feature_todo.domain.use_case.util.FormatDate
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoEvent
import com.mnowo.offlineschoolmanager.feature_todo.presentation.ToDoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onDeleteClicked: () -> Unit,
    title: String,
    text: String
) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            Text(text = title)
        },
        text = {
            Column() {
                Text(text = text)
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    Text(text = stringResource(R.string.no))
                }
                OutlinedButton(
                    onClick = { onDeleteClicked() },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            }
        }, shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun SubjectPickerDialog(
    onSubjectPicked: (Subject) -> Unit,
    onDismissRequest: () -> Unit,
    onAddNewSubjectClicked: () -> Unit,
    fredoka: FontFamily,
    subjectsList: List<Subject>
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.pickSubject),
                        fontFamily = fredoka,
                        fontWeight = FontWeight.Medium,
                        fontSize = 25.sp
                    )
                    CancelIconButton(onDismissRequest = { onDismissRequest() })
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                LazyColumn {
                    item {
                        PickSubjectNewSubjectItem(
                            onAddNewSubjectClicked = {
                                onAddNewSubjectClicked()
                                onDismissRequest()
                            },
                            fredoka = fredoka
                        )
                    }
                    items(subjectsList) {
                        PickSubjectItem(
                            data = it,
                            fredoka = fredoka,
                            onSubjectPicked = { subject ->
                                onSubjectPicked(subject)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PickSubjectItem(data: Subject, fredoka: FontFamily, onSubjectPicked: (Subject) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSubjectPicked(data)
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = Color(
                        red = data.color.red,
                        green = data.color.green,
                        blue = data.color.blue
                    )
                )
        )
        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
        Text(
            text = data.subjectName,
            fontFamily = fredoka,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun PickSubjectNewSubjectItem(onAddNewSubjectClicked: () -> Unit, fredoka: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAddNewSubjectClicked()
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.AddCircleOutline, contentDescription = "", tint = LightBlue)
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Text(
            text = stringResource(R.string.addNewSubject),
            fontFamily = fredoka,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun CancelIconButton(onDismissRequest: () -> Unit) {
    IconButton(onClick = { onDismissRequest() }) {
        Icon(Icons.Outlined.Cancel, contentDescription = "")
    }
}

@Composable
fun DatePicker(
    fredoka: FontFamily,
    onDateSelected: (Date) -> Unit,
    datePickerState: Boolean,
    onDismissRequest: () -> Unit,
    dateText: String
) {
    if (datePickerState) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f), shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.selectDate),
                            fontFamily = fredoka,
                            fontWeight = FontWeight.Medium
                        )
                        OutlinedButton(onClick = {
                            onDismissRequest()
                        }) {
                            Text(text = stringResource(R.string.apply))
                        }
                    }
                    Text(
                        text = dateText,
                        fontFamily = fredoka,
                        modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
                    )
                    Divider()
                    AndroidView(
                        { CalendarView(it) },
                        modifier = Modifier.wrapContentWidth(),
                        update = { views ->
                            views.setOnDateChangeListener { _, year, month, dayOfMonth ->
                                onDateSelected(
                                    Calendar.getInstance().apply {
                                        set(year, month, dayOfMonth)
                                    }.time
                                )
                            }
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ConfettiDialog(onDismiss: () -> Unit, fredoka: FontFamily, visibility: Boolean) {
    val textVisibility = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        textVisibility.value = true
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            shape = RoundedCornerShape(16.dp)
        ) {
            AnimatedVisibility(
                visible = textVisibility.value,
                enter = expandHorizontally(animationSpec = tween(durationMillis = 1000)) + fadeIn(
                    animationSpec = tween(durationMillis = 1300)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(R.string.goodJob),
                        fontSize = 35.sp,
                        fontFamily = fredoka,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(
                        text = stringResource(R.string.keepUpWithTheGoodGrades),
                        fontSize = 28.sp,
                        fontFamily = fredoka,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                }
            }
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(
                    Party(
                        angle = 45,
                        speed = 0f,
                        maxSpeed = 30f,
                        damping = 0.9f,
                        spread = 180,
                        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                        position = Position.Relative(0.0, 0.0),
                        emitter = Emitter(duration = 4000, TimeUnit.MILLISECONDS).perSecond(50)
                    ),
                    Party(
                        angle = 135,
                        speed = 0f,
                        maxSpeed = 30f,
                        damping = 0.9f,
                        spread = 180,
                        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                        position = Position.Relative(1.0, 0.0),
                        emitter = Emitter(duration = 4000, TimeUnit.MILLISECONDS).perSecond(50)
                    )
                )
            )
        }
    }

}