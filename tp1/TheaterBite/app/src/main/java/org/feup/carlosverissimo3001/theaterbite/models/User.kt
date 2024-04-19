package org.feup.carlosverissimo3001.theaterbite.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val name: String,
    val nif: String,
    val publicKey: String,
    val userid: String
) : Parcelable