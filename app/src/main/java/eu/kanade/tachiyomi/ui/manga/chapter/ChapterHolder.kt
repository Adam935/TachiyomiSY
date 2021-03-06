package eu.kanade.tachiyomi.ui.manga.chapter

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.view.isVisible
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.download.model.Download
import eu.kanade.tachiyomi.ui.base.holder.BaseFlexibleViewHolder
import exh.EH_SOURCE_ID
import exh.EXH_SOURCE_ID
import exh.metadata.MetadataUtil
import kotlinx.android.synthetic.main.chapters_item.bookmark_icon
import kotlinx.android.synthetic.main.chapters_item.chapter_description
import kotlinx.android.synthetic.main.chapters_item.chapter_title
import kotlinx.android.synthetic.main.chapters_item.download_text
import java.util.Date

class ChapterHolder(
    view: View,
    private val adapter: ChaptersAdapter
) : BaseFlexibleViewHolder(view, adapter) {

    fun bind(item: ChapterItem, manga: Manga) {
        val chapter = item.chapter

        chapter_title.text = when (manga.displayMode) {
            Manga.DISPLAY_NUMBER -> {
                val number = adapter.decimalFormat.format(chapter.chapter_number.toDouble())
                itemView.context.getString(R.string.display_mode_chapter, number)
            }
            else -> chapter.name
        }

        // Set correct text color
        val chapterTitleColor = when {
            chapter.read -> adapter.readColor
            chapter.bookmark -> adapter.bookmarkedColor
            else -> adapter.unreadColor
        }
        chapter_title.setTextColor(chapterTitleColor)

        val chapterDescriptionColor = when {
            chapter.read -> adapter.readColor
            chapter.bookmark -> adapter.bookmarkedColor
            else -> adapter.unreadColorSecondary
        }
        chapter_description.setTextColor(chapterDescriptionColor)

        bookmark_icon.isVisible = chapter.bookmark

        val descriptions = mutableListOf<CharSequence>()

        if (chapter.date_upload > 0) {
            // SY -->
            if (manga.source == EH_SOURCE_ID || manga.source == EXH_SOURCE_ID) {
                descriptions.add(MetadataUtil.EX_DATE_FORMAT.format(Date(chapter.date_upload)))
            } else /* SY <-- */ descriptions.add(adapter.dateFormat.format(Date(chapter.date_upload)))
        }
        if ((!chapter.read || (adapter.preserveReadingPosition && (manga.source == EH_SOURCE_ID || manga.source == EXH_SOURCE_ID))) && chapter.last_page_read > 0) {
            val lastPageRead = SpannableString(itemView.context.getString(R.string.chapter_progress, chapter.last_page_read + 1)).apply {
                setSpan(ForegroundColorSpan(adapter.readColor), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            descriptions.add(lastPageRead)
        }
        if (!chapter.scanlator.isNullOrBlank()) {
            descriptions.add(chapter.scanlator!!)
        }

        if (descriptions.isNotEmpty()) {
            chapter_description.text = descriptions.joinTo(SpannableStringBuilder(), " • ")
        } else {
            chapter_description.text = ""
        }

        notifyStatus(item.status)
    }

    fun notifyStatus(status: Int) = with(download_text) {
        when (status) {
            Download.QUEUE -> setText(R.string.chapter_queued)
            Download.DOWNLOADING -> setText(R.string.chapter_downloading)
            Download.DOWNLOADED -> setText(R.string.chapter_downloaded)
            Download.ERROR -> setText(R.string.chapter_error)
            else -> text = ""
        }
    }
}
