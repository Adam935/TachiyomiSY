package exh.metadata

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.util.system.dpToPx
import eu.kanade.tachiyomi.util.system.getResourceColor
import exh.util.SourceTagsUtil
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Metadata utils
 */
object MetadataUtil {
    fun humanReadableByteCount(bytes: Long, si: Boolean): String {
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return "$bytes B"
        val exp = (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
        return String.format("%.1f %sB", bytes / unit.toDouble().pow(exp.toDouble()), pre)
    }

    private const val KB_FACTOR: Long = 1000
    private const val KIB_FACTOR: Long = 1024
    private const val MB_FACTOR = 1000 * KB_FACTOR
    private const val MIB_FACTOR = 1024 * KIB_FACTOR
    private const val GB_FACTOR = 1000 * MB_FACTOR
    private const val GIB_FACTOR = 1024 * MIB_FACTOR

    fun parseHumanReadableByteCount(arg0: String): Double? {
        val spaceNdx = arg0.indexOf(" ")
        val ret = java.lang.Double.parseDouble(arg0.substring(0, spaceNdx))
        when (arg0.substring(spaceNdx + 1)) {
            "GB" -> return ret * GB_FACTOR
            "GiB" -> return ret * GIB_FACTOR
            "MB" -> return ret * MB_FACTOR
            "MiB" -> return ret * MIB_FACTOR
            "KB" -> return ret * KB_FACTOR
            "KiB" -> return ret * KIB_FACTOR
        }
        return null
    }

    val ONGOING_SUFFIX = arrayOf(
        "[ongoing]",
        "(ongoing)",
        "{ongoing}",
        "<ongoing>",
        "ongoing",
        "[incomplete]",
        "(incomplete)",
        "{incomplete}",
        "<incomplete>",
        "incomplete",
        "[wip]",
        "(wip)",
        "{wip}",
        "<wip>",
        "wip"
    )

    val EX_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun getRatingString(context: Context, @FloatRange(from = 0.0, to = 10.0) rating: Float?) = when ((rating ?: 100F).roundToInt()) {
        0 -> R.string.rating0
        1 -> R.string.rating1
        2 -> R.string.rating2
        3 -> R.string.rating3
        4 -> R.string.rating4
        5 -> R.string.rating5
        6 -> R.string.rating6
        7 -> R.string.rating7
        8 -> R.string.rating8
        9 -> R.string.rating9
        10 -> R.string.rating10
        else -> R.string.no_rating
    }.let { context.getString(it) }

    fun getGenreAndColour(context: Context, genre: String) = when (genre) {
        "doujinshi", "Doujinshi" -> SourceTagsUtil.DOUJINSHI_COLOR to R.string.doujinshi
        "manga", "Japanese Manga", "Manga" -> SourceTagsUtil.MANGA_COLOR to R.string.manga
        "artistcg", "artist CG", "artist-cg", "Artist CG" -> SourceTagsUtil.ARTIST_CG_COLOR to R.string.artist_cg
        "gamecg", "game CG", "game-cg", "Game CG" -> SourceTagsUtil.GAME_CG_COLOR to R.string.game_cg
        "western" -> SourceTagsUtil.WESTERN_COLOR to R.string.western
        "non-h", "non-H" -> SourceTagsUtil.NON_H_COLOR to R.string.non_h
        "imageset", "image Set" -> SourceTagsUtil.IMAGE_SET_COLOR to R.string.image_set
        "cosplay" -> SourceTagsUtil.COSPLAY_COLOR to R.string.cosplay
        "asianporn", "asian Porn" -> SourceTagsUtil.ASIAN_PORN_COLOR to R.string.asian_porn
        "misc" -> SourceTagsUtil.MISC_COLOR to R.string.misc
        "Korean Manhwa" -> SourceTagsUtil.ARTIST_CG_COLOR to R.string.manhwa
        "Chinese Manhua" -> SourceTagsUtil.GAME_CG_COLOR to R.string.manhua
        "Comic" -> SourceTagsUtil.WESTERN_COLOR to R.string.comic
        "artbook" -> SourceTagsUtil.IMAGE_SET_COLOR to R.string.artbook
        "webtoon" -> SourceTagsUtil.NON_H_COLOR to R.string.webtoon
        "Video" -> SourceTagsUtil.WESTERN_COLOR to R.string.video
        else -> "" to 0
    }.let { if (it.second == 0) null else Color.parseColor(it.first) to context.getString(it.second) }
}

fun <K, V> Set<Map.Entry<K, V>>.forEach(action: (K, V) -> Unit) {
    forEach { action(it.key, it.value) }
}

fun TextView.bindDrawable(context: Context, @DrawableRes drawable: Int) {
    ContextCompat.getDrawable(context, drawable)?.apply {
        setTint(context.getResourceColor(R.attr.colorAccent))
        setBounds(0, 0, 20.dpToPx, 20.dpToPx)
        setCompoundDrawables(this, null, null, null)
    }
}
