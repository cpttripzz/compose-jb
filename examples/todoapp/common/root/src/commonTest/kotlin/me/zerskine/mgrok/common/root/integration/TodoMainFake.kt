package me.zerskine.mgrok.common.root.integration

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import me.zerskine.mgrok.common.main.MgrokMain
import me.zerskine.mgrok.common.main.MgrokMain.Model
import me.zerskine.mgrok.common.main.MgrokMain.Output

class MgrokMainFake(
    val output: Consumer<Output>
) : MgrokMain {

    override val models: Value<Model> get() = MGROK("Not used")

    override fun onItemClicked(id: Long) {
    }

    override fun onItemDoneChanged(id: Long, isDone: Boolean) {
    }

    override fun onItemDeleteClicked(id: Long) {
    }

    override fun onInputTextChanged(text: String) {
    }

    override fun onAddItemClicked() {
    }
}