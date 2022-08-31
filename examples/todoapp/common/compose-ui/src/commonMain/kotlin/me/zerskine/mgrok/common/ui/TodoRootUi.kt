@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.zerskine.mgrok.common.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import me.zerskine.mgrok.common.root.MgrokRoot
import me.zerskine.mgrok.common.root.MgrokRoot.Child

@Composable
fun MgrokRootContent(component: MgrokRoot) {
    Children(routerState = component.routerState, animation = crossfadeScale()) {
        when (val child = it.instance) {
            is Child.Main -> MgrokMainContent(child.component)
            is Child.Edit -> MgrokEditContent(child.component)
        }
    }
}
