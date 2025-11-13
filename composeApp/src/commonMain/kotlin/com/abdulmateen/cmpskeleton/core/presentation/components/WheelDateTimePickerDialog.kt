package com.abdulmateen.cmpskeleton.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
@OptIn(ExperimentalTime::class)
@Composable
fun WheelDateTimePickerDialog(
    showDatePicker: Boolean,
    toggleDatePicker: (Boolean) -> Unit,
    onDateSelection: (Instant) -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }

        WheelDatePickerView(
            modifier = Modifier.padding(top = 18.dp, bottom = 10.dp).fillMaxWidth(),
            showDatePicker = showDatePicker,
            titleStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
            ),
            doneLabelStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF007AFF),
            ),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                borderColor = Color.LightGray,
            ),
//            timeFormat = TimeFormat.AM_PM,
//            dateTextColor = Color(0xff007AFF),
            rowCount = 5,
            height = 170.dp,
//            dateTextStyle = TextStyle(
//                fontWeight = FontWeight(600),
//            ),
            onDoneClick = {
                onDateSelection(it.atStartOfDayIn(TimeZone.currentSystemDefault()))
                toggleDatePicker(false)
            },
            dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
            onDismiss = {
                toggleDatePicker(false)
            }
        )
}