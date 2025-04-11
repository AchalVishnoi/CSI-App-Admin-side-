package com.example.csiappcompose.pages

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.R
import com.example.csiappcompose.TopBar
import com.example.csiappcompose.dataModelsResponseTask.GroupX
import com.example.csiappcompose.ui.theme.lightSkyBlue
import com.example.csiappcompose.viewModels.CreateTaskViewModel
import com.example.csiappcompose.viewModels.CreateTaskViewModelFactory

@Preview
@Composable
fun CreateTask() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val viewModel: CreateTaskViewModel = viewModel(factory = CreateTaskViewModelFactory(context))
    val activity = (context as? Activity)



    androidx.compose.material.Scaffold(
        modifier = Modifier.fillMaxSize().padding(top = 0.dp),
        topBar = {
            TopBar()
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(lightSkyBlue)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                androidx.compose.material.IconButton(onClick = { activity?.finish() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Create Task",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            inputField(
                message = viewModel.title,
                placeholder ="Name",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            inputField(
                message = viewModel.description,
                placeholder ="Description",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            //YearDomainSelector(viewModel)

            Spacer(Modifier.height(16.dp))

            //GroupCounter(viewModel)

            Spacer(Modifier.height(16.dp))

            viewModel.groups.forEachIndexed { index, group ->
                ExpandableGroupCard(
                    groupIndex = index,
                    group = group,
                    onUpdate = { viewModel.updateGroup(index, it) }
                )
            }

            Spacer(Modifier.height(16.dp))

            //DateRow(viewModel)

            Spacer(Modifier.height(16.dp))

            inputField(
                message = viewModel.attachmentUrl,
                placeholder = "Attachments",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.submitTask() },
                modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color.White),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Create Task")
            }
        }
    }
}

@Composable
fun ExpandableGroupCard(groupIndex: Int, group: GroupX, onUpdate: (GroupX) -> Unit) {
    var expanded by remember { mutableStateOf(true) }

    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            ) {
                Text("Group ${groupIndex + 1} Members", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
            }

   //         if (expanded) {
//                group.domains.forEach { domain ->
//                    Text(domain.name, style = MaterialTheme.typography.labelLarge)
//
//                    domain.members.forEach { member ->
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(start = 12.dp)
//                        ) {
//                            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(32.dp))
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Column(modifier = Modifier.weight(1f)) {
//                                Text(member.fullName)
//                                Text("${member.year} year", style = MaterialTheme.typography.bodySmall)
//                            }
//                            Checkbox(
//                                checked = domain.selectedMembers.contains(member),
//                                onCheckedChange = {
////                                    val updated = domain.toggleMember(member)
////                                    onUpdate(group.copy(domains = group.domains.map {
////                                        if (it.name == domain.name) updated else it
////                                    }))
//                                }
//                            )
//                        }
//                    }
//                }
//            }
       }
  }
}


//@Composable
//fun YearDomainSelector(viewModel: CreateTaskViewModel) {
//
//    Column {
//        Text("Select Year(s) and Domain(s)", style = MaterialTheme.typography.titleMedium)
//        Spacer(Modifier.height(8.dp))
//
//        // Just mock-up checkboxes. Replace with your logic.
//        listOf("1st Year", "2nd Year", "3rd Year", "4th Year").forEach { year ->
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Checkbox(
//                    checked = viewModel.selectedYears.contains(year),
//                    onCheckedChange = {
//                        if (it) viewModel.selectedYears.add(year)
//                        else viewModel.selectedYears.remove(year)
//                    }
//                )
//                Text(year)
//            }
//        }
//    }
//}


@Composable
fun inputField(
    message: String,
    placeholder: String = "Enter text",
    //error: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = Color(0xFFF7F7F7))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            TextField(
                value = message,
                onValueChange = {
                    //message= it.trimStart() // avoid accidental leading spaces
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 120.dp),
                placeholder = { Text(text = placeholder, fontSize = 16.sp) },
                maxLines = 6,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                textStyle = TextStyle(fontSize = 16.sp)
            )

//            error?.let {
//                Text(
//                    text = it,
//                    color = Color.Red,
//                    fontSize = 12.sp,
//                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
//                )
//            }
        }
    }


}

