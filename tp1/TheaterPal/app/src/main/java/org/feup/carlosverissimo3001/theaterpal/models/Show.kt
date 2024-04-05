package org.feup.carlosverissimo3001.theaterpal.models

import org.json.JSONArray
import org.json.JSONObject

data class Show (
	val duration: Int,
	val releasedate: String,
	val dates: List<Date>,
	val description: String,
	val name: String,
	val picture: String,
	val picture_b64: String,
	val price: Int,
	val id: Int
) : java.io.Serializable {
	fun debug(): String {
		return "Show '$name', ID $id"
	}
}

data class Date (
	val date: String,
	val showdateid: Int
)

fun parseDate (jsonObject: JSONObject): Date {
	return Date(
			jsonObject.getString("date"),
			jsonObject.getInt("showdateid")
	)
}

fun parseShow (jsonObject: JSONObject): Show {
	return Show(
			jsonObject.getInt("duration"),
			jsonObject.getString("releasedate"),
			jsonObject.getJSONArray("dates").let {
				List(it.length()) { i ->
					parseDate(it.getJSONObject(i))
				}
			},
			jsonObject.getString("description"),
			jsonObject.getString("name"),
			jsonObject.getString("picture"),
			jsonObject.getString("picture_b64"),
			jsonObject.getInt("price"),
			jsonObject.getInt("showid")
	)
}