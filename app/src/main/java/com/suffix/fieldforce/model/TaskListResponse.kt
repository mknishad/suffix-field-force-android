package com.suffix.fieldforce.model

data class TaskListResponse(
  var responseCode: String?,
  var responseData: List<Task>,
  var responseMsg: String?
)