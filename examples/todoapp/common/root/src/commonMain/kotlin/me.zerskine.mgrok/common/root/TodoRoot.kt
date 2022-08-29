package me.zerskine.mgrok.common.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import me.zerskine.mgrok.common.edit.TodoEdit
import me.zerskine.mgrok.common.main.TodoMain

interface TodoRoot {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Main(val component: TodoMain) : Child()
        data class Edit(val component: TodoEdit) : Child()
    }
}
