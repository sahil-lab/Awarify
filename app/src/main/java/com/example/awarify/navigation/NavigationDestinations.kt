package com.example.awarify.navigation

enum class NavigationDestinations(val route: String) {
    HOME("home"),
    HOBBIES("hobbies"),
    HOBBY_DETAIL("hobby_detail/{hobbyId}"),
    ADD_HOBBY("add_hobby"),
    EDIT_HOBBY("edit_hobby/{hobbyId}"),
    CALENDAR("calendar"),
    NOTIFICATIONS("notifications"),
    SETTINGS("settings"),
    TIMETABLE("timetable"),
    ADD_TASK("add_task/{hobbyId}"),
    TASK_DETAIL("task_detail/{taskId}")
}

object NavigationArguments {
    const val HOBBY_ID = "hobbyId"
    const val TASK_ID = "taskId"
} 