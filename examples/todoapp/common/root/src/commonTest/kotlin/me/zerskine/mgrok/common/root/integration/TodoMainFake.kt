package me.zerskine.mgrok.common.root.integration

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import me.zerskine.mgrok.common.main.TodoMain
import me.zerskine.mgrok.common.main.TodoMain.Model
import me.zerskine.mgrok.common.main.TodoMain.Output

class TodoMainFake(
    val output: Consumer<Output>
) : TodoMain {

    override val models: Value<Model> get() = TODO("Not used")

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