package me.zerskine.mgrok.common.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import me.zerskine.mgrok.common.main.MgrokItem
import me.zerskine.mgrok.common.main.MgrokMain
import me.zerskine.mgrok.common.main.MgrokMain.Model

@Preview
@Composable
fun MgrokMainContentPreview() {
    MgrokMainContent(MgrokMainPreview())
}

class MgrokMainPreview : MgrokMain {
    override val models: Value<Model> =
        MutableValue(
            Model(
                items = List(5) { index ->
                    MgrokItem(
                        id = index.toLong(),
                        text = "Item $index",
                        isDone = index % 2 == 0
                    )
                },
                text = "Some text"
            )
        )

    override fun onItemClicked(id: Long) {}
    override fun onItemDoneChanged(id: Long, isDone: Boolean) {}
    override fun onItemDeleteClicked(id: Long) {}
    override fun onInputTextChanged(text: String) {}
    override fun onAddItemClicked() {}
}
