package com.example.apppush

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
  // 토큰은 새 기기에서 앱 복원 / 앱 삭제 및 재설치 / 데이터 소거 등의 이유로 얼마든지 변경될 수 있기 때문에 토큰이 갱신될때마다 서버에 토큰을 갱신해준다.
  override fun onNewToken(token: String) {
    super.onNewToken(token)
  }
  
  // 실제 메세지를 수신할때마다 호출
  override fun onMessageReceived(msg: RemoteMessage) {
    super.onMessageReceived(msg)
    createNotificationChannel()
    
    val type = msg.data["type"]
      // enum과 동일해야 적용.
      ?.let {NotificationType.valueOf(it)}
    val title = msg.data["title"]
    val message = msg.data["message"]
    
    // type이 없을 경우 리턴
    type ?: return
    
    NotificationManagerCompat.from(this)
      .notify(type.id, createNotification(type, title, message))
  }
  
  private fun createNotificationChannel() {
    // 8.0 이상인 경우만 가능
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // 채널 생성 (NotificationChannel(아이디, 이름, 중요도)
      val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
      channel.description = CHANNEL_DESCRIPTION
      // 채널을 notification Manager에 추가
      (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .createNotificationChannel(channel)
    }
  }
  
  // 알림 컨텐츠 만들기
  private fun createNotification(type: NotificationType, title: String?, message: String?): Notification {
    // 알림 탭 설정
    val intent = Intent(this, MainActivity::class.java).apply {
      putExtra("notificationType", "${type.title} 타입")
      // FLAG_ACTIVITY_SINGLE_TOP > 같은 뷰일 경우 쌓이는게 아닌 onNewIntent 호출
      addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    // PendingIntent : 누군가에게 인텐트를 다룰 수 있는 권한을 준다. 코드 상에서는 notificationManager에게 전달한다.
    val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_MUTABLE)
    
    
    val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        // 아이콘
      .setSmallIcon(R.drawable.ic_noti)
        // 타이틀
      .setContentTitle(title)
        // 텍스트
      .setContentText(message)
        // 우선순위
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // 알림 탭 클릭
      .setContentIntent(pendingIntent)
        // 알림 클릭하면 알림 자동 삭제
      .setAutoCancel(true)
    
    when(type) {
      NotificationType.NORMAL -> Unit
      NotificationType.EXPANDABLE -> {
        notificationBuilder.setStyle(
          NotificationCompat.BigTextStyle()
            .bigText(
              "\uD83D\uDE00\n" +
                  "\uD83D\uDE03\n" +
                  "\uD83D\uDE04\n" +
                  "\uD83D\uDE01\n" +
                  "\uD83D\uDE06\n" +
                  "\uD83D\uDE05\n" +
                  "\uD83E\uDD23\n" +
                  "\uD83D\uDE02\n" +
                  "\uD83D\uDE42\n" +
                  "\uD83D\uDE43\n" +
                  "\uD83D\uDE09\n" +
                  "\uD83D\uDE0A\n" +
                  "\uD83D\uDE07\n" +
                  "\uD83E\uDD70\n" +
                  "\uD83D\uDE0D\n" +
                  "\uD83E\uDD29\n" +
                  "\uD83D\uDE18\n" +
                  "\uD83D\uDE17\n"
            )
        )
      }
      NotificationType.CUSTOM -> {
        notificationBuilder
          .setStyle(NotificationCompat.DecoratedCustomViewStyle())
          .setCustomContentView(
            RemoteViews(packageName, R.layout.view_custom_notification).apply {
              setTextViewText(R.id.title, title)
              setTextViewText(R.id.message, message)
            }
          )
      }
    }
    return notificationBuilder.build()
  }
  
  companion object {
    private const val CHANNEL_NAME = "Emoji Party"
    private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
    private const val CHANNEL_ID = "Channel Id"
  }
}