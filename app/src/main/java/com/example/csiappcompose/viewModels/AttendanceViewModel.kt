import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.http.GET

class AttendanceViewModel : ViewModel() {

    var attendanceList by mutableStateOf<List<AttendanceDay>>(emptyList())
        private set

    init {
        fetchAttendance()
    }

    private fun fetchAttendance() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getAttendance()
                attendanceList = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



data class AttendanceDay(
    val day: String,     // e.g., "Mon", "Tue"
    val date: String,    // e.g., "21", "22"
    val status: String   // e.g., "present", "absent", "today", "none"
)

