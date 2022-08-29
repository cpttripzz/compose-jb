package me.zerskine.mgrok.common.edit.integration

import me.zerskine.mgrok.common.edit.TodoEdit.Model
import me.zerskine.mgrok.common.edit.store.TodoEditStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            text = it.text,
            isDone = it.isDone
        )
    }
