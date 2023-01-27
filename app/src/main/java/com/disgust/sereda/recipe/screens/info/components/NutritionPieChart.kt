package com.disgust.sereda.recipe.screens.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData

@Composable
fun NutritionPieChart(
    chartSlices: List<ChartSlice>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        PieChart(
            pieChartData = PieChartData(
                chartSlices.map { it.toSlice() }
            ),
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        )
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            chartSlices.forEach {
                ChartSliceAndColorText(
                    color = it.color,
                    text = "${it.title} (${it.amount} ${it.unit ?: ""})"
                )
            }
        }
    }
}

@Composable
fun ChartSliceAndColorText(color: Color, text: String) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .background(color)
                .border(1.dp, Color.Black)
        )
        Text(text = text, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
    }
}

data class ChartSlice(
    val color: Color,
    val title: String,
    val amount: Float,
    val unit: String? = null
) {
    fun toSlice(): PieChartData.Slice {
        return PieChartData.Slice(
            value = amount,
            color = color
        )
    }
}