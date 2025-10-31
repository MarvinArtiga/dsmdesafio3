package com.example.agenciadeviajes


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val name: Name = Name(""),
    val capital: List<String> = listOf(""),
    val flags: Flags = Flags("")
) : Parcelable {
    val nameText: String get() = name.common
    val capitalName: String get() = capital.firstOrNull() ?: "Sin capital"
    val flagUrl: String get() = flags.png
}

@Parcelize
data class Name(val common: String = "") : Parcelable

@Parcelize
data class Flags(val png: String = "") : Parcelable
