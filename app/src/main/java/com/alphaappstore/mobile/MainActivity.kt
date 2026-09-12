package com.alphaappstore.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Futuristic premium palette shared across the whole app.
private val DeepSpace = Color(0xFF060B18)
private val Panel = Color(0xFF0C1526)
private val Cyan = Color(0xFF22D3EE)
private val Violet = Color(0xFFA78BFA)
private val TextHigh = Color(0xFFE6F1FF)
private val TextMid = Color(0xFF9FB0CC)
private val TextLow = Color(0xFF5B6B88)

enum class StoreTab(val label: String) {
    Featured("Featured"),
    Categories("Categories"),
    Trending("Trending"),
    Library("Library"),
}

data class StoreApp(
    val name: String,
    val category: String,
    val rating: Double,
    val sizeMb: Int,
    val tagline: String,
    val accent: Color,
)

data class CategoryTile(
    val name: String,
    val apps: Int,
    val accent: Color,
)

object Catalog {
    val featured = listOf(
        StoreApp("Nebula Player", "Entertainment", 4.8, 38, "Cinema-grade playback, zero ads.", Cyan),
        StoreApp("Pulse Fit", "Health", 4.7, 22, "Adaptive workouts that learn you.", Violet),
        StoreApp("Vault Notes", "Productivity", 4.9, 12, "Encrypted thoughts, instant sync.", Color(0xFF34D399)),
        StoreApp("Orbit Mail", "Communication", 4.6, 30, "Inbox at lightspeed.", Color(0xFFF472B6)),
    )
    val trending = listOf(
        StoreApp("Hyperdash", "Tools", 4.9, 18, "Device stats on your wallpaper.", Cyan),
        StoreApp("Lumen Camera", "Photography", 4.5, 44, "Night shots that feel alive.", Violet),
        StoreApp("Echo Chat", "Social", 4.4, 26, "Rooms that vanish when you leave.", Color(0xFF34D399)),
        StoreApp("Starling Pay", "Finance", 4.8, 21, "Send money like a text.", Color(0xFFFBBF24)),
        StoreApp("Drift Games", "Games", 4.3, 95, "Arcade classics, recharged.", Color(0xFFF472B6)),
        StoreApp("Skyscope", "Weather", 4.6, 14, "Storm tracking in real time.", Cyan),
    )
    val categories = listOf(
        CategoryTile("Games", 128, Cyan),
        CategoryTile("Tools", 94, Violet),
        CategoryTile("Social", 61, Color(0xFF34D399)),
        CategoryTile("Photography", 47, Color(0xFFF472B6)),
        CategoryTile("Finance", 39, Color(0xFFFBBF24)),
        CategoryTile("Entertainment", 72, Cyan),
    )
    val all get() = featured + trending
}

@Composable
fun AlphaStoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Cyan,
            onPrimary = Color(0xFF05222B),
            background = DeepSpace,
            onBackground = TextHigh,
            surface = Panel,
            onSurface = TextHigh,
            outline = Color(0xFF22314D),
        ),
        content = content,
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlphaStoreTheme {
                StoreScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen() {
    var tab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }
    val installed = remember { mutableStateOf(setOf<String>()) }
    val installing = remember { mutableStateOf(setOf<String>()) }
    val scope = rememberCoroutineScope()

    val requestInstall: (StoreApp) -> Unit = { app ->
        if (app.name !in installed.value && app.name !in installing.value) {
            installing.value = installing.value + app.name
            scope.launch {
                delay(1400)
                installing.value = installing.value - app.name
                installed.value = installed.value + app.name
            }
        }
    }

    Scaffold(
        containerColor = DeepSpace,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(
                                        Brush.linearGradient(listOf(Cyan, Violet)),
                                        RoundedCornerShape(10.dp),
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("A", color = Color(0xFF05222B), fontWeight = FontWeight.Black, fontSize = 17.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("Alpha Store", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextHigh)
                                Text("V1 - futuristic delivery", fontSize = 11.sp, color = TextMid)
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Person, contentDescription = "Account", tint = TextMid)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepSpace),
                )
                SearchField(query) { query = it }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                StoreTab.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = {
                            Icon(
                                imageVector = when (item) {
                                    StoreTab.Featured -> Icons.Filled.Star
                                    StoreTab.Categories -> Icons.Filled.Explore
                                    StoreTab.Trending -> Icons.Filled.LocalFireDepartment
                                    StoreTab.Library -> Icons.Filled.VideoLibrary
                                },
                                contentDescription = item.label,
                            )
                        },
                        label = { Text(item.label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Cyan,
                            selectedTextColor = Cyan,
                            indicatorColor = Cyan.copy(alpha = 0.16f),
                            unselectedIconColor = TextLow,
                            unselectedTextColor = TextLow,
                        ),
                    )
                }
            }
        },
    ) { padding ->
        when (tab) {
            0 -> FeaturedTab(padding, query, installed.value, installing.value, requestInstall)
            1 -> CategoriesTab(padding)
            2 -> TrendingTab(padding, installed.value, installing.value, requestInstall)
            else -> LibraryTab(padding, installed.value)
        }
    }
}

@Composable
fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
        placeholder = { Text("Search apps and games", fontSize = 13.sp, color = TextLow) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = TextLow, modifier = Modifier.size(18.dp))
        },
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Panel,
            unfocusedContainerColor = Panel,
            focusedBorderColor = Cyan.copy(alpha = 0.55f),
            unfocusedBorderColor = Color(0xFF1B2A45),
            focusedTextColor = TextHigh,
            unfocusedTextColor = TextHigh,
            cursorColor = Cyan,
        ),
    )
}

@Composable
fun FeaturedTab(
    padding: PaddingValues,
    query: String,
    installed: Set<String>,
    installing: Set<String>,
    onInstall: (StoreApp) -> Unit,
) {
    val apps = if (query.isBlank()) {
        Catalog.featured
    } else {
        Catalog.all.filter { it.name.contains(query, ignoreCase = true) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (query.isBlank()) {
            item { HeroBanner() }
        } else {
            item { SectionTitle("Results") }
        }
        item { SectionTitle(if (query.isBlank()) "Featured this week" else "Matching apps") }
        items(apps) { app ->
            AppCard(app, installed, installing, onInstall)
        }
        if (apps.isEmpty()) {
            item {
                Text("Nothing found. Try another name.", fontSize = 13.sp, color = TextMid)
            }
        }
    }
}

@Composable
fun TrendingTab(
    padding: PaddingValues,
    installed: Set<String>,
    installing: Set<String>,
    onInstall: (StoreApp) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { SectionTitle("Rising this week") }
        items(Catalog.trending) { app ->
            AppCard(app, installed, installing, onInstall)
        }
    }
}

@Composable
fun CategoriesTab(padding: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        gridItems(Catalog.categories) { cat ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Panel, RoundedCornerShape(16.dp))
                    .padding(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(cat.accent.copy(alpha = 0.16f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(Modifier.size(14.dp).background(cat.accent, RoundedCornerShape(4.dp)))
                }
                Spacer(Modifier.height(10.dp))
                Text(cat.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextHigh)
                Text(cat.apps.toString() + " apps", fontSize = 11.sp, color = TextMid)
            }
        }
    }
}

@Composable
fun LibraryTab(padding: PaddingValues, installed: Set<String>) {
    val apps = Catalog.all.filter { it.name in installed }
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
    ) {
        SectionTitle("My apps")
        Spacer(Modifier.height(12.dp))
        if (apps.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(Icons.Filled.VideoLibrary, contentDescription = null, tint = TextLow, modifier = Modifier.size(42.dp))
                Spacer(Modifier.height(12.dp))
                Text("Nothing installed yet", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextHigh)
                Text("Grab something from Featured and it lands here.", fontSize = 12.sp, color = TextMid)
            }
        } else {
            apps.forEach { app ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(app.accent.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(app.name.first().toString(), color = app.accent, fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(app.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextHigh)
                        Text("Installed - " + app.sizeMb + " MB", fontSize = 11.sp, color = TextMid)
                    }
                }
            }
        }
    }
}

@Composable
fun AppCard(
    app: StoreApp,
    installed: Set<String>,
    installing: Set<String>,
    onInstall: (StoreApp) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    Brush.linearGradient(listOf(app.accent.copy(alpha = 0.85f), app.accent.copy(alpha = 0.35f))),
                    RoundedCornerShape(14.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(app.name.first().toString(), fontWeight = FontWeight.Black, fontSize = 20.sp, color = Color(0xFF05131F))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(app.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextHigh)
            Text(app.category + " - " + app.sizeMb + " MB", fontSize = 11.sp, color = TextMid)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(3.dp))
                Text(app.rating.toString(), fontSize = 11.sp, color = TextMid)
            }
        }
        Spacer(Modifier.width(8.dp))
        InstallButton(app, installed, installing, onInstall)
    }
}

@Composable
fun InstallButton(
    app: StoreApp,
    installed: Set<String>,
    installing: Set<String>,
    onInstall: (StoreApp) -> Unit,
) {
    val shape = RoundedCornerShape(10.dp)
    val compact = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
    when {
        app.name in installed -> {
            Button(
                onClick = {},
                shape = shape,
                contentPadding = compact,
                modifier = Modifier.height(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14304A), contentColor = Cyan),
            ) {
                Text("Open", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        app.name in installing -> {
            Button(
                onClick = {},
                enabled = false,
                shape = shape,
                contentPadding = compact,
                modifier = Modifier.height(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cyan.copy(alpha = 0.18f), contentColor = Cyan),
            ) {
                Text("Installing...", fontSize = 12.sp)
            }
        }
        else -> {
            Button(
                onClick = { onInstall(app) },
                shape = shape,
                contentPadding = compact,
                modifier = Modifier.height(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cyan, contentColor = Color(0xFF05222B)),
            ) {
                Icon(Icons.Filled.GetApp, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(4.dp))
                Text("Install", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun HeroBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFF0F2038), Color(0xFF123B54), Color(0xFF172A4E))),
                RoundedCornerShape(18.dp),
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("ALPHA ORIGINALS", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = Cyan)
            Spacer(Modifier.height(6.dp))
            Text("Nebula Player", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = TextHigh)
            Text("Cinema-grade playback, zero ads. Free this week.", fontSize = 12.sp, color = TextMid)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.height(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cyan, contentColor = Color(0xFF05222B)),
            ) {
                Text("Get it free", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Brush.linearGradient(listOf(Cyan, Violet)), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color(0xFF05222B), modifier = Modifier.size(30.dp))
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(text, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextHigh)
}
