package mx.edu.utng.aalp.notificaciones.utils
import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mx.edu.utng.aalp.notificaciones.MainActivity
import mx.edu.utng.aalp.notificaciones.R
import mx.edu.utng.aalp.notificaciones.notificaciones.domain.NotificationRepository
import mx.edu.utng.aalp.notificaciones.notificaciones.domain.NotificationMessage
import mx.edu.utng.aalp.notificaciones.notificaciones.domain.NotificationCategory




/**
 * 📬 AppNotificationManager Mejorado
 *
 * Usa NotificationRepository para obtener mensajes de forma organizada.
 */
class AppNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "motivation_channel"
    }

    private val repository = NotificationRepository()

    /**
     * 💡 Envía notificación usando un NotificationMessage
     */
    fun sendNotification(notificationMessage: NotificationMessage) {
        if (!checkNotificationPermission()) {
            println("⚠️ No hay permiso para enviar notificaciones")
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_id", notificationMessage.id)
            putExtra("category", notificationMessage.category.name)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationMessage.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notificationMessage.title)
            .setContentText(notificationMessage.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationMessage.message))
            .build()

        NotificationManagerCompat.from(context).notify(notificationMessage.id, notification)
        println("✅ Notificación enviada: ${notificationMessage.title}")
    }

    /**
     * 🎲 Envía una notificación aleatoria
     */
    fun sendRandomNotification() {
        val message = repository.getRandomMessage()
        sendNotification(message)
    }

    /**
     * 📂 Envía notificación de una categoría específica
     */
    fun sendNotificationByCategory(category: NotificationCategory) {
        val messages = repository.getMessagesByCategory(category)
        if (messages.isNotEmpty()) {
            val message = messages.random()
            sendNotification(message)
        } else {
            println("⚠️ No hay mensajes en la categoría: $category")
        }
    }

    /**
     * 🖼️ Envía notificación con imagen (9.2)
     */
    fun sendImageNotification(notificationMessage: NotificationMessage, imageRes: Int) {
        if (!checkNotificationPermission()) return

        val bitmap = BitmapFactory.decodeResource(context.resources, imageRes)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, notificationMessage.id, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notificationMessage.title)
            .setContentText(notificationMessage.message)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(notificationMessage.id, notification)
    }

    /**
     * 🎛️ Envía notificación con botones de acción (9.3)
     */
    fun sendActionNotification(notificationMessage: NotificationMessage) {
        if (!checkNotificationPermission()) return

        // Reemplazar con tus actividades/receptores reales
        val studyIntent = Intent(context, MainActivity::class.java)
        val studyPendingIntent = PendingIntent.getActivity(
            context, 0, studyIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(context, MainActivity::class.java)
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context, 1, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notificationMessage.title)
            .setContentText(notificationMessage.message)
            .addAction(R.drawable.ic_study, "🚀 Estudiar Ahora", studyPendingIntent)
            .addAction(R.drawable.ic_snooze, "⏰ En 15 min", snoozePendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationMessage.id, notification)
    }

    /**
     * 🔒 Verifica permisos de notificación
     */
    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    /**
     * 📊 Obtiene la cantidad total de mensajes disponibles
     */
    fun getAvailableMessagesCount(): Int {
        return repository.getAllMessages().size
    }
    fun getRandomMessage(): NotificationMessage {
        return repository.getRandomMessage()
    }

}