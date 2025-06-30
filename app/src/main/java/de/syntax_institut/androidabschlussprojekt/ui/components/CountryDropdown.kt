package de.syntax_institut.androidabschlussprojekt.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.utils.countryCodeToEmoji

@Composable
fun CountryDropdown(
    selectedCountry: String,
    onCountrySelected: (String) -> Unit
) {
    val countryList = listOf("de", "us", "fr", "tr", "it", "es", "gb", "jp", "cn")
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(12.dp)
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = countryCodeToEmoji(selectedCountry).plus(" ") + selectedCountry.uppercase())
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            countryList.forEach { countryCode ->
                DropdownMenuItem(
                    text = {
                        Text(text = "${countryCodeToEmoji(countryCode)} ${countryCode.uppercase()}")
                    },
                    onClick = {
                        onCountrySelected(countryCode)
                        expanded = false
                    }
                )
            }
        }
    }
}