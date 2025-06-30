package de.syntax_institut.androidabschlussprojekt.utils

fun countryCodeToEmoji(countryCode: String): String {
    return countryCode
        .uppercase()
        .map { char -> 0x1F1E6 - 'A'.code + char.code }
        .map { code -> String(Character.toChars(code)) }
        .joinToString("")
}