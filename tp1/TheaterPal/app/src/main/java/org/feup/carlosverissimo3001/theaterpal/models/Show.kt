package org.feup.carlosverissimo3001.theaterpal.models

import org.json.JSONArray
import org.json.JSONObject

data class Show (
	val dates: JSONArray,
	val description: String,
	val name: String,
	val picture: String,
	val picture_b64: String,
	val price: Int,
	val id: Int
) {
	fun debug(): String {
		return "Show '$name', ID $id"
	}
}

fun parseShow (jsonObject: JSONObject): Show {
	return Show(
			jsonObject.getJSONArray("dates"),
			jsonObject.getString("description"),
			jsonObject.getString("name"),
			jsonObject.getString("picture"),
			jsonObject.getString("picture_b64"),
			jsonObject.getInt("price"),
			jsonObject.getInt("showid"),
	)
}