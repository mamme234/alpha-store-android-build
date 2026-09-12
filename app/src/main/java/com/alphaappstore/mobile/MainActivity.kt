package com.alphaappstore.mobile

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

// ============================================================
// Localization (en / am / om / ar). Persisted, RTL-aware.
// ============================================================

object L10n {
    val LANGS = listOf("en", "am", "om", "ar")
    val LANG_LABELS = mapOf(
        "en" to "English",
        "am" to "አማርኛ",
        "om" to "Afaan Oromo",
        "ar" to "العربية",
    )

    private val EN = mapOf(
        "home" to "Home",
        "apps" to "Apps",
        "games" to "Games",
        "updates" to "Updates",
        "library" to "Library",
        "search" to "Search apps and games",
        "featured" to "Featured",
        "new_releases" to "New releases",
        "trending" to "Trending now",
        "editors" to "Editor's picks",
        "recommended" to "Recommended for you",
        "categories" to "Categories",
        "install" to "Install",
        "update" to "Update",
        "open" to "Open",
        "installing" to "Downloading",
        "retry" to "Retry",
        "cancel" to "Close",
        "up_to_date" to "All apps are up to date",
        "no_results" to "No results found",
        "network_error" to "Could not reach Alpha App Store. Check your connection.",
        "about" to "About",
        "language" to "Language",
        "version" to "Version",
        "whats_new" to "What's new",
        "details" to "Details",
        "description" to "Description",
        "not_available" to "Download unavailable yet",
        "unknown" to "Unknown",
        "all_apps" to "All apps",
        "all_games" to "All games",
        "updated" to "Updated",
        "size" to "Size",
        "developer" to "Developer",
        "back" to "Back",
        "release_page" to "Get the latest APK on GitHub",
        "install_perm" to "Allow installs from Alpha App Store in Settings, then tap Install again.",
    )

    private val AM = mapOf(
        "home" to "መነጃ",
        "apps" to "ከተግበራያዎች",
        "games" to "ከሳታዎች",
        "updates" to "ዛማሜዎች",
        "library" to "ቤየተ",
        "search" to "ከተግበራያ ና ከሳታ ፈለግ",
        "featured" to "ከጭ የተመረጡ",
        "new_releases" to "አዲስ የወጡ",
        "trending" to "ታዋራዋሽ",
        "editors" to "የአርታኢ ምርጫ",
        "recommended" to "የሚመሰረቡ",
        "categories" to "ምድቦች",
        "install" to "ጫን",
        "update" to "ዛምን",
        "open" to "ከፈት",
        "installing" to "በሚጫን ላይ",
        "retry" to "እንደገና ሞክር",
        "cancel" to "ዝጋ",
        "up_to_date" to "ሁሉም ዛማሜ አለው",
        "no_results" to "ውጤት አልተገኘም",
        "network_error" to "ከAlpha App Store ጋር መገናበት አልተቻለም። ኢንተርኔትዎን ያረጋግጡ።",
        "about" to "ስወ ከተግበራያው",
        "language" to "ቋንቋ",
        "version" to "እትም",
        "whats_new" to "ምን አዲስ",
        "details" to "ዝርዝር",
        "description" to "መግለጫ",
        "not_available" to "ማውረጃ አሑን አይገኝም",
        "unknown" to "የማይታወቅ",
        "all_apps" to "ሁሉም ከተግበራያዎች",
        "all_games" to "ሁሉም ከዠታዎች",
        "updated" to "የተዘመነው",
        "size" to "መጠን",
        "developer" to "ገንቢ",
        "back" to "ተመለስ",
        "release_page" to "አዲስ APK ከGitHub ያግኙ",
        "install_perm" to "በቅንብሮች ከAlpha App Store መጫን ይፍቀዱ፣ ከዚካ እንደገና ይጫኑ።",
    )

    private val OM = mapOf(
        "home" to "Mana",
        "apps" to "Aappii",
        "games" to "Taphii",
        "updates" to "Haaromsi",
        "library" to "Kuusaa koo",
        "search" to "Aappii fi taphii barbaadi",
        "featured" to "Filatamaa",
        "new_releases" to "Haaraa",
        "trending" to "Jaalatamaa",
        "editors" to "Filannoo gulaalaa",
        "recommended" to "Gorfame",
        "categories" to "Ramaddii",
        "install" to "Olbisi",
        "update" to "Haaromsi",
        "open" to "Bani",
        "installing" to "Buufamaa",
        "retry" to "Irra deebi'i",
        "cancel" to "Cufi",
        "up_to_date" to "Hundi haaromsiin jira",
        "no_results" to "Bu'aa hin argamne",
        "network_error" to "Alpha App Store hin quqaamne. Frichi kee mirkaneessi.",
        "about" to "Waa'ee",
        "language" to "Afaan",
        "version" to "Version",
        "whats_new" to "Waan haaraa",
        "details" to "Ibsa",
        "description" to "Ibsa",
        "not_available" to "Buufamni amma hin jiru",
        "unknown" to "Hin beekamu",
        "all_apps" to "Aappii hunda",
        "all_games" to "Taphii hunda",
        "updated" to "Haaromfame",
        "size" to "Guddina",
        "developer" to "Hojjetaa",
        "back" to "Galdi",
        "release_page" to "APK haaraa GitHub irra",
        "install_perm" to "Sajoo irratti olbisa hayyami, ergasii irra deebi' olbisi.",
    )

    private val AR = mapOf(
        "home" to "الرئيسية",
        "apps" to "التطبيقات",
        "games" to "الألعاب",
        "updates" to "التحديثات",
        "library" to "مكتبتي",
        "search" to "ابحث عن تطبيقات وألعاب",
        "featured" to "مميز",
        "new_releases" to "أحدث الإصدارات",
        "trending" to "الأكثر رواجًا",
        "editors" to "اختيارات المحررين",
        "recommended" to "موصى به",
        "categories" to "التصنيفات",
        "install" to "تثبيت",
        "update" to "تحديث",
        "open" to "فتح",
        "installing" to "جارٍ التنزيل",
        "retry" to "إعادة المحاولة",
        "cancel" to "إغلاق",
        "up_to_date" to "جميع التطبيقات محدثة",
        "no_results" to "لا توجد نتائج",
        "network_error" to "تعذّر الوصول إلى Alpha App Store. تحقق من اتصالك.",
        "about" to "حول",
        "language" to "اللغة",
        "version" to "الإصدار",
        "whats_new" to "ما الجديد",
        "details" to "التفاصيل",
        "description" to "الوصف",
        "not_available" to "التنزيل غير متوفر بعد",
        "unknown" to "غير معروف",
        "all_apps" to "كل التطبيقات",
        "all_games" to "كل الألعاب",
        "updated" to "آخر تحديث",
        "size" to "الحجم",
        "developer" to "المطور",
        "back" to "رجوع",
        "release_page" to "الحصول على أحدث إصدار من GitHub",
        "install_perm" to "اسمح بالتثبيت من Alpha App Store في الإعدادات، ثم اضغط تثبيت مجددًا.",
    )

    private val TABLE = mapOf("en" to EN, "am" to AM, "om" to OM, "ar" to AR)

    fun load(ctx: Context): String {
        val saved = ctx.getSharedPreferences("alpha_prefs", Context.MODE_PRIVATE)
            .getString("lang", null)
        if (saved != null && LANGS.contains(saved)) return saved
        val sys = Locale.getDefault().language
        return if (LANGS.contains(sys)) sys else "en"
    }

    fun save(ctx: Context, lang: String) {
        ctx.getSharedPreferences("alpha_prefs", Context.MODE_PRIVATE)
            .edit().putString("lang", lang).apply()
    }

    fun t(lang: String, key: String): String {
        val table = TABLE[lang] ?: EN
        return table[key] ?: EN[key] ?: key
    }
}

// ============================================================
// Models
// ============================================================

data class StoreApp(
    val id: String,
    val name: String,
    val packageId: String,
    val shortDescription: String,
    val category: String,
    val kind: String,
    val developerName: String?,
    val version: String?,
    val versionCode: Long?,
    val sizeBytes: Long?,
    val downloadCount: Long,
    val ratingAvg: Double?,
    val ratingCount: Long,
    val featured: Boolean,
    val editorsPick: Boolean,
    val updatedAt: Long,
)

data class AppVersion(
    val versionName: String,
    val versionCode: Long,
    val releaseNotes: String?,
    val sizeBytes: Long?,
    val apkUrl: String?,
)

data class AppDetail(
    val app: StoreApp,
    val description: String,
    val versions: List<AppVersion>,
)

data class HomeFeed(
    val featured: List<StoreApp>,
    val editorsPicks: List<StoreApp>,
    val newReleases: List<StoreApp>,
    val recentlyUpdated: List<StoreApp>,
    val popularGames: List<StoreApp>,
    val recommended: List<StoreApp>,
)

data class UpdateRow(
    val packageId: String,
    val app: StoreApp,
    val installedCode: Long,
    val newVersionName: String,
    val newVersionCode: Long,
    val apkUrl: String?,
)

sealed interface DownloadState {
    data object Idle : DownloadState
    data class Downloading(val pct: Int) : DownloadState
    data class Ready(val uri: Uri) : DownloadState
    data object Failed : DownloadState
}

data class CategoryRow(val slug: String, val name: String)
// ============================================================
// Alpha API — real HTTP against the live store endpoints.
// ============================================================

object AlphaApi {
    const val BASE = "https://flippant-tiger-230.convex.site"
    const val RELEASE_PAGE =
        "https://github.com/mamme234/alpha-store-android-build/releases"

    private fun optStr(o: JSONObject, k: String): String? =
        if (o.isNull(k)) null else o.optString(k)

    private fun optLong(o: JSONObject, k: String): Long? =
        if (o.isNull(k) || !o.has(k)) null else try {
            o.getLong(k)
        } catch (e: Exception) {
            null
        }

    private fun parseApp(o: JSONObject): StoreApp = StoreApp(
        id = o.optString("id"),
        name = o.optString("name"),
        packageId = o.optString("packageId"),
        shortDescription = o.optString("shortDescription"),
        category = o.optString("category"),
        kind = o.optString("kind", "apps"),
        developerName = optStr(o, "developerName"),
        version = optStr(o, "version"),
        versionCode = optLong(o, "versionCode"),
        sizeBytes = optLong(o, "sizeBytes"),
        downloadCount = optLong(o, "downloadCount") ?: 0L,
        ratingAvg = if (o.isNull("ratingAvg")) null else o.optDouble("ratingAvg"),
        ratingCount = optLong(o, "ratingCount") ?: 0L,
        featured = o.optBoolean("featured", false),
        editorsPick = o.optBoolean("editorsPick", false),
        updatedAt = optLong(o, "updatedAt") ?: 0L,
    )

    private fun apps(arr: JSONArray): List<StoreApp> {
        val out = ArrayList<StoreApp>()
        for (i in 0 until arr.length()) out.add(parseApp(arr.getJSONObject(i)))
        return out
    }

    private fun request(path: String, body: String?): String? {
        return try {
            val conn = URL(BASE + path).openConnection() as HttpURLConnection
            conn.connectTimeout = 12000
            conn.readTimeout = 20000
            if (body != null) {
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.outputStream.use {
                    it.write(body.toByteArray(Charsets.UTF_8))
                }
            } else {
                conn.requestMethod = "GET"
            }
            val code = conn.responseCode
            if (code in 200..299) {
                conn.inputStream.bufferedReader(Charsets.UTF_8).use {
                    it.readText()
                }
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun home(): HomeFeed? {
        val text = request("/api/catalog/home", null) ?: return null
        return try {
            val o = JSONObject(text)
            HomeFeed(
                featured = apps(o.getJSONArray("featured")),
                editorsPicks = apps(o.getJSONArray("editorsPicks")),
                newReleases = apps(o.getJSONArray("newReleases")),
                recentlyUpdated = apps(o.getJSONArray("recentlyUpdated")),
                popularGames = apps(o.getJSONArray("popularGames")),
                recommended = apps(o.getJSONArray("recommended")),
            )
        } catch (e: Exception) {
            null
        }
    }

    fun list(kind: String, category: String?): List<StoreApp> {
        var path = "/api/apps?kind=" + URLEncoder.encode(kind, "UTF-8") + "&limit=60"
        if (category != null) path += "&category=" + URLEncoder.encode(category, "UTF-8")
        val text = request(path, null) ?: return emptyList()
        return try {
            apps(JSONArray(text))
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun search(q: String): List<StoreApp> {
        val text = request("/api/search?q=" + URLEncoder.encode(q, "UTF-8"), null)
            ?: return emptyList()
        return try {
            apps(JSONArray(text))
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun detail(id: String): AppDetail? {
        val text = request("/api/app?id=" + URLEncoder.encode(id, "UTF-8"), null) ?: return null
        return try {
            val o = JSONObject(text)
            val versions = ArrayList<AppVersion>()
            val vArr = o.getJSONArray("versions")
            for (i in 0 until vArr.length()) {
                val v = vArr.getJSONObject(i)
                versions.add(
                    AppVersion(
                        versionName = v.optString("versionName"),
                        versionCode = v.optLong("versionCode"),
                        releaseNotes = optStr(v, "releaseNotes"),
                        sizeBytes = optLong(v, "sizeBytes"),
                        apkUrl = optStr(v, "apkUrl"),
                    ),
                )
            }
            AppDetail(
                app = parseApp(o),
                description = o.optString("description"),
                versions = versions,
            )
        } catch (e: Exception) {
            null
        }
    }

    fun categories(kind: String): List<CategoryRow> {
        val text = request("/api/categories?kind=" + URLEncoder.encode(kind, "UTF-8"), null)
            ?: return emptyList()
        return try {
            val arr = JSONArray(text)
            val out = ArrayList<CategoryRow>()
            for (i in 0 until arr.length()) {
                val c = arr.getJSONObject(i)
                out.add(CategoryRow(slug = c.optString("slug"), name = c.optString("name")))
            }
            out
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Real update check: sends installed versionCodes, returns only packages
     * whose published versionCode is strictly greater. Never invents updates.
     */
    fun updates(installed: Map<String, Pair<String, Long>>): List<UpdateRow> {
        if (installed.isEmpty()) return emptyList()
        val known = ArrayList<StoreApp>()
        known.addAll(list("apps", null))
        known.addAll(list("games", null))
        val packages = JSONArray()
        for (app in known) {
            val inst = installed[app.packageId] ?: continue
            packages.put(
                JSONObject()
                    .put("packageId", app.packageId)
                    .put("versionCode", inst.second),
            )
        }
        if (packages.length() == 0) return emptyList()
        val text = request(
            "/api/updates",
            JSONObject().put("packages", packages).toString(),
        ) ?: return emptyList()
        val out = ArrayList<UpdateRow>()
        return try {
            val arr = JSONObject(text).getJSONArray("updates")
            val byPackage = HashMap<String, StoreApp>()
            for (app in known) byPackage[app.packageId] = app
            for (i in 0 until arr.length()) {
                val u = arr.getJSONObject(i)
                val pkg = u.optString("packageId")
                val app = byPackage[pkg] ?: continue
                out.add(
                    UpdateRow(
                        packageId = pkg,
                        app = app,
                        installedCode = u.optLong("installedVersionCode"),
                        newVersionName = u.optString("newVersionName"),
                        newVersionCode = u.optLong("newVersionCode"),
                        apkUrl = optStr(u, "apkUrl"),
                    ),
                )
            }
            out
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Records a real download event. Returns the real APK URL to fetch. */
    fun startDownload(packageId: String, deviceId: String): String? {
        val body = JSONObject()
            .put("packageId", packageId)
            .put("deviceId", deviceId)
            .toString()
        val text = request("/api/downloads", body) ?: return null
        return try {
            optStr(JSONObject(text), "apkUrl")
        } catch (e: Exception) {
            null
        }
    }

    fun deviceId(ctx: Context): String {
        val prefs = ctx.getSharedPreferences("alpha_prefs", Context.MODE_PRIVATE)
        val saved = prefs.getString("device_id", null)
        if (saved != null) return saved
        val fresh = try {
            Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
                ?: java.util.UUID.randomUUID().toString()
        } catch (e: Exception) {
            java.util.UUID.randomUUID().toString()
        }
        prefs.edit().putString("device_id", fresh).apply()
        return fresh
    }
}

// ============================================================
// Real install pipeline: DownloadManager + package installer.
// ============================================================

class InstallManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("alpha_downloads", Context.MODE_PRIVATE)
    private val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private val autoInstallTried = HashSet<String>()

    fun enqueue(packageId: String, url: String, title: String): Boolean {
        return try {
            cancel(packageId)
            val req = DownloadManager.Request(Uri.parse(url))
                .setTitle(title)
                .setDescription("Alpha App Store")
                .setMimeType("application/vnd.android.package-archive")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    packageId + ".apk",
                )
            val id = dm.enqueue(req)
            prefs.edit().putLong(packageId, id).apply()
            autoInstallTried.remove(packageId)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun stateOf(packageId: String): DownloadState {
        val id = prefs.getLong(packageId, -1L)
        if (id == -1L) return DownloadState.Idle
        return try {
            val q = dm.query(DownloadManager.Query().setFilterById(id))
            if (q == null || !q.moveToFirst()) return DownloadState.Idle
            when (q.getInt(q.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    val uri = dm.getUriForDownloadedFile(id)
                    if (uri != null) DownloadState.Ready(uri) else DownloadState.Idle
                }
                DownloadManager.STATUS_FAILED -> DownloadState.Failed
                DownloadManager.STATUS_RUNNING -> {
                    val total = q.getLong(
                        q.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES),
                    )
                    val sofar = q.getLong(
                        q.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR),
                    )
                    DownloadState.Downloading(
                        if (total > 0) ((sofar * 100) / total).toInt() else 0,
                    )
                }
                else -> DownloadState.Downloading(0)
            }
        } catch (e: Exception) {
            DownloadState.Idle
        }
    }

    /** Launches the real package installer once the APK is on disk. */
    fun maybeAutoInstall(packageId: String, state: DownloadState) {
        if (state is DownloadState.Ready && !autoInstallTried.contains(packageId)) {
            autoInstallTried.add(packageId)
            install(packageId, state.uri)
        }
    }

    fun install(packageId: String, uri: Uri) {
        try {
            val i = Intent(Intent.ACTION_INSTALL_PACKAGE)
            i.setData(uri)
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(context, e.message ?: "Install failed", Toast.LENGTH_LONG).show()
        }
    }

    fun cancel(packageId: String) {
        val id = prefs.getLong(packageId, -1L)
        if (id != -1L) {
            try {
                dm.remove(id)
            } catch (e: Exception) {
            }
        }
        prefs.edit().remove(packageId).apply()
    }
}

fun canInstallPackages(ctx: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ctx.packageManager.canRequestPackageInstalls()
    } else true
}

fun requestInstallPermission(ctx: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            val i = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:" + ctx.packageName),
            )
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(
                ctx,
                "Settings > Apps > Special access > Install unknown apps",
                Toast.LENGTH_LONG,
            ).show()
        }
    }
}

fun installedPackages(ctx: Context): Map<String, Pair<String, Long>> {
    val out = HashMap<String, Pair<String, Long>>()
    return try {
        val pm = ctx.packageManager
        for (info in pm.getInstalledPackages(0)) {
            val code =
                if (Build.VERSION.SDK_INT >= 28) info.longVersionCode else info.versionCode.toLong()
            out[info.packageName] = Pair(info.versionName ?: "?", code)
        }
        out
    } catch (e: Exception) {
        out
    }
}

fun ownVersion(ctx: Context): Pair<String, Long> {
    return try {
        val info = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        val code =
            if (Build.VERSION.SDK_INT >= 28) info.longVersionCode else info.versionCode.toLong()
        Pair(info.versionName ?: "?", code)
    } catch (e: Exception) {
        Pair("?", 0L)
    }
}

fun openStoreApp(ctx: Context, packageId: String) {
    val intent = ctx.packageManager.getLaunchIntentForPackage(packageId)
    if (intent != null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(intent)
    } else {
        Toast.makeText(ctx, "App not installed", Toast.LENGTH_SHORT).show()
    }
}

fun formatBytes(b: Long): String = when {
    b >= 1_000_000_000L -> String.format(Locale.US, "%.1f GB", b / 1_000_000_000.0)
    b >= 1_000_000L -> String.format(Locale.US, "%.1f MB", b / 1_000_000.0)
    b >= 1_000L -> String.format(Locale.US, "%.0f KB", b / 1_000.0)
    else -> b.toString() + " B"
}

// ============================================================
// Activity, theme and app shell
// ============================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlphaStoreApp()
        }
    }
}

private val AlphaBackground = Color(0xFF060B18)
private val AlphaSurfaceColor = Color(0xFF0D1526)
private val AlphaSurfaceHigh = Color(0xFF16203A)
private val AlphaAccent = Color(0xFF22D3EE)
private val AlphaAccentDim = Color(0xFF0E7490)
private val AlphaText = Color(0xFFE2E8F0)
private val AlphaTextDim = Color(0xFF94A3B8)
private val AlphaViolet = Color(0xFFA78BFA)

@Composable
fun AlphaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = AlphaAccent,
            onPrimary = Color(0xFF04121A),
            secondary = AlphaViolet,
            background = AlphaBackground,
            onBackground = AlphaText,
            surface = AlphaSurfaceColor,
            onSurface = AlphaText,
            surfaceVariant = AlphaSurfaceHigh,
            onSurfaceVariant = AlphaTextDim,
            error = Color(0xFFF87171),
        ),
        content = content,
    )
}

enum class Tab { HOME, APPS, GAMES, UPDATES, LIBRARY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphaStoreApp() {
    val ctx = LocalContext.current
    var lang by remember { mutableStateOf(L10n.load(ctx)) }
    var tab by remember { mutableStateOf(Tab.HOME) }
    var searchOpen by remember { mutableStateOf(false) }
    var aboutOpen by remember { mutableStateOf(false) }
    var langOpen by remember { mutableStateOf(false) }
    var selectedAppId by remember { mutableStateOf<String?>(null) }

    val layoutDirection = if (lang == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        AlphaTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = AlphaBackground) {
                val installs = remember {
                    mutableStateMapOf<String, DownloadState>()
                }
                val installManager = remember { InstallManager(ctx) }
                val scope = rememberCoroutineScope()

                // Poll real DownloadManager progress for active downloads.
                LaunchedEffect(Unit) {
                    while (true) {
                        for ((pkg, _) in installs.toList()) {
                            val next = installManager.stateOf(pkg)
                            installs[pkg] = next
                            installManager.maybeAutoInstall(pkg, next)
                        }
                        delay(900)
                    }
                }

                val openDetails: (String) -> Unit = { selectedAppId = it }
                val download: (String, String, String) -> Unit = { pkg, url, title ->
                    scope.launch {
                        val realUrl = withContext(Dispatchers.IO) {
                            AlphaApi.startDownload(pkg, AlphaApi.deviceId(ctx))
                        } ?: url
                        if (installManager.enqueue(pkg, realUrl, title)) {
                            installs[pkg] = DownloadState.Downloading(0)
                        }
                    }
                }

                if (selectedAppId != null) {
                    AppDetailScreen(
                        appId = selectedAppId!!,
                        lang = lang,
                        installs = installs,
                        onBack = { selectedAppId = null },
                        onDownload = download,
                    )
                } else {
                    Scaffold(
                        containerColor = AlphaBackground,
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        "Alpha App Store",
                                        fontWeight = FontWeight.Bold,
                                        color = AlphaAccent,
                                    )
                                },
                                actions = {
                                    IconButton(onClick = { searchOpen = true }) {
                                        Icon(
                                            Icons.Filled.Search,
                                            contentDescription = L10n.t(lang, "search"),
                                            tint = AlphaText,
                                        )
                                    }
                                    IconButton(onClick = { langOpen = true }) {
                                        Icon(
                                            Icons.Filled.Language,
                                            contentDescription = L10n.t(lang, "language"),
                                            tint = AlphaText,
                                        )
                                    }
                                    IconButton(onClick = { aboutOpen = true }) {
                                        Icon(
                                            Icons.Filled.Info,
                                            contentDescription = L10n.t(lang, "about"),
                                            tint = AlphaText,
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = AlphaBackground,
                                ),
                            )
                        },
                        bottomBar = {
                            NavigationBar(containerColor = AlphaSurfaceColor) {
                                val tabs = listOf(
                                    Triple(Tab.HOME, Icons.Filled.Home, "home"),
                                    Triple(Tab.APPS, Icons.Filled.Apps, "apps"),
                                    Triple(Tab.GAMES, Icons.Filled.SportsEsports, "games"),
                                    Triple(Tab.UPDATES, Icons.Filled.SystemUpdate, "updates"),
                                    Triple(Tab.LIBRARY, Icons.Filled.Person, "library"),
                                )
                                for ((t, icon, key) in tabs) {
                                    NavigationBarItem(
                                        selected = tab == t,
                                        onClick = {
                                            tab = t
                                            searchOpen = false
                                        },
                                        icon = {
                                            Icon(icon, contentDescription = L10n.t(lang, key))
                                        },
                                        label = {
                                            Text(
                                                L10n.t(lang, key),
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = AlphaAccent,
                                            selectedTextColor = AlphaAccent,
                                            indicatorColor = AlphaAccentDim.copy(alpha = 0.35f),
                                            unselectedIconColor = AlphaTextDim,
                                            unselectedTextColor = AlphaTextDim,
                                        ),
                                    )
                                }
                            }
                        },
                    ) { pad ->
                        Box(Modifier.padding(pad).fillMaxSize()) {
                            when (tab) {
                                Tab.HOME -> HomeScreen(lang, openDetails)
                                Tab.APPS -> BrowseScreen(lang, "apps", openDetails)
                                Tab.GAMES -> BrowseScreen(lang, "games", openDetails)
                                Tab.UPDATES -> UpdatesScreen(lang, installManager, installs)
                                Tab.LIBRARY -> LibraryScreen(lang) { pkg ->
                                    openStoreApp(ctx, pkg)
                                }
                            }
                        }
                    }
                }

                if (searchOpen) {
                    SearchOverlay(
                        lang = lang,
                        onClose = { searchOpen = false },
                        onOpenApp = {
                            searchOpen = false
                            selectedAppId = it
                        },
                    )
                }
                if (aboutOpen) {
                    AboutDialog(ctx, lang) { aboutOpen = false }
                }
                if (langOpen) {
                    LangDialog(lang) { picked ->
                        L10n.save(ctx, picked)
                        lang = picked
                        langOpen = false
                    }
                }
            }
        }
    }
}

// ============================================================
// Dialogs
// ============================================================

@Composable
fun LangDialog(current: String, onPick: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = { onPick(current) },
        confirmButton = {},
        title = { Text(L10n.t(current, "language"), color = AlphaText) },
        text = {
            Column {
                for (l in L10n.LANGS) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onPick(l) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            L10n.LANG_LABELS[l] ?: l,
                            color = if (l == current) AlphaAccent else AlphaText,
                            fontWeight = if (l == current) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        },
        containerColor = AlphaSurfaceHigh,
    )
}

@Composable
fun AboutDialog(ctx: Context, lang: String, onClose: () -> Unit) {
    val (vName, vCode) = remember { ownVersion(ctx) }
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(onClick = onClose) {
                Text(L10n.t(lang, "cancel"), color = AlphaAccent)
            }
        },
        title = { Text(L10n.t(lang, "about"), color = AlphaText) },
        text = {
            Column {
                Text(
                    "Alpha App Store",
                    fontWeight = FontWeight.Bold,
                    color = AlphaAccent,
                    fontSize = 18.sp,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    L10n.t(lang, "version") + ": " + vName + " (" + vCode + ")",
                    color = AlphaText,
                    fontSize = 14.sp,
                )
                Spacer(Modifier.height(4.dp))
                Text("© 2026 Alpha App Store", color = AlphaTextDim, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
                Text(
                    L10n.t(lang, "release_page"),
                    color = AlphaAccent,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable {
                        try {
                            val i = Intent(Intent.ACTION_VIEW, Uri.parse(AlphaApi.RELEASE_PAGE))
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ctx.startActivity(i)
                        } catch (e: Exception) {
                        }
                    },
                )
            }
        },
        containerColor = AlphaSurfaceHigh,
    )
}

// ============================================================
// Shared UI pieces
// ============================================================

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        color = AlphaText,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
fun AppIconBox(name: String, sizeDp: Int, fontSize: Int) {
    Box(
        Modifier
            .size(sizeDp.dp)
            .background(AlphaSurfaceHigh, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            name.take(1).uppercase(),
            color = AlphaAccent,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCard(app: StoreApp, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = AlphaSurfaceColor),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.width(150.dp),
    ) {
        Column(Modifier.padding(12.dp)) {
            AppIconBox(app.name, 52, 22)
            Spacer(Modifier.height(8.dp))
            Text(
                app.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = AlphaText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                app.shortDescription,
                fontSize = 11.sp,
                color = AlphaTextDim,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 2.dp),
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (app.ratingAvg != null) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(13.dp),
                    )
                    Text(
                        " " + app.ratingAvg,
                        color = AlphaTextDim,
                        fontSize = 11.sp,
                    )
                } else {
                    Text(
                        app.category.replaceFirstChar { it.uppercase() },
                        color = AlphaTextDim,
                        fontSize = 11.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun AppRow(
    app: StoreApp,
    trailing: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIconBox(app.name, 46, 18)
        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(
                app.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = AlphaText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                app.shortDescription,
                fontSize = 12.sp,
                color = AlphaTextDim,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (app.ratingAvg != null) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(12.dp),
                    )
                    Text(
                        " " + app.ratingAvg + "  ·  " + (app.developerName ?: "Unknown"),
                        color = AlphaTextDim,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        app.developerName ?: "Unknown",
                        color = AlphaTextDim,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        trailing()
    }
}

@Composable
fun LoadingList() {
    Column(
        Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = AlphaAccent)
    }
}

@Composable
fun EmptyBox(
    lang: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    message: String,
) {
    Column(
        Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(icon, contentDescription = null, tint = AlphaTextDim, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(10.dp))
        Text(message, color = AlphaTextDim, fontSize = 14.sp)
    }
}

@Composable
fun ErrorBox(lang: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            Icons.Filled.ErrorOutline,
            contentDescription = null,
            tint = Color(0xFFF87171),
            modifier = Modifier.size(36.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            L10n.t(lang, "network_error"),
            color = AlphaTextDim,
            fontSize = 13.sp,
        )
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onRetry) {
            Icon(Icons.Filled.Refresh, contentDescription = null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(L10n.t(lang, "retry"), color = AlphaText)
        }
    }
}
// ============================================================
// Screens
// ============================================================

@Composable
fun HomeScreen(lang: String, onOpen: (String) -> Unit) {
    var feed by remember { mutableStateOf<HomeFeed?>(null) }
    var error by remember { mutableStateOf(false) }
    var reload by remember { mutableStateOf(0) }

    LaunchedEffect(reload) {
        error = false
        val f = withContext(Dispatchers.IO) { AlphaApi.home() }
        if (f == null) error = true else feed = f
    }

    val f = feed
    if (f == null) {
        Column(Modifier.fillMaxSize()) {
            if (error) ErrorBox(lang) { reload++ } else LoadingList()
        }
        return
    }

    LazyColumn(Modifier.fillMaxSize()) {
        item { SectionTitle(L10n.t(lang, "featured")) }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(f.featured) { a -> AppCard(a) { onOpen(a.id) } }
            }
        }
        item { SectionTitle(L10n.t(lang, "editors")) }
        items(f.editorsPicks) { a ->
            AppRow(a, trailing = {}, onClick = { onOpen(a.id) })
        }
        item { SectionTitle(L10n.t(lang, "new_releases")) }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(f.newReleases) { a -> AppCard(a) { onOpen(a.id) } }
            }
        }
        item { SectionTitle(L10n.t(lang, "trending")) }
        items(f.recentlyUpdated) { a ->
            AppRow(a, trailing = {}, onClick = { onOpen(a.id) })
        }
        item { SectionTitle(L10n.t(lang, "games")) }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(f.popularGames) { a -> AppCard(a) { onOpen(a.id) } }
            }
        }
        item { SectionTitle(L10n.t(lang, "recommended")) }
        items(f.recommended) { a ->
            AppRow(a, trailing = {}, onClick = { onOpen(a.id) })
        }
        item { Spacer(Modifier.height(20.dp)) }
    }
}

@Composable
fun BrowseScreen(lang: String, kind: String, onOpen: (String) -> Unit) {
    var apps by remember { mutableStateOf<List<StoreApp>?>(null) }
    var cats by remember { mutableStateOf<List<CategoryRow>>(emptyList()) }
    var category by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf(false) }
    var reload by remember { mutableStateOf(0) }

    LaunchedEffect(kind, category, reload) {
        error = false
        val list = withContext(Dispatchers.IO) { AlphaApi.list(kind, category) }
        if (category == null) {
            cats = withContext(Dispatchers.IO) { AlphaApi.categories(kind) }
        }
        apps = list
        if (list.isEmpty() && category == null) error = true
    }

    Column(Modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FilterChip(
                    selected = category == null,
                    onClick = { category = null },
                    label = {
                        Text(
                            if (kind == "games") L10n.t(lang, "all_games")
                            else L10n.t(lang, "all_apps"),
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = AlphaSurfaceColor,
                        selectedContainerColor = AlphaAccentDim.copy(alpha = 0.4f),
                        labelColor = AlphaTextDim,
                        selectedLabelColor = AlphaAccent,
                    ),
                )
            }
            items(cats) { c ->
                FilterChip(
                    selected = category == c.slug,
                    onClick = { category = c.slug },
                    label = { Text(c.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = AlphaSurfaceColor,
                        selectedContainerColor = AlphaAccentDim.copy(alpha = 0.4f),
                        labelColor = AlphaTextDim,
                        selectedLabelColor = AlphaAccent,
                    ),
                )
            }
        }
        val list = apps
        when {
            error && (list == null || list.isEmpty()) -> ErrorBox(lang) { reload++ }
            list == null -> LoadingList()
            list.isEmpty() -> EmptyBox(
                lang,
                Icons.Filled.GetApp,
                L10n.t(lang, "no_results"),
            )
            else -> LazyColumn(Modifier.fillMaxSize()) {
                items(list) { a ->
                    AppRow(a, trailing = {}, onClick = { onOpen(a.id) })
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun UpdatesScreen(
    lang: String,
    installManager: InstallManager,
    downloads: MutableMap<String, DownloadState>,
) {
    val ctx = LocalContext.current
    var updates by remember { mutableStateOf<List<UpdateRow>?>(null) }
    var error by remember { mutableStateOf(false) }
    var reload by remember { mutableStateOf(0) }

    LaunchedEffect(reload) {
        error = false
        val installed = withContext(Dispatchers.IO) { installedPackages(ctx) }
        val rows = withContext(Dispatchers.IO) { AlphaApi.updates(installed) }
        updates = rows
        if (rows == null) error = true
    }

    Column(Modifier.fillMaxSize()) {
        SectionTitle(L10n.t(lang, "updates"))
        val u = updates
        when {
            error && u == null -> ErrorBox(lang) { reload++ }
            u == null -> LoadingList()
            u.isEmpty() -> EmptyBox(
                lang,
                Icons.Filled.CheckCircle,
                L10n.t(lang, "up_to_date"),
            )
            else -> LazyColumn(Modifier.fillMaxSize()) {
                items(u, key = { it.packageId }) { row ->
                    val state = downloads[row.packageId] ?: DownloadState.Idle
                    AppRow(
                        row.app,
                        trailing = {
                            when (state) {
                                is DownloadState.Downloading -> Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    LinearProgressIndicator(
                                        progress = { state.pct / 100f },
                                        color = AlphaAccent,
                                        trackColor = AlphaSurfaceHigh,
                                        modifier = Modifier.width(72.dp),
                                    )
                                    Text(
                                        state.pct.toString() + "%",
                                        fontSize = 10.sp,
                                        color = AlphaTextDim,
                                    )
                                }
                                is DownloadState.Ready -> TextButton(
                                    onClick = {
                                        installManager.install(row.packageId, state.uri)
                                    },
                                ) {
                                    Text(
                                        L10n.t(lang, "install"),
                                        color = Color(0xFF34D399),
                                    )
                                }
                                is DownloadState.Failed -> TextButton(
                                    onClick = {
                                        val url = row.apkUrl
                                        if (url != null) {
                                            if (installManager.enqueue(
                                                    row.packageId,
                                                    url,
                                                    row.app.name,
                                                )
                                            ) {
                                                downloads[row.packageId] =
                                                    DownloadState.Downloading(0)
                                            }
                                        }
                                    },
                                ) {
                                    Text(
                                        L10n.t(lang, "retry"),
                                        color = Color(0xFFF87171),
                                    )
                                }
                                DownloadState.Idle -> TextButton(
                                    onClick = {
                                        val url = row.apkUrl
                                        if (url != null) {
                                            if (installManager.enqueue(
                                                    row.packageId,
                                                    url,
                                                    row.app.name,
                                                )
                                            ) {
                                                downloads[row.packageId] =
                                                    DownloadState.Downloading(0)
                                            }
                                        }
                                    },
                                ) {
                                    Text(
                                        L10n.t(lang, "update"),
                                        color = AlphaAccent,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        },
                        onClick = {},
                    )
                    Text(
                        L10n.t(lang, "version") + " " + row.installedCode + "  →  " +
                            row.newVersionName + " (" + row.newVersionCode + ")",
                        color = AlphaTextDim,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 74.dp, bottom = 6.dp),
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun LibraryScreen(lang: String, onOpen: (String) -> Unit) {
    val ctx = LocalContext.current
    var installed by remember {
        mutableStateOf<List<Pair<String, Pair<String, Long>>>?>(null)
    }

    LaunchedEffect(Unit) {
        val all = withContext(Dispatchers.IO) { installedPackages(ctx) }
        installed = all.entries
            .filter {
                it.key == "com.alphaappstore.mobile" ||
                    it.key.startsWith("app.alpha.") ||
                    it.key.startsWith("game.alpha.")
            }
            .map { it.key to it.value }
            .sortedBy { it.first }
    }

    Column(Modifier.fillMaxSize()) {
        SectionTitle(L10n.t(lang, "library"))
        val list = installed
        if (list == null) {
            LoadingList()
        } else if (list.isEmpty()) {
            EmptyBox(lang, Icons.Filled.GetApp, L10n.t(lang, "no_results"))
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(list, key = { it.first }) { entry ->
                    val pkg = entry.first
                    val info = entry.second
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier
                                .size(44.dp)
                                .background(AlphaSurfaceHigh, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                pkg.takeLast(1).uppercase(),
                                color = AlphaViolet,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(
                                pkg,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = AlphaText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                L10n.t(lang, "version") + " " + info.first +
                                    " (" + info.second + ")",
                                fontSize = 11.sp,
                                color = AlphaTextDim,
                            )
                        }
                        TextButton(onClick = { onOpen(pkg) }) {
                            Text(L10n.t(lang, "open"), color = AlphaAccent)
                        }
                    }
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(
            label,
            color = AlphaTextDim,
            fontSize = 13.sp,
            modifier = Modifier.width(110.dp),
        )
        Text(
            value,
            color = AlphaText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
    appId: String,
    lang: String,
    installs: MutableMap<String, DownloadState>,
    onBack: () -> Unit,
    onDownload: (String, String, String) -> Unit,
) {
    val ctx = LocalContext.current
    var detail by remember { mutableStateOf<AppDetail?>(null) }
    var error by remember { mutableStateOf(false) }
    var reload by remember { mutableStateOf(0) }
    var installed by remember { mutableStateOf(false) }

    LaunchedEffect(reload) {
        error = false
        val d = withContext(Dispatchers.IO) { AlphaApi.detail(appId) }
        if (d == null) {
            error = true
        } else {
            detail = d
            installed = withContext(Dispatchers.IO) {
                installedPackages(ctx).containsKey(d.app.packageId)
            }
        }
    }

    Scaffold(
        containerColor = AlphaBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        detail?.app?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = AlphaText,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = L10n.t(lang, "back"),
                            tint = AlphaText,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AlphaBackground,
                ),
            )
        },
    ) { pad ->
        val d = detail
        if (d == null) {
            Column(Modifier.padding(pad).fillMaxSize()) {
                if (error) ErrorBox(lang) { reload++ } else LoadingList()
            }
            return@Scaffold
        }
        val latest = d.versions.firstOrNull()
        LazyColumn(Modifier.padding(pad).fillMaxSize()) {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppIconBox(d.app.name, 72, 30)
                    Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                        Text(
                            d.app.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = AlphaText,
                        )
                        Text(
                            d.app.developerName ?: L10n.t(lang, "unknown"),
                            color = AlphaAccent,
                            fontSize = 13.sp,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (d.app.ratingAvg != null) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(13.dp),
                                )
                                Text(
                                    " " + d.app.ratingAvg + " (" + d.app.ratingCount + ")",
                                    color = AlphaTextDim,
                                    fontSize = 12.sp,
                                )
                            } else {
                                Text(
                                    d.app.category.replaceFirstChar { it.uppercase() },
                                    color = AlphaTextDim,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
            item {
                val state = installs[d.app.packageId] ?: DownloadState.Idle
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    when {
                        installed -> Button(
                            onClick = { openStoreApp(ctx, d.app.packageId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AlphaSurfaceHigh,
                                contentColor = AlphaAccent,
                            ),
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            Text(
                                L10n.t(lang, "open"),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        state is DownloadState.Downloading -> Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            LinearProgressIndicator(
                                progress = { state.pct / 100f },
                                color = AlphaAccent,
                                trackColor = AlphaSurfaceHigh,
                                modifier = Modifier.width(120.dp),
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                L10n.t(lang, "installing") + " " + state.pct + "%",
                                fontSize = 11.sp,
                                color = AlphaTextDim,
                            )
                        }
                        state is DownloadState.Failed -> TextButton(
                            onClick = {
                                val url = latest?.apkUrl
                                if (url != null) {
                                    onDownload(d.app.packageId, url, d.app.name)
                                }
                            },
                        ) {
                            Text(L10n.t(lang, "retry"), color = Color(0xFFF87171))
                        }
                        state is DownloadState.Ready -> TextButton(onClick = {
                            InstallManager(ctx).install(d.app.packageId, state.uri)
                        }) {
                            Text(L10n.t(lang, "install"), color = Color(0xFF34D399))
                        }
                        latest?.apkUrl == null -> Text(
                            L10n.t(lang, "not_available"),
                            color = AlphaTextDim,
                            fontSize = 12.sp,
                        )
                        else -> Button(
                            onClick = {
                                val url = latest?.apkUrl
                                if (url == null) return@Button
                                if (canInstallPackages(ctx)) {
                                    onDownload(d.app.packageId, url, d.app.name)
                                } else {
                                    requestInstallPermission(ctx)
                                    Toast.makeText(
                                        ctx,
                                        L10n.t(lang, "install_perm"),
                                        Toast.LENGTH_LONG,
                                    ).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AlphaAccent,
                                contentColor = Color(0xFF04121A),
                            ),
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            Text(
                                L10n.t(lang, "install"),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
            item { SectionTitle(L10n.t(lang, "whats_new")) }
            item {
                Text(
                    latest?.releaseNotes ?: L10n.t(lang, "unknown"),
                    color = AlphaTextDim,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item { SectionTitle(L10n.t(lang, "details")) }
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    DetailRow(
                        L10n.t(lang, "version"),
                        latest?.versionName ?: L10n.t(lang, "unknown"),
                    )
                    DetailRow(
                        L10n.t(lang, "size"),
                        latest?.sizeBytes?.let { formatBytes(it) } ?: "—",
                    )
                    DetailRow(
                        L10n.t(lang, "updated"),
                        if (d.app.updatedAt > 0) {
                            java.text.DateFormat.getDateInstance()
                                .format(java.util.Date(d.app.updatedAt))
                        } else {
                            L10n.t(lang, "unknown")
                        },
                    )
                    DetailRow(
                        L10n.t(lang, "categories"),
                        d.app.category.replaceFirstChar { it.uppercase() },
                    )
                    DetailRow(
                        L10n.t(lang, "developer"),
                        d.app.developerName ?: L10n.t(lang, "unknown"),
                    )
                }
            }
            item { SectionTitle(L10n.t(lang, "description")) }
            item {
                Text(
                    d.description,
                    color = AlphaText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun SearchOverlay(lang: String, onClose: () -> Unit, onOpenApp: (String) -> Unit) {
    var q by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<StoreApp>?>(null) }

    LaunchedEffect(q) {
        val query = q.trim()
        if (query.length < 2) {
            results = null
            return@LaunchedEffect
        }
        results = withContext(Dispatchers.IO) { AlphaApi.search(query) }
    }

    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {},
        title = { Text(L10n.t(lang, "search"), color = AlphaText) },
        text = {
            Column {
                OutlinedTextField(
                    value = q,
                    onValueChange = { q = it },
                    singleLine = true,
                    placeholder = { Text(L10n.t(lang, "search"), color = AlphaTextDim) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AlphaAccent,
                        cursorColor = AlphaAccent,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(10.dp))
                val r = results
                if (q.trim().length < 2) {
                    Text(
                        L10n.t(lang, "search"),
                        color = AlphaTextDim,
                        fontSize = 12.sp,
                    )
                } else if (r == null) {
                    LoadingList()
                } else if (r.isEmpty()) {
                    Text(
                        L10n.t(lang, "no_results"),
                        color = AlphaTextDim,
                        fontSize = 13.sp,
                    )
                } else {
                    LazyColumn(Modifier.height(320.dp)) {
                        items(r) { a ->
                            AppRow(a, trailing = {}, onClick = { onOpenApp(a.id) })
                        }
                    }
                }
            }
        },
        containerColor = AlphaSurfaceHigh,
    )
}
