package me.zerskine.mgrok.common.root.integration

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import me.zerskine.mgrok.common.edit.MgrokEdit
import me.zerskine.mgrok.common.edit.MgrokEdit.Model
import me.zerskine.mgrok.common.edit.MgrokEdit.Output

class MgrokEditFake(
    val itemId: Long,
    val output: Consumer<Output>
) : MgrokEdit {

    override val models: Value<Model> get() = MGROK("Not used")

    override fun onTextChanged(text: String) {
    }

    override fun onDoneChanged(isDone: Boolean) {
    }

    override fun onCloseClicked() {
    }
}