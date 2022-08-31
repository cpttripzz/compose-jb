package me.zerskine.mgrok.common.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import me.zerskine.mgrok.common.edit.MgrokEdit
import me.zerskine.mgrok.common.main.MgrokMain

interface MgrokRoot {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Main(val component: MgrokMain) : Child()
        data class Edit(val component: MgrokEdit) : Child()
    }
}
