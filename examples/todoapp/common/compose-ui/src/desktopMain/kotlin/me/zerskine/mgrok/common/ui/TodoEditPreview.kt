package me.zerskine.mgrok.common.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import me.zerskine.mgrok.common.edit.TodoEdit
import me.zerskine.mgrok.common.edit.TodoEdit.Model
import me.zerskine.mgrok.common.ui.TodoEditPreview

@Composable
@Preview
fun TodoEditContentPreview() {
    TodoEditContent(TodoEditPreview())
}

class TodoEditPreview : TodoEdit {
    override val models: Value<Model> =
        MutableValue(
            Model(
                text = "Some text",
                isDone = true
            )
        )

    override fun onTextChanged(text: String) {}
    override fun onDoneChanged(isDone: Boolean) {}
    override fun onCloseClicked() {}
}
