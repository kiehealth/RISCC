package com.cronelab.riscc.support.data.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = DatabaseConfigs.tbl_app_analytics_type)
class DBAppAnalyticsType {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id = 0

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("appAnalyticsKeyDataType")
    @Expose
    var appAnalyticsDataType: String? = null
}