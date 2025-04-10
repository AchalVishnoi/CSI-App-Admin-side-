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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.TaskDetailsActivity
import com.example.csiappcompose.dataModelsResponseTask.Completed
import com.example.csiappcompose.dataModelsResponseTask.Current
import com.example.csiappcompose.dataModelsResponseTask.Group
import com.example.csiappcompose.dataModelsResponseTask.Member
import com.example.csiappcompose.dataModelsResponseTask.Pending
import com.example.csiappcompose.dataModelsResponseTask.TaskData
import com.example.csiappcompose.pages.CreateTaskActivity
import com.example.csiappcompose.pages.ShimmerEffect
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import com.example.csiappcompose.viewModels.HomePageViewModel
import com.example.csiappcompose.viewModels.HomePageViewModelFactory
import com.example.csiappcompose.viewModels.NetWorkResponse
import com.example.csiappcompose.viewModels.TaskPageViewModel
import com.example.csiappcompose.viewModels.TaskPageViewModelFactory

@Preview
@Composable
fun TaskPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: TaskPageViewModel = viewModel(factory = TaskPageViewModelFactory(context))
    val tokenState = viewModel.token.collectAsState(initial = "")
    val token = tokenState.value

    val task = viewModel.taskPage.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchToken()
    }

    when (val response = task.value) {
        is NetWorkResponse.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Failed to load data: ${response.message}", color = Color.Red)
            }
        }

        is NetWorkResponse.Loading -> {
            ShimmerEffect(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Gray)
            )
        }

        is NetWorkResponse.Success -> {
            val taskSetValue = response.data as TaskData

            taskUi(
                completed =taskSetValue.completed,
                pending=taskSetValue.pending,
                current=taskSetValue.current,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
            )
        }
        else->{
            Text("No data available")

        }
    }
}
@Composable
fun taskUi(
    completed: List<Completed> = listOf(
        Completed("attachment", 57.0f, "abouttask", "endDate", listOf(Group(
            1, listOf(Member("domain", "name", 1)), "name")), 1, "informationUrl", "startingDate", "status", "Title")
    ),
    pending: List<Pending> = listOf(
        Pending("attachment", 75, "abouttask", "endDate",listOf(Group(
            1, listOf(Member("domain", "name", 1)), "name")), 1, "informationUrl", "startingDate", "status", "Title")
    ),
    current: List<Current> = listOf(
        Current("attachment", 75, "aboutTask", "endDate", listOf(Group(
            1, listOf(Member("domain", "name", 1)), "name")), 1, "informationUrl", "startingDate", "status", "Title")
    ),

    modifier: Modifier
   ){
    val context = LocalContext.current

    Box(
        modifier = modifier.background(color = PrimaryBackgroundColor)
            .fillMaxSize()

    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(color = PrimaryBackgroundColor),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Button(
                    onClick = {
                        context.let { ctx -> // ✅ Ensuring context isn't null
                            val intent = Intent(ctx, CreateTaskActivity()::class.java).apply {
                            }
                            ctx.startActivity(intent) // ✅ Starting activity with context
                        }
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

            item { TaskSection1("Current Tasks", current) }
            item { TaskSection2("Pending Tasks", pending )}
            item { TaskSection3("Previous Tasks", completed) }


        }
    }
}

@Composable
fun TaskSection1(title: String, tasks: List<Current>) {
    Column(modifier = Modifier.fillMaxWidth().background(color = PrimaryBackgroundColor)) {
        Row {
            Text(
                text = title,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
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
                TaskItem1(item)
            }
        }


        if(title.equals("Previous Tasks")) Spacer(Modifier.height(110.dp))
    }
}

@Composable
fun TaskSection2(title: String, tasks: List<Pending>) {
    Column(modifier = Modifier.fillMaxWidth().background(color = PrimaryBackgroundColor)) {
        Row {
            Text(
                text = title,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
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
                TaskItem2(item)
            }
        }


        if(title.equals("Previous Tasks")) Spacer(Modifier.height(110.dp))
    }
}

@Composable
fun TaskSection3(title: String, tasks: List<Completed>) {
    Column(modifier = Modifier.fillMaxWidth().background(color = PrimaryBackgroundColor)) {
        Row {
            Text(
                text = title,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
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
                TaskItem3(item)
            }
        }


        if(title.equals("Previous Tasks")) Spacer(Modifier.height(110.dp))
    }
}


@Composable
fun TaskItem1(task: Current) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(180.dp)
            .height(180.dp)
            .border(1.5.dp, Color.Blue, shape = RoundedCornerShape(16.dp))
            .clickable {
                context.let { ctx -> // ✅ Ensuring context isn't null
                    val intent = Intent(ctx, TaskDetailsActivity::class.java).apply {
                        putExtra("date", task.start_date)
                        putExtra("name", task.title)
                        putExtra("progress", task.current_progress)
                        putExtra("description", task.description)
                        task.attachment?.let {
                            putExtra("attachment", it.toString())
                        }
                        putExtra("group", task.groups.joinToString { it.name })
                    }
                    ctx.startActivity(intent) // ✅ Starting activity with context
                }
            },// Border with rounded corners
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.White).padding(8.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = task.start_date, fontSize = 12.sp, color = Color.Black)
            Text(text = task.title, fontSize = 20.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                        maxLines = 1,
                  overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(38.dp))
            LinearProgressIndicator(
                progress = task.current_progress / 100f, // Convert percentage to float value (0-1)
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(8.dp,0.dp,8.dp,0.dp),
                color = Color.Blue,

                )

            // Progress Text
            Text(
                text = "Progress: ${task.current_progress}%",
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun TaskItem2(task: Pending) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(180.dp)
            .height(180.dp)
            .border(1.5.dp, Color.Blue, shape = RoundedCornerShape(16.dp))
            .clickable {
                context.let { ctx -> // ✅ Ensuring context isn't null
                    val intent = Intent(ctx, TaskDetailsActivity::class.java).apply {
                        putExtra("date", task.start_date)
                        putExtra("name", task.title)
                        putExtra("progress", task.current_progress)
                        putExtra("description", task.description)
                        task.attachment?.let {
                            putExtra("attachment", it.toString())
                        }
                        putExtra("group", task.groups.joinToString { it.name })
                    }
                    ctx.startActivity(intent) // ✅ Starting activity with context
                }
            },// Border with rounded corners
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.White).padding(8.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = task.start_date, fontSize = 12.sp, color = Color.Black)
            Text(text = task.title, fontSize = 20.sp,color = Color.Red, fontWeight = FontWeight.Bold
                   , maxLines = 1,
                overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(38.dp))
            LinearProgressIndicator(
                progress = {
                    task.current_progress / 100f // Convert percentage to float value (0-1)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(8.dp,0.dp,8.dp,0.dp),
                color = Color.Blue,
            )

            // Progress Text
            Text(
                text = "Progress: ${task.current_progress}%",
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun TaskItem3(task: Completed) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(180.dp)
            .height(180.dp)
            .border(1.5.dp, Color.Blue, shape = RoundedCornerShape(16.dp))
            .clickable {
                context.let { ctx -> // ✅ Ensuring context isn't null
                    val intent = Intent(ctx, TaskDetailsActivity::class.java).apply {
                        putExtra("date", task.start_date)
                        putExtra("name", task.title)
                        putExtra("progress", task.current_progress)
                        putExtra("description", task.description)
                        task.attachment?.let {
                            putExtra("attachment", it.toString())
                        }
                        putExtra("group", task.groups.joinToString { it.name })
                    }
                    ctx.startActivity(intent) // ✅ Starting activity with context
                }
            },// Border with rounded corners
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.White).padding(8.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = task.start_date, fontSize = 12.sp, color = Color.Black)
            Text(text = task.title, fontSize = 20.sp,color = Color.Red, fontWeight = FontWeight.Bold
                , maxLines = 1,
                overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(38.dp))

            // Progress Text
            LinearProgressIndicator(
                progress = { task.current_progress / 100f }, // Convert percentage to float value (0-1)
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(8.dp,0.dp,8.dp,0.dp),
                color = Color.Blue,

                )
            Text(
                text = "Progress: ${task.current_progress}",
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}
