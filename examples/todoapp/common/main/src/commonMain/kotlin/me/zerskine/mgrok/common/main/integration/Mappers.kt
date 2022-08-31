package me.zerskine.mgrok.common.main.integration

import me.zerskine.mgrok.common.main.MgrokMain.Model
import me.zerskine.mgrok.common.main.store.MgrokMainStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            items = it.items,
            text = it.text
        )
    }