import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.PreviousTask
import com.example.csiappcompose.TaskDetailsActivity
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor

@Composable
fun TaskPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier.background(color = PrimaryBackgroundColor)
            .fillMaxSize()

    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(color = PrimaryBackgroundColor),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Button(
                    onClick = {
//                        context.let { ctx -> // ✅ Ensuring context isn't null
//                            val intent = Intent(ctx, CreateTaskActivity()::class.java).apply {
//                            }
//                            ctx.startActivity(intent) // ✅ Starting activity with context
//                        }
                        },
                    modifier = Modifier.padding(top = 70.dp )
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue, // Change button background color
                        contentColor = Color.White   // Change text/icon color
                    )
                ) {
                    Text(text = "+ Create Task")
                }
            }

            item { TaskSection("Current Tasks", sampleTasks()) }
            item { TaskSection("Pending Tasks", sampleTasks() )}
            item { TaskSection("Previous Tasks", sampleTasks()) }


        }
    }
}

@Composable
fun TaskSection(title: String, tasks: List<PreviousTask>) {
    Column(modifier = Modifier.fillMaxWidth().background(color = PrimaryBackgroundColor)) {
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
            modifier = Modifier.background(color = Color.Transparent)
                .fillMaxWidth()

        ) {
            items(tasks) { item ->
                TaskItem(item)
            }
        }


        if(title.equals("Previous Tasks")) Spacer(Modifier.height(110.dp))
    }
}

@Composable
fun TaskItem(task: PreviousTask) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(150.dp)
            .border(1.5.dp, Color.Blue, shape = RoundedCornerShape(16.dp))
            .clickable {
                context.let { ctx -> // ✅ Ensuring context isn't null
                    val intent = Intent(ctx, TaskDetailsActivity::class.java).apply {
                        putExtra("data", task.data)
                        putExtra("name", task.name)
                        putExtra("progress", task.progress)
                    }
                    ctx.startActivity(intent) // ✅ Starting activity with context
                }
            },// Border with rounded corners
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = PrimaryBackgroundColor)
            ,
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
    TaskPage(modifier = Modifier)
}
