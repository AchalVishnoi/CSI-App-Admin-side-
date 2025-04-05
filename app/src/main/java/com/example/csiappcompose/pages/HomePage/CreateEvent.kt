package com.example.csiappcompose.pages.HomePage

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Switch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.csiappcompose.R
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import com.example.csiappcompose.viewModels.HomePageViewModel
import com.example.csiappcompose.viewModels.HomePageViewModelFactory
import com.google.ai.client.generativeai.type.content
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Calendar



@Preview
@Composable
fun CreateEvent(){

    var context=LocalContext.current
    val viewModel: HomePageViewModel = viewModel(factory = HomePageViewModelFactory(context))


    val name = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val guidelines = remember { mutableStateOf("") }
    val venue = remember { mutableStateOf("") }
    var selectedPosterUri = remember { mutableStateOf<Uri?>(null) }
    val registrationStartDate = remember { mutableStateOf("") }
    val registrationEndDate = remember { mutableStateOf("") }
    val eventDate = remember { mutableStateOf("") }
    val isRegistrationsOpen = remember { mutableStateOf(false) }
    val comingSoon = remember { mutableStateOf(false) }
    val paymentRequired = remember { mutableStateOf(false) }
    val amount: MutableState<Int> = remember { mutableStateOf(0) }
    var amountText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier.padding(start = 20.dp).size(20.dp)
                )
            }

            Text(
                text = "Create Event",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }


        inputField(name, "Event Name")
        inputField(title, "Title")
        inputField(description, "Description")
        inputField(guidelines, "Guidelines")
        inputField(venue, "Venue")
        ImageUploadScreen(selectedImageUri = selectedPosterUri) {

        }




Row{

    Card(
        modifier = Modifier.weight(1f)
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF7F7F7))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (eventDate.value.isEmpty()) "Start Date" else eventDate.value,
                fontSize = 16.sp,
                color = if (eventDate.value.isEmpty()) Color.Gray else Color.Black
            )
            DatePickerField( eventDate, Modifier.weight(1f))


        }
    }


    Card(
        modifier = Modifier.weight(1f)
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF7F7F7))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (registrationEndDate.value.isEmpty()) "End Date" else registrationEndDate.value,
                fontSize = 16.sp,
                color = if (registrationEndDate.value.isEmpty()) Color.Gray else Color.Black
            )

            DatePickerField(registrationEndDate,Modifier.weight(1f))


        }
    }

}



        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "Event Status",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }




        Card(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 4.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {


            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF7F7F7))
                .padding(12.dp)){

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFF7F7F7))
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Coming Soon",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Switch(
                        checked = comingSoon.value,
                        onCheckedChange = { comingSoon.value = it }
                    )

                }

                if(comingSoon.value){

                    var date =remember { mutableStateOf("") }
                    Divider(modifier = Modifier.padding(12.dp).fillMaxWidth(), color = Color.LightGray)

                    Row(verticalAlignment = Alignment.CenterVertically){
                        DatePickerField(date, Modifier.padding(end=5.dp).size(20.dp))
                        Text(
                            text = if (date.value.isEmpty()) "Due Date" else date.value,
                            fontSize = 16.sp,
                            color = if (date.value.isEmpty()) Color.Gray else Color.Black
                        )

                    }





                }




            }



        }



        Card(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 4.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {


Column(modifier = Modifier
    .fillMaxWidth()
    .background(color = Color(0xFFF7F7F7))
    .padding(12.dp)){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF7F7F7))
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Registration Open",
            fontSize = 16.sp,
            color = Color.Black
        )

        Switch(
            checked = isRegistrationsOpen.value,
            onCheckedChange = { isRegistrationsOpen.value = it }
        )

    }

    if(isRegistrationsOpen.value){


        Divider(modifier = Modifier.padding(12.dp).fillMaxWidth(), color = Color.LightGray)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First DatePicker (Registration Start Date)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerField(
                    registrationStartDate,
                    Modifier
                        .padding(end = 8.dp)
                        .size(20.dp)
                )
                Text(
                    text = if (registrationStartDate.value.isEmpty()) "Open Date" else registrationStartDate.value,
                    fontSize = 16.sp,
                    color = if (registrationStartDate.value.isEmpty()) Color.Gray else Color.Black,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }


            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(horizontal = 8.dp),
                color = Color.LightGray
            )

            // Second DatePicker (Registration End Date)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerField(
                    registrationEndDate,
                    Modifier
                        .padding(end = 8.dp)
                        .size(20.dp)
                )
                Text(
                    text = if (registrationEndDate.value.isEmpty()) "Close Date" else registrationEndDate.value,
                    fontSize = 16.sp,
                    color = if (registrationEndDate.value.isEmpty()) Color.Gray else Color.Black
                )
            }
        }








    }




            }



        }




        //payment

        Card(
            modifier = Modifier
                .padding(horizontal = 17.dp, vertical = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {


            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF7F7F7))
                .padding(12.dp)){

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFF7F7F7))
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Payment Required",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Switch(
                        checked = paymentRequired.value,
                        onCheckedChange = { paymentRequired.value = it }
                    )

                }

                if(paymentRequired.value){


                    Divider(modifier = Modifier.padding(12.dp).fillMaxWidth(), color = Color.LightGray)



                    TextField(
                        value = amountText.value,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                amountText.value = input

                                amount.value = input.toIntOrNull() ?: 0 // Safely convert to Int
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 40.dp, max = 120.dp),
                        placeholder = { Text("Amount") },
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray
                        )
                    )



                    }








                }

            Button(
                onClick = {


                    if (name.value.isBlank() || title.value.isBlank() || description.value.isBlank() ||
                        guidelines.value.isBlank() || venue.value.isBlank() || registrationStartDate.value.isBlank() ||
                        registrationEndDate.value.isBlank() || eventDate.value.isBlank()
                    ) {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }




                    val posterPart = selectedPosterUri.value?.let { uri ->
                        try {
                            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                val posterRequestBody = inputStream.readBytes()
                                    .toRequestBody("image/*".toMediaTypeOrNull())
                                MultipartBody.Part.createFormData("poster", "poster.jpg", posterRequestBody)
                            }
                        } catch (e: Exception) {

                            e.printStackTrace()
                            null
                        }
                    }

                    viewModel.submitEvent(

                        title = name.value,
                        description = description.value,
                        guidelines = guidelines.value,
                        venue = venue.value,
                        registrationStartDate = registrationStartDate.value,
                        registrationEndDate = registrationEndDate.value,
                        eventDate = eventDate.value,
                        status = "ongoing",
                        isRegistrationsOpen = isRegistrationsOpen.value,
                        paymentRequired = paymentRequired.value,
                        amount =  amount.value.toInt(),
                        poster = posterPart,
                       gallery = null
                    )

                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Create Event",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }


            }








        }













@Composable
fun inputField(


    message: MutableState<String>,
    placeholder: String = "Enter text",
    error: String? = null,
    modifier: Modifier= Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            ,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = Color(0xFFF7F7F7))
                .padding(2.dp)
        ) {

            TextField(
                value = message.value,
                onValueChange = { message.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp, max = 120.dp),

                placeholder = { Text(placeholder) },
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

            )


            if (error != null) {
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}




@SuppressLint("SuspiciousIndentation")
@Composable
fun DatePickerField(date: MutableState<String>,modifier: Modifier= Modifier) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date.value = "${dayOfMonth}/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis
    // UI for showing the selected date


            Icon(
                painter = painterResource(id = R.drawable.date_range),
                contentDescription = "collender",
                modifier = modifier.clickable { datePickerDialog.show() }
            )
        }



@Composable

fun posterPicker(selectedImageUri: MutableState<Uri?>,
                 onImageSelected: (Uri) -> Unit,){
       ImageUploadScreen(selectedImageUri=selectedImageUri) { onImageSelected  }


}

@Composable
fun ImageUploadScreen(
    placeholder: String = "Upload Poster",
    error: String? = null,
    modifier: Modifier = Modifier,
    selectedImageUri: MutableState<Uri?>,
    onImageSelected: (Uri) -> Unit,
) {
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
        uri?.let { onImageSelected(it) }
    }

    Card(
        modifier = modifier
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {
            getContent.launch("image/*")
        },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color(0xFFF7F7F7))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row for icon and placeholder text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.vector__3_),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = selectedImageUri.value?.let { "Poster Selected Preview" }
                        ?: placeholder,
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Image preview card
            selectedImageUri.value?.let { uri ->
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Selected Poster",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }


            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}






