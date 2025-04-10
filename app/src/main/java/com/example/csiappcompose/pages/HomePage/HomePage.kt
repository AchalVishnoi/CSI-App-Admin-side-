import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.constraintlayout.compose.State
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.csiappcompose.R
import com.example.csiappcompose.dataModelsResponse.EventItem
import com.example.csiappcompose.dataModelsResponse.HomePageStats
import com.example.csiappcompose.dataModelsResponse.announcmentDisplay
import com.example.csiappcompose.pages.Chat.ProfileImage
import com.example.csiappcompose.pages.ShimmerEffect
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import com.example.csiappcompose.viewModels.HomePageViewModel
import com.example.csiappcompose.viewModels.HomePageViewModelFactory
import com.example.csiappcompose.viewModels.NetWorkResponse
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.csiappcompose.TaskDetailsActivity
import com.example.csiappcompose.pages.HomePage.CreateEvent
import com.example.csiappcompose.pages.HomePage.CreateEventActivity


@Composable
fun HomePage(modifier: Modifier = Modifier,selected: MutableState<String?>) {

    val context = LocalContext.current
    val viewModel: HomePageViewModel = viewModel(factory = HomePageViewModelFactory(context))

    var homePageStats = viewModel.homeStats.observeAsState()
    val tokenState = viewModel.token.collectAsState(initial = "")
    val token = tokenState.value

    var announcmentsList=viewModel.announcements.observeAsState()


    var ongoingEvents=viewModel.ongoingEvents.observeAsState()
    var upcomingEvent=viewModel.upcomingEvents.observeAsState()


    LaunchedEffect(Unit) {
        viewModel.fetchToken()
    }

    Box(
        modifier = modifier
            .background(color = PrimaryBackgroundColor)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = PrimaryBackgroundColor)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Adds spacing between items

        ) {



                Spacer(modifier= Modifier.height(70.dp))




            //upper section

                when (val response = homePageStats.value) {
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
                                .height(200.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Gray)
                        )
                    }

                    is NetWorkResponse.Success -> {
                        val homePageStatsValue = response.data
                        upperView(
                            name = homePageStatsValue.name,
                            chatCnt = homePageStatsValue.chat_groups_with_unread,
                            announcmentCnt = homePageStatsValue.announcement_count,
                            taskCnt = homePageStatsValue.tasks_assigned,
                            photo = homePageStatsValue.photo,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }

                    else -> {
                        Text(text = "Unexpected state", color = Color.Gray)
                    }
                }

         //announcment section
            when(val response=announcmentsList.value){

                    is NetWorkResponse.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Failed to load data: ${response.message}",
                                color = Color.Red
                            )
                        }
                    }

                    is NetWorkResponse.Loading -> {

                        Row {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .weight(2f)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .size(50.dp)
                                    .background(Color.Gray)
                            )
                        }

                        ShimmerEffect(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Gray)
                        )
                    }

                    is NetWorkResponse.Success -> {

                        val response = response.data
                        if(response.isNotEmpty()){
                            Column(modifier = Modifier.fillMaxWidth().background(color = PrimaryBackgroundColor)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Row (verticalAlignment = Alignment.CenterVertically){
                                        Icon(
                                            painter = painterResource(id = R.drawable.announcment_icon),
                                            contentDescription = "add announcement",
                                            modifier = Modifier.padding(start = 20.dp).size(20.dp)
                                        )

                                        Text(
                                            text = "Announcements",
                                            fontSize = 20.sp,
                                            color = Color.Black,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                    when (val response = homePageStats.value) {
                                        is NetWorkResponse.Error -> { }

                                        is NetWorkResponse.Loading -> {
                                            ShimmerEffect(
                                                modifier = Modifier
                                                    .padding(horizontal = 20.dp)
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .size(50.dp)
                                                    .background(Color.LightGray)
                                            )
                                        }

                                        is NetWorkResponse.Success -> {
                                            val homePageStatsValue = response.data
                                            if(homePageStatsValue.year!="2nd"){
                                                Icon(
                                                    painter = painterResource(id = R.drawable.add_icon),
                                                    contentDescription = "add announcement",
                                                    modifier = Modifier.padding(end = 20.dp).size(25.dp)
                                                                             )
                                                }

                                        }

                                        else -> {
                                            Text(text = "Unexpected state", color = Color.Gray)
                                        }
                                    }










                                }

                                annuncements(announcmentsList=response)




                            }
                        }

                    }

                    else -> {
                        Text(text = "Unexpected state", color = Color.Gray)
                    }
            }



            when(val response=upcomingEvent.value){

                is NetWorkResponse.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load data: ${response.message}",
                            color = Color.Red
                        )
                    }
                }

                is NetWorkResponse.Loading -> {

                    Row(modifier = Modifier.fillMaxWidth()) {



                        ShimmerEffect(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .size(50.dp)
                                .background(Color.Gray)
                        )
                       
                       
                        ShimmerEffect(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .weight(2f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Gray)
                        )

                    }

                    
                }

                is NetWorkResponse.Success -> {

                    val response = response.data
                    if(response.isNotEmpty()){
                        Column(modifier = Modifier.fillMaxWidth().background(color = PrimaryBackgroundColor)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Row (verticalAlignment = Alignment.CenterVertically){
                                    Icon(
                                        painter = painterResource(id = R.drawable.events_icon),
                                        contentDescription = "add announcement",
                                        modifier = Modifier.padding(start = 20.dp).size(20.dp)
                                    )

                                    Text(
                                        text = "Events",
                                        fontSize = 20.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }











                            }

                          }
                    }

                }
                else->{}


            }


          Events(ongoing = ongoingEvents, upcoming = upcomingEvent, homeStat = homePageStats, selected = selected)






            Spacer(modifier= Modifier.height(140.dp))
         }







        }
    }




@Composable

fun upperView(
    modifier: Modifier= Modifier.padding(horizontal = 20.dp)
        .fillMaxWidth()
        .height(200.dp),
    name: String = "Achal",
    chatCnt: Int = 2,
    announcmentCnt: Int = 3,
    taskCnt: Int = 3,
    photo:String?="file:///C:/Users/HP/Downloads/WhatsApp%20Image%202025-03-25%20at%2021.45.05_61ab6874.webp",



) {
    Card(
        modifier = modifier

    ) {

        Box(modifier = Modifier.fillMaxSize()
            .background(color = Color((0xFF0032CD)))
            .padding(start=15.dp),
            
            contentAlignment = Alignment.Center
            ){

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ){


                Column{

                  Row{
                      Text(
                          text = "Hey, ",
                          color = Color.White,
                          fontWeight = FontWeight.W300,
                          fontSize = 20.sp
                       )
                      Text(
                          text = "$name",
                          color = Color.White,
                          fontWeight = FontWeight.W600,
                          fontSize = 20.sp
                      )

                  }
                    Text(
                        text = "Good Morning",
                        color = Color.White,
                        fontWeight = FontWeight.W300,
                        fontSize = 23.sp
                    )



                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier
                            .width(152.dp)
                            .height(37.dp)
                            ,
                        colors = CardDefaults.cardColors(containerColor = Color.White) // White background for the Card
                    ) {
                        Row (modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            badgeBox(R.drawable.chat_icon, chatCnt,"chat"){
                                //navController?.navigate("chat_room")
                            }
                            badgeBox(R.drawable.task_icon, taskCnt,"task"){
                               // navController?.navigate("task")
                            }
                            badgeBox(R.drawable.announcment_icon,announcmentCnt ,"announcement"){}
                        }
                    }




                }

                Card(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(130.dp),
                    shape = CircleShape,
                ) {
                    CompositionLocalProvider(LocalInspectionMode provides false) {
                        if (photo != null) {
                            val imgUrl = photo?.let {
                                if (it.toString().startsWith("http://")) {
                                    it.toString().replace("http://", "https://")
                                } else it
                            } ?: ""

                            Log.d("ROOM_AVATAR", "Final Image URL: $imgUrl")

                            ProfileImage(imgUrl.toString()) {}


                            Log.d("ROOM_AVATAR", "groupListItem: ${imgUrl}")
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile_icon),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }


            }


        }
    }
}


 @Composable
  fun badgeBox(icon:Int,cnt:Int,content: String, onClick:()-> Unit){
      BadgedBox(
          badge = {
              if (cnt > 0) {
                  Badge(
                      containerColor = Color.Red,
                      contentColor = Color.White
                  ) {
                      Text("$cnt")
                  }
              }
          }
      ) {
          Icon(
              painter = painterResource(id = icon),
              contentDescription = content,
              modifier = Modifier.size(25.dp)
                  .clickable(
                      onClick = onClick
                  ),
          )
      }

  }


var announcemnts=listOf(
    announcmentDisplay("hello every one,\n today is meeting, every one must present ", "Task","Rishi","2025-03-19 16:53"),
    announcmentDisplay("hello every one,\n today is meeting, every one must present ", "Task","Rishi","2025-03-19 16:53"),
    announcmentDisplay("hello every one,\n today is meeting, every one must present ", "Task","Rishi","2025-03-19 16:53"),
    announcmentDisplay("hello every one,\n today is meeting, every one must present ", "Task","Rishi","2025-03-19 16:53"),
)


@Composable

fun annuncements(modifier: Modifier = Modifier
    .padding(horizontal = 20.dp)
    .fillMaxWidth()

    .wrapContentHeight()
    .heightIn(max = 400.dp)
    .padding(5.dp),

    announcmentsList:List<announcmentDisplay> =announcemnts){

    val scrollState = rememberLazyListState()

   Card(modifier = modifier.clip(RoundedCornerShape(16.dp)),


    shape = RoundedCornerShape(16.dp), // Add rounded corners

 ) {

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            //.padding(end = 12.dp)
            ,

            state=scrollState) {
            items(announcmentsList){item->

                announcementsItem(announcmentDisplay = item)

            }





    }
}



}



@Composable

fun announcementsItem(
    modifier: Modifier= Modifier.fillMaxWidth()
        .wrapContentHeight()
        .background(color = Color.White)
        .padding(top = 8.dp,  start = 8.dp, end = 8.dp),
    announcmentDisplay: announcmentDisplay= announcmentDisplay("hello every one,\n today is meeting, every one must present ", "Task","Rishi","2025-03-19 16:53"),
   ){

    Column(modifier=modifier){
            Text(
                text="${announcmentDisplay.time_date} ${announcmentDisplay.related_to_display} From: ${announcmentDisplay.sender_first_name}",
                color = Color.DarkGray,
                fontSize = 12.sp
            )

        Spacer(modifier= Modifier.height(10.dp))
        
        Text(
                text="\"${announcmentDisplay.message}\"",
                color = Color.Black,
            fontSize = 16.sp
            )

        Spacer(modifier= Modifier.height(20.dp))

        Divider(modifier= Modifier.padding(horizontal = 5.dp)
            .background(color = Color.Gray))




    }

}



@SuppressLint("SuspiciousIndentation")
@Preview
@Composable
fun AttendanceCard(viewModel: AttendanceViewModel = viewModel()) {
    val attendanceList = viewModel.attendanceList

    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA)) // Light background
    ) {
        val day = "Mon"
        val date = "28"
        val status = "Present"
           LazyRow{
             item{ Days(day,date ,status)}
             item{ Days(day,date ,status)}
             item{ Days(day,date ,status)}
             item{ Days(day,date ,"Abscent")}
             item{ Days(day,date ,status)}
             item{ Days(day,date ,"Abscent")}
             item{ Days(day,date ,status)}

           }

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                /* TODO: Trigger attendance API */
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005EFF))
            ) {
                Text("Mark Attendance", color = Color.White)
            }
        }

}
@Composable
fun Days(
day:String,date:String,status:String
){
    Card(
        modifier = Modifier
            .padding(4.dp).height(80.dp).width(46.dp).border(0.5.dp, Color.Black, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA))
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(day ,color = Color.Black, fontSize = 15.sp, fontWeight= FontWeight.Bold,modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(8.dp))
        Text(date, color = Color.Black , fontSize = 12.sp, fontWeight= FontWeight.Bold,modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = "status",
            tint = if (status == "Present") Color.Green else if(status=="Abscent") Color.Red else Color.Gray,
            modifier = Modifier
                .size(12.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}






var ongoingEvents=listOf(
    EventItem(4,"https://res.cloudinary.com/dcbla9zbl/image/upload/v1742897578/vt2slkifr8cw5v3b5k8t.jpg"),
    EventItem(4,"https://res.cloudinary.com/dcbla9zbl/image/upload/v1742897578/vt2slkifr8cw5v3b5k8t.jpg"),
    EventItem(4,"https://res.cloudinary.com/dcbla9zbl/image/upload/v1742897578/vt2slkifr8cw5v3b5k8t.jpg")
)


@Composable

fun Events(
    modifier: Modifier= Modifier
        .padding(horizontal = 20.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .heightIn(max = 400.dp)
        .padding(5.dp),
    ongoing: State<NetWorkResponse<List<EventItem>>?>,
    upcoming: State<NetWorkResponse<List<EventItem>>?>,
    homeStat:State<NetWorkResponse<HomePageStats>?>,
    selected: MutableState<String?>
){



val context=LocalContext.current












    Card(modifier = modifier
        .clip(RoundedCornerShape(16.dp))
        ,


        shape = RoundedCornerShape(16.dp), // Add rounded corners

    ) {

        Column(
            modifier= Modifier.fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.White)
        ) {

            when(val homeStat= homeStat.value){

                is NetWorkResponse.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load data",
                            color = Color.Red
                        )
                    }
                }
                is NetWorkResponse.Loading->{
                    ShimmerEffect(
                        modifier = Modifier.padding(start = 8.dp,end=8.dp, top = 10.dp )
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(color = Color.Gray)
                            .clip(RoundedCornerShape(5))
                    )
                }
                is NetWorkResponse.Success->{
                    if (homeStat.data.year != "2nd") {
                        Button(
                            modifier = Modifier.padding(start = 8.dp,end=8.dp, top = 10.dp )
                                .fillMaxWidth()
                                .height(40.dp),
                            onClick = {

                                context.let { ctx ->
                                    val intent = Intent(ctx, CreateEventActivity::class.java)
                                    ctx.startActivity(intent)
                                }

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0032CD),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Create Event"
                            )
                        }


                    }
                }
                
                else->{}

            }

            when(val upcoming= upcoming.value){

                is NetWorkResponse.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load data",
                            color = Color.Red
                        )
                    }
                }
                is NetWorkResponse.Loading->{
                    Column(modifier = Modifier.fillMaxWidth().background(color = Color.White)) {
                        Row {
                            ShimmerEffect(
                                modifier = Modifier.padding(14.dp)
                                    .width(70.dp)
                                    .height(30.dp)

                                    .clip(RoundedCornerShape(5))
                                    .background(color = Color.Gray)
                            )
                            ShimmerEffect(
                                modifier = Modifier.padding(14.dp)
                                    .width(30.dp)
                                    .height(30.dp)

                                    .clip(RoundedCornerShape(5))
                                    .background(color = Color.Gray)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                        LazyRow(
                            modifier = Modifier.background(color = Color.Transparent)
                                .fillMaxWidth()

                        ) {
                            items(4) {


                                   ShimmerEffect(
                                       modifier = Modifier
                                           .width(200.dp)
                                           .height(120.dp)
                                                                                      .padding(10.dp)
                                           .clip(RoundedCornerShape(16))
                                           .background(color = Color.Gray)

                                   )



                            }
                        }



                    }
                }
                is NetWorkResponse.Success->{
                    if (upcoming.data.isNotEmpty()) {

                        Column(modifier = Modifier.fillMaxWidth().background(color = Color.White)) {
                            Row {
                                Text(
                                    text = "Upcoming Event",
                                    fontSize = 15.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(14.dp)
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
                                items(upcoming.data) { item ->

                                    EventItem(item,selected)

                                }
                            }



                        }


                    }
                }

                else->{}

            }

            when(val ongoing= ongoing.value){

                is NetWorkResponse.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load data",
                            color = Color.Red
                        )
                    }
                }
                is NetWorkResponse.Loading->{
                    Column(modifier = Modifier.fillMaxWidth().background(color = Color.White)) {
                        Row {
                            ShimmerEffect(
                                modifier = Modifier.padding(14.dp)
                                    .width(70.dp)
                                    .height(30.dp)

                                    .clip(RoundedCornerShape(5))
                                    .background(color = Color.Gray)
                            )
                            ShimmerEffect(
                                modifier = Modifier.padding(14.dp)
                                    .width(30.dp)
                                    .height(30.dp)

                                    .clip(RoundedCornerShape(5))
                                    .background(color = Color.Gray)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                        LazyRow(
                            modifier = Modifier.background(color = Color.Transparent)
                                .fillMaxWidth()

                        ) {
                            items(4) {


                                ShimmerEffect(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(120.dp)
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(16))
                                        .background(color = Color.Gray)

                                )



                            }
                        }



                    }
                }
                is NetWorkResponse.Success->{
                    if(ongoing.data.isNotEmpty()){

                        Column(modifier = Modifier.fillMaxWidth().background(color = Color.White)) {
                            Row {
                                Text(
                                    text = "Ongoing Event",
                                    fontSize = 15.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(14.dp)
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
                                items(ongoing.data) { item ->

                                    EventItem(item,selected)

                                }
                            }



                        }




                    }
                }

                else->{}

            }









            








        }


    }




}

@Composable

fun EventItem(
    event: EventItem=EventItem(4,"http://res.cloudinary.com/dcbla9zbl/image/upload/v1742897578/vt2slkifr8cw5v3b5k8t.jpg"),
    selected: MutableState<String?>
){


    val imgUrl = event.poster?.let {
        if (it.toString().startsWith("http://")) {
            it.toString().replace("http://", "https://")
        } else it
    } ?: ""

    Log.d("ROOM_AVATAR", "Final Image URL: $imgUrl")







    Card (
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(16))
            ,
        shape = RoundedCornerShape(16)

    ){
        AsyncImage(
            model = imgUrl,
            contentDescription = "Group Profile Picture",
            modifier = Modifier

                .clickable {
                    selected.value=imgUrl.toString()
                },
            contentScale = ContentScale.Crop
        )
    }

}