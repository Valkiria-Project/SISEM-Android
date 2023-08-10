package com.skgtecnologia.sisem.ui.menu.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skgtecnologia.sisem.R.drawable
import com.valkiria.uicomponents.props.TextStyle.BUTTON_1
import com.valkiria.uicomponents.props.TextStyle.HEADLINE_5
import com.valkiria.uicomponents.props.toTextStyle

@Suppress("LongMethod")
@Composable
fun MenuHeaderComponent(
    modifier: Modifier,
    menuItemsPersonal: List<DrawerItemInfoPersonal>,
    onLogout: (DrawerItemInfoPersonal) -> Unit
) {
    Column(modifier = modifier) {
        Row {
            Icon(
                painter = painterResource(id = drawable.ic_ambulance),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 33.dp, top = 30.dp, bottom = 10.dp)
                    .width(64.096.dp)
                    .height(54.342.dp),
                tint = Color(android.graphics.Color.parseColor("#EB8FEB")) // FIXME: All
            )
            Column {
                Text(
                    text = "SMSOO",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 30.dp),
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.W700,
                        lineHeight = 28.sp,
                        letterSpacing = (2).sp,
                        color = Color(android.graphics.Color.parseColor("#EB8FEB"))
                    )
                )
                Text(
                    text = "5185",
                    modifier = Modifier.padding(
                        start = 20.dp
                    ),
                    style = HEADLINE_5.toTextStyle(),
                    color = Color(android.graphics.Color.parseColor("#EB8FEB"))
                )
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .padding(start = 32.dp, top = 10.dp, end = 32.dp)
                .fillMaxWidth()
        ) {
            val (subRedText, functionalUnitText, baseText) = createRefs()
            Text(
                "Subred: Norte",
                modifier = Modifier.constrainAs(subRedText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = BUTTON_1.toTextStyle()
            )
            Text(
                "Base:",
                modifier = Modifier.constrainAs(functionalUnitText) {
                    top.linkTo(subRedText.bottom)
                    start.linkTo(parent.start)
                },
                style = BUTTON_1.toTextStyle()
            )
            Text(
                "Unidad funcional",
                modifier = Modifier
                    .constrainAs(baseText) {
                        end.linkTo(parent.end)
                    },
                style = BUTTON_1.toTextStyle(),
                textAlign = TextAlign.End
            )
        }

        Divider(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 33.dp, end = 20.dp, top = 15.dp
                ),
            horizontalAlignment = Alignment.Start,
        ) {
            menuItemsPersonal.forEach {
                CrewMemberItem(item = it) {
                    onLogout(it)
                }
            }
        }

        Divider(
            modifier = Modifier.padding(start = 33.dp, end = 33.dp, top = 15.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
