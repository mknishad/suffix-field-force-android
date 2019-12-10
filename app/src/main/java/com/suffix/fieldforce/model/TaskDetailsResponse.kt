package com.suffix.fieldforce.model


class TaskDetailsResponse(
  var responseCode: String,
  var responseData: List<Task>,
  var responseMsg: String
)