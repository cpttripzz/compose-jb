package me.zerskine.mgrok.common.root.integration

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import me.zerskine.mgrok.common.edit.TodoEdit
import me.zerskine.mgrok.common.edit.TodoEdit.Model
import me.zerskine.mgrok.common.edit.TodoEdit.Output

class TodoEditFake(
    val itemId: Long,
    val output: Consumer<Output>
) : TodoEdit {

    override val models: Value<Model> get() = TODO("Not used")

    override fun onTextChanged(text: String) {
    }

    override fun onDoneChanged(isDone: Boolean) {
    }

    override fun onCloseClicked() {
    }
}