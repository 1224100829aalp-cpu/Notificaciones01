package mx.edu.utng.aalp.notificaciones.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Instancia el gestor de notificaciones
        val notificationManager = AppNotificationManager(applicationContext)

        // Obtén un mensaje aleatorio
        val randomMessage = notificationManager.getRandomMessage()

        // Envía la notificación usando el método existente
        notificationManager.sendNotification(randomMessage)

        return Result.success()
    }
}