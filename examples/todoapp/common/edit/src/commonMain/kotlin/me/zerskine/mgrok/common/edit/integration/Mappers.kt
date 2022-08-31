package me.zerskine.mgrok.common.edit.integration

import me.zerskine.mgrok.common.edit.MgrokEdit.Model
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            text = it.text,
            isDone = it.isDone
        )
    }
