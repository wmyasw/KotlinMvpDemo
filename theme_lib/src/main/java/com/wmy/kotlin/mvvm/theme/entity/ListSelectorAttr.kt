import android.view.View
import android.widget.AbsListView
import com.wmy.kotlin.mvvm.theme.SkinManager
import com.wmy.kotlin.mvvm.theme.entity.SkinItem

/**
 *
 */
class ListSelectorAttr : SkinItem() {

    override fun apply(view: View?) {
        if (view is AbsListView) {
            val tv = view
            if (SkinItem.RES_TYPE_NAME_COLOR == typeName) {
                tv.setSelector(SkinManager.instance.getColor(resId))
            } else if (SkinItem.RES_TYPE_NAME_DRAWABLE == typeName) {
                tv.setSelector(SkinManager.instance.getDrawable(resId))
            }
        }
    }
}