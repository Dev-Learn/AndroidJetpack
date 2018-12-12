package nam.tran.android.helper.widget

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Region
import android.util.ArrayMap
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nam.tran.android.helper.databinding.AdapterArticleHeaderBinding
import tran.nam.common.DataBoundViewHolder
import tran.nam.util.Logger

/**
 * ItemDecoration for Pinned Header.
 *
 * porting from https://github.com/beworker/pinned-section-listview
 */
class PinnedHeaderItemDecoration : RecyclerView.ItemDecoration() {

    private var mAdapter: RecyclerView.Adapter<DataBoundViewHolder<ViewDataBinding>>? = null

    // cached data
    // pinned header view
    private var mPinnedHeaderView: View? = null
    private var mHeaderPosition = -1

    private val mPinnedViewTypes = ArrayMap<Int, Boolean>()

    private var mPinnedHeaderTop: Int = 0
    private var mClipBounds: Rect? = null

    interface PinnedHeaderAdapter {
        fun isPinnedViewType(viewType: Int): Boolean
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        createPinnedHeader(parent)
//        Logger.debug("Need to see - 1");

        if (mPinnedHeaderView != null) {
            // check overlap section view.
            //TODO support only vertical header currently.
            val headerEndAt = mPinnedHeaderView!!.top + mPinnedHeaderView!!.height
            val v = parent.findChildViewUnder((c.width / 2).toFloat(), (headerEndAt + 1).toFloat())

            try {
                val result = isHeaderView(parent, v);
                if (result) {
                    mPinnedHeaderTop = v!!.top - mPinnedHeaderView!!.height
                } else {
                    mPinnedHeaderTop = 0
                }

                mClipBounds = c.clipBounds
                mClipBounds!!.top = mPinnedHeaderTop + mPinnedHeaderView!!.height
                c.clipRect(mClipBounds!!)
            }catch (e : Exception){
                Logger.debug(e)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mPinnedHeaderView != null) {
            c.save()

            mClipBounds!!.top = 0
            c.clipRect(mClipBounds!!, Region.Op.UNION)
            c.translate(0f, mPinnedHeaderTop.toFloat())
            mPinnedHeaderView!!.draw(c)
//            Logger.debug("Need to see - 2: " + (mPinnedHeaderView as TextView).text)

            c.restore()
        }
    }

    private fun createPinnedHeader(parent: RecyclerView) {
        checkCache(parent)

        // get LinearLayoutManager.
        val linearLayoutManager: LinearLayoutManager
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            linearLayoutManager = layoutManager
        } else {
            return
        }

        val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
        val headerPosition = findPinnedHeaderPosition(firstVisiblePosition)

        if (headerPosition >= 0 && mHeaderPosition != headerPosition) {
            mHeaderPosition = headerPosition
            val viewType = mAdapter!!.getItemViewType(headerPosition)

            val pinnedViewHolder = mAdapter!!.createViewHolder(parent, viewType)
            mAdapter!!.bindViewHolder(pinnedViewHolder, headerPosition)
            mPinnedHeaderView = pinnedViewHolder.binding.root

            // read layout parameters
            var layoutParams: ViewGroup.LayoutParams? = mPinnedHeaderView!!.layoutParams
            if (layoutParams == null) {
                layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                mPinnedHeaderView!!.layoutParams = layoutParams
            }

            var heightMode = View.MeasureSpec.getMode(layoutParams.height)
            var heightSize = View.MeasureSpec.getSize(layoutParams.height)

            if (heightMode == View.MeasureSpec.UNSPECIFIED) {
                heightMode = View.MeasureSpec.EXACTLY
            }

            val maxHeight = parent.height - parent.paddingTop - parent.paddingBottom
            if (heightSize > maxHeight) {
                heightSize = maxHeight
            }

            // measure & layout
            val ws = View.MeasureSpec.makeMeasureSpec(
                parent.width - parent.paddingLeft - parent.paddingRight,
                View.MeasureSpec.EXACTLY
            )
            val hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode)
            mPinnedHeaderView!!.measure(ws, hs)
            mPinnedHeaderView!!.layout(0, 0, mPinnedHeaderView!!.measuredWidth, mPinnedHeaderView!!.measuredHeight)
        }
    }

    private fun findPinnedHeaderPosition(fromPosition: Int): Int {
        if (fromPosition > mAdapter!!.itemCount) {
            return -1
        }

        for (position in fromPosition downTo 0) {
            val viewType = mAdapter!!.getItemViewType(position)
            if (isPinnedViewType(viewType)) {
                return position
            }
        }

        return -1
    }

    private fun isPinnedViewType(viewType: Int): Boolean {
        if (!mPinnedViewTypes.containsKey(viewType)) {
            mPinnedViewTypes[viewType] = (mAdapter as PinnedHeaderAdapter).isPinnedViewType(viewType)
        }

        return mPinnedViewTypes[viewType]!!
    }

    private fun isHeaderView(parent: RecyclerView, v: View?): Boolean {
        try {
            val position = parent.getChildAdapterPosition(v!!)
            if (position == RecyclerView.NO_POSITION) {
                return false
            }
            val viewType = mAdapter!!.getItemViewType(position)

            return isPinnedViewType(viewType)
        }catch (e : java.lang.Exception){
            Logger.debug(e)
        }
        return false
    }

    private fun checkCache(parent: RecyclerView) {
        val adapter = parent.adapter
        if (mAdapter !== adapter) {
            disableCache()
            if (adapter is PinnedHeaderAdapter) {
                @Suppress("UNCHECKED_CAST")
                mAdapter = adapter as RecyclerView.Adapter<DataBoundViewHolder<ViewDataBinding>>?
            } else {
                mAdapter = null
            }
        }
    }

    private fun disableCache() {
        mPinnedHeaderView = null
        mHeaderPosition = -1
        mPinnedViewTypes.clear()
    }

}
