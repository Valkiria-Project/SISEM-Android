package com.skgtecnologia.sisem.ui.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.components.dropdown.DropDownItemUiModel
import com.valkiria.uicomponents.components.header.HeaderUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle

@Suppress("LongMethod")
@Composable
fun DropDownContent(
    headerModel: HeaderUiModel,
    defaultSelected: String?,
    itemList: List<DropDownItemUiModel>,
    onAction: (item: DropDownItemUiModel) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (header, searchHeader, body, footer) = createRefs()

        HeaderSection(
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            headerUiModel = headerModel
        )

        var text by rememberSaveable {
            mutableStateOf(itemList.find { it.id == defaultSelected }?.name.orEmpty())
        }

        var selectedItem by remember {
            mutableStateOf(itemList.find { it.id == defaultSelected })
        }

        OutlinedTextField(
            modifier = Modifier
                .constrainAs(searchHeader) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            value = text,
            onValueChange = { updatedValue ->
                text = updatedValue
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(25.dp)
        )

        if (text.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .constrainAs(body) {
                        top.linkTo(searchHeader.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(footer.top)
                        height = Dimension.preferredWrapContent
                    },
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(itemList.filter { it.name.startsWith(text, true) }) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                text = it.name
                                selectedItem = it
                            },
                        text = it.name,
                        textAlign = TextAlign.Start
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .constrainAs(body) {
                        top.linkTo(searchHeader.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(footer.top)
                        height = Dimension.preferredWrapContent
                    },
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(itemList) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                text = it.name
                                selectedItem = it
                            },
                        text = it.name,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

        Button(
            enabled = selectedItem != null,
            onClick = { onAction(selectedItem!!) },
            modifier = Modifier
                .constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.wounds_save_cta),
                style = TextStyle.HEADLINE_3.toTextStyle()
            )
        }
    }
}
