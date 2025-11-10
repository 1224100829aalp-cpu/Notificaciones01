package mx.edu.utng.aalp.notificaciones.notificaciones.domain

import androidx.core.app.NotificationCompat

/**
 * �� Modelo de datos para mensajes de notificación
 *
 * Es como una &quot;ficha técnica&quot; que describe cómo debe verse una notificación
 */
data class NotificationMessage(
    val id: Int,
    val title: String,
    val message: String,
    val emoji: String,
    val priority: Int = NotificationCompat.PRIORITY_HIGH,
    val category: NotificationCategory = NotificationCategory.REMINDER
)
/**
 * ��️ Categorías de notificaciones
 *
 * Diferentes &quot;sabores&quot; de notificaciones, como los sabores de helado ��
 */
enum class NotificationCategory {
    REMINDER, // Recordatorios gentiles
    MOTIVATION, // Mensajes motivadores
    ACHIEVEMENT, // Logros desbloqueados
    WARNING, // Advertencias importantes
    TIP // Consejos útiles
}