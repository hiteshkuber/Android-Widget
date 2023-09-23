package com.example.composewidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

object CustomWidget: GlanceAppWidget() {

    val countKey = intPreferencesKey("count")

    @Composable
    override fun Content() {
        var count = currentState(key = countKey) ?: 0

        Column(
            modifier = GlanceModifier.fillMaxSize().background(Color.DarkGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(Color.Green),
                    fontSize = 26.sp
                )
            )
            Button(
                text = "Increment",
                onClick = actionRunCallback(IncrementActionCallback::class.java)
            )
        }
    }
}

class SimpleCounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CustomWidget

}

class IncrementActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) {
            val currentCount = it[CustomWidget.countKey]

            if(currentCount != null) {
                it[CustomWidget.countKey] = currentCount + 1
            } else {
                it[CustomWidget.countKey] = 1
            }
        }

        CustomWidget.update(context, glanceId)
    }

}