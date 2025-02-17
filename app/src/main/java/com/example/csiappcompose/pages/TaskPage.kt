import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.PreviousTask

@Composable
fun TaskPage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue, // Change button background color
                        contentColor = Color.White   // Change text/icon color
                    )
                ) {
                    Text(text = "Create Task")
                }
            }

            item { TaskSection("Current Tasks", sampleTasks()) }
            item { TaskSection("Pending Tasks", sampleTasks()) }
            item { TaskSection("Previous Tasks", sampleTasks()) }
        }
    }
}

@Composable
fun TaskSection(title: String, tasks: List<PreviousTask>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            Text(
                text = title,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items(tasks) { item ->
                TaskItem(item)
            }
        }
    }
}

@Composable
fun TaskItem(task: PreviousTask) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(150.dp)
            .border(2.dp, Color.Blue, shape = RoundedCornerShape(16.dp)), // Border with rounded corners
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = task.data, fontSize = 12.sp, color = Color.Black)
            Text(text = task.name, fontSize = 26.sp,color = Color.Red, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(38.dp))
            LinearProgressIndicator(
                progress = task.progress / 100f, // Convert percentage to float value (0-1)
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(8.dp,0.dp,8.dp,0.dp),
                color = Color.Blue,
                trackColor = Color.LightGray
            )

            // Progress Text
            Text(
                text = "Progress: ${task.progress}%",
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}

fun sampleTasks() = listOf(
    PreviousTask("10/1/1992", "Task 1", 100),
    PreviousTask("10/2/1992", "Task 2", 75),
    PreviousTask("10/3/1992", "Task 3", 50),
    PreviousTask("10/4/1992", "Task 4", 25),
)

@Preview
@Composable
fun PreviewHomePage() {
    TaskPage()
}
