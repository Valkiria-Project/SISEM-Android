package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun CardComponent(
    isNews: Boolean,
    isLogin: Boolean = false,
    hallazgos: List<String> = listOf("prueba1 es demaciado grande", "prueba2", "prueba3", "prueba4", "prueba5"),
    modifier: Modifier = Modifier
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        "ic_ambulance", DefType.DRAWABLE
    )

    val brush = Brush.horizontalGradient(
        colors = listOf(
            Color.Black,
            MaterialTheme.colorScheme.background
        )
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 25.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,

                ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier
                .background(brush = brush)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    iconResourceId?.let {
                        Icon(
                            painter = painterResource(id = iconResourceId),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = "MÉDICO",
                            style = TextStyle.HEADLINE_7.toTextStyle(),
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .background(
                                    color = if (isLogin) {
                                        MaterialTheme.colorScheme.tertiary
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    },
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .padding(horizontal = 8.dp),
                            text = "Anterior: Andres Perez",
                            style = TextStyle.HEADLINE_8.toTextStyle()
                        )
                    }

                    if (isNews) {
                        BadgedBoxView(count = 2)
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = "20/03/2023",
                        style = TextStyle.HEADLINE_8.toTextStyle(),
                        modifier = Modifier.padding(end = 12.dp),
                        color = Color.White
                    )

                    Icon(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = "13:00",
                        style = TextStyle.HEADLINE_8.toTextStyle(),
                        color = Color.White
                    )
                }

                Text(
                    text = "Hallazgos",
                    style = TextStyle.HEADLINE_7.toTextStyle(),
                    modifier = Modifier.padding(top = 12.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardComponentPreview() {
    CardComponent(isNews = true)
}

@Composable
fun BadgedBoxView(count: Int) {
    BadgedBox(
        badge = {
            Badge {
                Text(text = count.toString())
            }
        },
    ) {
        Icon(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 12.dp),
            painter = painterResource(id = R.drawable.ic_news),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun ChipView(text: String) {
    SuggestionChip(
        onClick = { /*TODO*/ },
        modifier = Modifier.wrapContentSize(),
        label = {
            Text(
                text = text,
                style = TextStyle.HEADLINE_8.toTextStyle()
            )
        },
        shape = RoundedCornerShape(25.dp)
    )
}



