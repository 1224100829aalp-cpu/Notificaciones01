package mx.edu.utng.aalp.notificaciones.notificaciones.domain

import mx.edu.utng.aalp.notificaciones.notificaciones.domain.NotificationCategory
import mx.edu.utng.aalp.notificaciones.notificaciones.domain.NotificationMessage
/**
 * �� Repositorio de Mensajes
 *
 * Imagina que esto es una biblioteca: aquí guardamos todos los mensajes

 * disponibles y podemos buscarlos cuando queramos
 */
class NotificationRepository {
    // �� Base de datos de mensajes (en una app real, esto vendría de una DB)
    private val messages = listOf(
        NotificationMessage(
            id = 1,
            title = "¡Hora de brillar! ��",
    message = "Tu cerebro está listo para absorber conocimiento. ¡Dale una oportunidad! ����",
    emoji = "��",
    category = NotificationCategory.MOTIVATION
    ),
    NotificationMessage(
    id = 2,
    title = "¿Olvidaste tu estudio? ��",
    message = "¡Tu cerebro te está pidiendo ayuda! No lo dejes esperando ����",
    emoji = "��",
    category = NotificationCategory.REMINDER
    ),
    NotificationMessage(
    id = 3,
    title = "¡Pausa para el éxito! ⏸️",
    message = "Unos minutos de estudio hoy = Un futuro brillante mañana ✨",
    emoji = "⏸️",
    category = NotificationCategory.MOTIVATION
    ),
    NotificationMessage(
    id = 4,
    title = "¡Alerta de genio! ��",
    message = "Tu yo del futuro te agradecerá este momento de estudio ����",
    emoji = "��",
    category = NotificationCategory.WARNING
    ),
    NotificationMessage(
    id = 5,
    title = "Momento de superación ��‍♂️",
    message = "Cada página que lees te acerca más a tus metas. ¡Vamos! ��",
    emoji = "��‍♂️",
    category = NotificationCategory.ACHIEVEMENT
    ),
    NotificationMessage(
    id = 6,
    title = "¡Tu mente tiene hambre! ��",
    message = "Aliméntala con algo de conocimiento delicioso ����",
    emoji = "��",

    category = NotificationCategory.TIP
    ),
    NotificationMessage(
    id = 7,
    title = "Checkpoint alcanzado ��",
    message = "¡Es hora de subir de nivel! Abre ese libro y evoluciona ��⬆️",
    emoji = "��",
    category = NotificationCategory.ACHIEVEMENT
    ),
    NotificationMessage(
    id = 8,
    title = "Notificación épica ⚔️",
    message = "Los héroes también estudian. ¡Demuestra tu valentía! ��️��",
    emoji = "⚔️",
    category = NotificationCategory.MOTIVATION
    ),
    NotificationMessage(
    id = 9,
    title = "¡Desafío del día! ��",
    message = "Completa 30 minutos de estudio y desbloquea el nivel &#39;Cerebrito&#39; ����",
    emoji = "��",
    category = NotificationCategory.ACHIEVEMENT
    ),
    NotificationMessage(
    id = 10,
    title = "Recordatorio amistoso ��",
    message = "Estudiar no es aburrido cuando ves tu progreso. ¡Tú puedes! ��",
    emoji = "��",
    category = NotificationCategory.REMINDER
    )
    )
    /**
     * �� Obtiene un mensaje aleatorio
     */
    fun getRandomMessage(): NotificationMessage {
        return messages.random()
    }
    /**
     * �� Obtiene mensajes por categoría
     */
    fun getMessagesByCategory(category: NotificationCategory): List<NotificationMessage> {
        return messages.filter { it.category == category }
    }
    /**
     * �� Obtiene todos los mensajes

     */
    fun getAllMessages(): List<NotificationMessage> {
        return messages
    }
    /**
     * �� Obtiene un mensaje específico por ID
     */
    fun getMessageById(id: Int): NotificationMessage? {
        return messages.find { it.id == id }
    }
}