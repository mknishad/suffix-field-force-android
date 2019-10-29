package com.suffix.fieldforce.model


class TaskDashboardResponse(
    var responseCode: String,
    var responseData: List<TaskDashboardResponseData>,
    var responseMsg: String
)