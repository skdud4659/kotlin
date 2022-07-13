package com.example.apppush

enum class NotificationType(val title: String, val id: Int) {
  NORMAL("일반", 0),
  EXPANDABLE("확장", 1),
  CUSTOM("커스텀", 2)
}