package akhmedoff.usman.data.model

class ResponseVideo(
    val count: Int,
    val items: List<Video>,
    val profiles: List<User>?,
    val groups: List<Group>?
)