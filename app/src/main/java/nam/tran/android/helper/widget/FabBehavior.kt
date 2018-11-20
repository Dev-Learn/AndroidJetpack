package nam.tran.android.helper.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat


class FabBehavior @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppBarLayout.ScrollingViewBehavior(){

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return super.layoutDependsOn(parent, child, dependency) || dependency is FloatingActionButton
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        directTargetChild: View, target: View, nestedScrollAxes: Int
    ): Boolean {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            nestedScrollAxes
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        if (dyConsumed > 0) {
            // User scrolled down -> hide the FAB
            val dependencies = coordinatorLayout.getDependencies(child)
            for (view in dependencies) {
                if (view is FloatingActionButton) {
                    view.hide()
                }
            }
        } else if (dyConsumed < 0) {
            // User scrolled up -> show the FAB
            val dependencies = coordinatorLayout.getDependencies(child)
            for (view in dependencies) {
                if (view is FloatingActionButton) {
                    view.show()
                }
            }
        }
    }
}