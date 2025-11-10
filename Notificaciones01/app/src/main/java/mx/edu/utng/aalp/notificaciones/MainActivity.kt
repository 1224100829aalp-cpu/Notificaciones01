package mx.edu.utng.aalp.notificaciones

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import mx.edu.utng.aalp.notificaciones.notificaciones.domain.NotificationCategory
import java.util.concurrent.TimeUnit
import mx.edu.utng.aalp.notificaciones.utils.NotificationHelper
import mx.edu.utng.aalp.notificaciones.utils.AppNotificationManager
import mx.edu.utng.aalp.notificaciones.utils.NotificationWorker

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) println("✅ Permiso concedido para notificaciones")
        else println("❌ Permiso denegado")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)
        checkAndRequestNotificationPermission()

        setContent {
            NotificationAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationScreen()
                }
            }
        }

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(NotificationHelper.calculateDelayUntil9AM(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> println("✅ Ya tenemos permiso")
                else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @Composable
    fun NotificationScreen() {
        var notificationCount by remember { mutableStateOf(0) }
        var selectedCategory by remember { mutableStateOf<NotificationCategory?>(null) }
        val notificationManager = remember { AppNotificationManager(this) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Encabezado
            Text(
                text = "📢 Notificaciones Inteligentes",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Elige tu tipo de motivación favorita",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selector de categorías
            CategorySelector(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón principal
            Button(
                onClick = {
                    if (selectedCategory != null) {
                        notificationManager.sendNotificationByCategory(selectedCategory!!)
                    } else {
                        notificationManager.sendRandomNotification()
                    }
                    notificationCount++
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (selectedCategory != null) {
                        "📬 Enviar ${getCategoryName(selectedCategory!!)}"
                    } else {
                        "🎲 Sorpréndeme"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Estadísticas
            StatsCard(
                notificationCount = notificationCount,
                availableMessages = notificationManager.getAvailableMessagesCount()
            )

            // Tip
            TipCard()
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun CategorySelector(
        selectedCategory: NotificationCategory?,
        onCategorySelected: (NotificationCategory?) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Categorías:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryChip(
                    label = "📌 Todas",
                    isSelected = selectedCategory == null,
                    onClick = { onCategorySelected(null) }
                )
                NotificationCategory.values().forEach { category ->
                    CategoryChip(
                        label = "${getCategoryEmoji(category)} ${getCategoryName(category)}",
                        isSelected = selectedCategory == category,
                        onClick = { onCategorySelected(category) }
                    )
                }
            }
        }
    }

    @Composable
    fun CategoryChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
        FilterChip(
            selected = isSelected,
            onClick = onClick,
            label = { Text(text = label, fontSize = 13.sp) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }

    @Composable
    fun StatsCard(notificationCount: Int, availableMessages: Int) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(icon = "📤", value = "$notificationCount", label = "Enviadas")
                Divider(
                    modifier = Modifier.height(60.dp).width(1.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f)
                )
                StatItem(icon = "📥", value = "$availableMessages", label = "Disponibles")
            }
        }
    }

    @Composable
    fun StatItem(icon: String, value: String, label: String) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }

    @Composable
    fun TipCard() {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "💡", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
                Column {
                    Text(
                        text = "¿Sabías qué?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Puedes personalizar los mensajes en AppNotificationManager.kt",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// Funciones auxiliares para categorías
fun getCategoryName(category: NotificationCategory): String = when (category) {
    NotificationCategory.REMINDER -> "Recordatorio"
    NotificationCategory.MOTIVATION -> "Motivación"
    NotificationCategory.ACHIEVEMENT -> "Logro"
    NotificationCategory.WARNING -> "Advertencia"
    NotificationCategory.TIP -> "Consejo"
}

fun getCategoryEmoji(category: NotificationCategory): String = when (category) {
    NotificationCategory.REMINDER -> "⏰"
    NotificationCategory.MOTIVATION -> "💪"
    NotificationCategory.ACHIEVEMENT -> "🏆"
    NotificationCategory.WARNING -> "⚠️"
    NotificationCategory.TIP -> "💡"
}

@Composable
fun NotificationAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF6200EE),
            secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6),
            tertiary = androidx.compose.ui.graphics.Color(0xFFFF9800)
        ),
        content = content
    )
}