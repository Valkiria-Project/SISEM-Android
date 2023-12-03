package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun StaggeredCardsComponent(
    uiModel: StaggeredCardsUiModel
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = uiModel.modifier.height(300.dp)
    ) {
        itemsIndexed(uiModel.cards) { _, card ->
            StaggeredCardView(
                modifier = Modifier
                    .padding(10.dp),
                staggeredCardList = card
            )
        }
    }
}

@Composable
private fun StaggeredCardView(
    modifier: Modifier = Modifier,
    staggeredCardList: StaggeredCardListUiModel
) {

    Column(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(color = Color.DarkGray)
    ) {
        staggeredCardList.content.forEach {
            Column(
                modifier = Modifier
                    .padding(10.dp),
            ) {
                Text(
                    text = it.label.text,
                    style = it.label.textStyle.toTextStyle(),
                )

                it.value?.let { value ->
                    Text(
                        text = value.text,
                        style = value.textStyle.toTextStyle(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StaggeredCardViewPreview() {
    StaggeredCardView(
        staggeredCardList = StaggeredCardListUiModel(
            content = listOf(
                StaggeredCardUiModel(
                    label = TextUiModel(
                        text = "Consecutivo",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    value = TextUiModel(
                        text = "0001",
                        textStyle = TextStyle.HEADLINE_5
                    )
                ),
                StaggeredCardUiModel(
                    label = TextUiModel(
                        text = "Registro asistencial",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    value = TextUiModel(
                        text = "0001",
                        textStyle = TextStyle.HEADLINE_5
                    )
                )
            ),
            arrangement = Arrangement.Center
        )
    )
}

@Suppress("LongMethod")
@Preview
@Composable
fun StaggeredCardsComponentPreview() {
    StaggeredCardsComponent(
        uiModel = StaggeredCardsUiModel(
            identifier = "12345",
            cards = listOf(
                StaggeredCardListUiModel(
                    content = listOf(
                        StaggeredCardUiModel(
                            label = TextUiModel(
                                text = "Consecutivo",
                                textStyle = TextStyle.HEADLINE_5
                            ),
                            value = TextUiModel(
                                text = "0001",
                                textStyle = TextStyle.HEADLINE_5
                            )
                        ),
                        StaggeredCardUiModel(
                            label = TextUiModel(
                                text = "Registro asistencial",
                                textStyle = TextStyle.HEADLINE_5
                            ),
                            value = TextUiModel(
                                text = "0001",
                                textStyle = TextStyle.HEADLINE_5
                            )
                        ),
                        StaggeredCardUiModel(
                            label = TextUiModel(
                                text = "Registro asistencial",
                                textStyle = TextStyle.HEADLINE_5
                            ),
                            value = TextUiModel(
                                text = "0001",
                                textStyle = TextStyle.HEADLINE_5
                            )
                        )
                    ),
                    arrangement = Arrangement.Center
                ),
                StaggeredCardListUiModel(
                    content = listOf(
                        StaggeredCardUiModel(
                            label = TextUiModel(
                                text = "Consecutivo",
                                textStyle = TextStyle.HEADLINE_5
                            ),
                            value = TextUiModel(
                                text = "0001",
                                textStyle = TextStyle.HEADLINE_5
                            )
                        ),
                        StaggeredCardUiModel(
                            label = TextUiModel(
                                text = "Registro asistencial",
                                textStyle = TextStyle.HEADLINE_5
                            ),
                            value = TextUiModel(
                                text = "0001",
                                textStyle = TextStyle.HEADLINE_5
                            )
                        )
                    ),
                    arrangement = Arrangement.Center
                ),
                StaggeredCardListUiModel(
                    content = listOf(
                        StaggeredCardUiModel(
                            label = TextUiModel(
                                text = "Consecutivo",
                                textStyle = TextStyle.HEADLINE_5
                            ),
                            value = TextUiModel(
                                text = "0001",
                                textStyle = TextStyle.HEADLINE_5
                            )
                        ),
                        StaggeredCardUiModel(
                            label = TextUiModel(
                                text = "Registro asistencial",
                                textStyle = TextStyle.HEADLINE_5
                            ),
                            value = TextUiModel(
                                text = "0001",
                                textStyle = TextStyle.HEADLINE_5
                            )
                        )
                    ),
                    arrangement = Arrangement.Center
                )
            ),
            arrangement = Arrangement.Center,
            modifier = Modifier
        )
    )
}
