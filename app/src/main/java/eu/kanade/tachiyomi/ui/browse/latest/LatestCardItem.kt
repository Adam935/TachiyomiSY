package eu.kanade.tachiyomi.ui.browse.latest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Manga
import uy.kohesive.injekt.api.get

class LatestCardItem(val manga: Manga) : AbstractFlexibleItem<LatestCardHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.global_search_controller_card_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): LatestCardHolder {
        return LatestCardHolder(view, adapter as LatestCardAdapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: LatestCardHolder,
        position: Int,
        payloads: List<Any?>?
    ) {
        holder.bind(manga)
    }

    override fun equals(other: Any?): Boolean {
        if (other is LatestCardItem) {
            return manga.id == other.manga.id
        }
        return false
    }

    override fun hashCode(): Int {
        return manga.id?.toInt() ?: 0
    }
}
