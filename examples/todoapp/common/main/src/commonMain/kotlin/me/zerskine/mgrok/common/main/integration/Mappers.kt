package me.zerskine.mgrok.common.main.integration

import me.zerskine.mgrok.common.main.TodoMain.Model
import me.zerskine.mgrok.common.main.store.TodoMainStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            items = it.items,
            text = it.text
        )
    }