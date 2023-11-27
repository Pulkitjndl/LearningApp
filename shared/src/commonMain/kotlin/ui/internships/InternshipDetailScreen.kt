package ui.internships

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.InternshipApiModel

@Composable
fun InternshipDetailScreen(internship: InternshipApiModel.Internship,onBackPress:() -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(internship.profileName) },
                navigationIcon = {
                    IconButton(onClick = { onBackPress.invoke() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Company: ${internship.companyName}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Location: ${internship}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Start Date: ${internship.startDate}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Handle additional actions */ }) {
                    Text(text = "Apply Now")
                }
            }
        }
    )
}
