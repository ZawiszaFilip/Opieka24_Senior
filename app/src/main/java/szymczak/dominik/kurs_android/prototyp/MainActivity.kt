/**
 * Główny pakiet aplikacji prototypowej dla kursu Android.
 */
package szymczak.dominik.kurs_android.prototyp

// ########## SEKCJA IMPORTÓW (Kompletna i Zdeduplikowana) ##########
import android.Manifest
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import java.util.UUID
import kotlin.math.sqrt



// ########## SEKCJA MOTYWU (Theme) ##########

val PrimaryPurple = Color(0xFF6A1B9A)
val SecondaryPink = Color(0xFFEC407A)
val AccentOrange = Color(0xFFFFAB40)
val SosRed = Color(0xFFD32F2F)
val HighContrastNeonGreen = Color(0xFF39FF14)
val HighContrastYellow = Color(0xFFFFEB3B)
val MyCustomBlue = Color(0xFF2196F3)
val AccentGreen = Color(0xFF4CAF50)
val AccentBlue = Color(0xFF03A9F4)
val AdminPanelColor = Color(0xFF00695C)
val AppGradient = Brush.horizontalGradient(colors = listOf(PrimaryPurple, SecondaryPink))

val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        PrimaryPurple.copy(alpha = 0.1f),
        SecondaryPink.copy(alpha = 0.1f),
        Color.Transparent
    )
)

private val DefaultLightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    secondary = SecondaryPink,
    tertiary = AccentOrange,
    tertiaryContainer = MyCustomBlue,
    error = SosRed,
)

private val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastNeonGreen,
    onPrimary = Color.Black,
    secondary = HighContrastNeonGreen,
    onSecondary = Color.Black,
    tertiary = HighContrastNeonGreen,
    onTertiary = Color.Black,
    tertiaryContainer = HighContrastNeonGreen,
    onTertiaryContainer = Color.Black,
    background = Color.Black,
    onBackground = HighContrastYellow,
    surface = Color.Black,
    onSurface = HighContrastYellow,
    surfaceVariant = Color.Black,
    onSurfaceVariant = HighContrastYellow.copy(alpha = 0.8f),
    outline = HighContrastNeonGreen,
    error = AccentOrange,
    onError = Color.Black
)

/**
 * Główny kompozyt motywu aplikacji, obsługujący dynamiczne skalowanie czcionek
 * oraz tryb wysokiego kontrastu.
 *
 * @param fontSizeScale Mnożnik skalowania rozmiaru czcionki (domyślnie 1.0f).
 * @param isHighContrast Przełącznik trybu wysokiego kontrastu (domyślnie false).
 * @param content Treść @Composable do wyświetlenia w ramach motywu.
 */
@Composable
fun AppTheme(
    fontSizeScale: Float = 1.0f,
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (isHighContrast) {
        HighContrastColorScheme
    } else {
        DefaultLightColorScheme
    }

    val defaultTypography = MaterialTheme.typography
    // Skalowanie typografii na podstawie ustawień użytkownika
    val scaledTypography = defaultTypography.copy(
        displayLarge = defaultTypography.displayLarge.copy(fontSize = defaultTypography.displayLarge.fontSize * fontSizeScale),
        displayMedium = defaultTypography.displayMedium.copy(fontSize = defaultTypography.displayMedium.fontSize * fontSizeScale),
        displaySmall = defaultTypography.displaySmall.copy(fontSize = defaultTypography.displaySmall.fontSize * fontSizeScale),
        headlineLarge = defaultTypography.headlineLarge.copy(fontSize = defaultTypography.headlineLarge.fontSize * fontSizeScale),
        headlineMedium = defaultTypography.headlineMedium.copy(fontSize = defaultTypography.headlineMedium.fontSize * fontSizeScale),
        headlineSmall = defaultTypography.headlineSmall.copy(fontSize = defaultTypography.headlineSmall.fontSize * fontSizeScale),
        titleLarge = defaultTypography.titleLarge.copy(fontSize = defaultTypography.titleLarge.fontSize * fontSizeScale),
        titleMedium = defaultTypography.titleMedium.copy(fontSize = defaultTypography.titleMedium.fontSize * fontSizeScale),
        titleSmall = defaultTypography.titleSmall.copy(fontSize = defaultTypography.titleSmall.fontSize * fontSizeScale),
        bodyLarge = defaultTypography.bodyLarge.copy(fontSize = defaultTypography.bodyLarge.fontSize * fontSizeScale),
        bodyMedium = defaultTypography.bodyMedium.copy(fontSize = defaultTypography.bodyMedium.fontSize * fontSizeScale),
        bodySmall = defaultTypography.bodySmall.copy(fontSize = defaultTypography.bodySmall.fontSize * fontSizeScale),
        labelLarge = defaultTypography.labelLarge.copy(fontSize = defaultTypography.labelLarge.fontSize * fontSizeScale),
        labelMedium = defaultTypography.labelMedium.copy(fontSize = defaultTypography.labelMedium.fontSize * fontSizeScale),
        labelSmall = defaultTypography.labelSmall.copy(fontSize = defaultTypography.labelSmall.fontSize * fontSizeScale)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = scaledTypography
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}
// ########## KONIEC SEKCJI MOTYWU ##########


// ########## GŁÓWNA AKTYWNOŚĆ (MainActivity) ##########

/**
 * Główna Aktywność aplikacji, będąca punktem wejścia.
 * Odpowiada za ustawienie widoku, inicjalizację motywu [AppTheme],
 * utworzenie kanału powiadomień oraz inicjalizację głównych ViewModeli.
 */
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Umożliwia rysowanie treści pod paskami systemowymi (np. paskiem statusu)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Tworzy kanał powiadomień przy starcie aplikacji
        createNotificationChannel()

        setContent {
            // Inicjalizacja ViewModelu ustawień (zależnego od repozytorium)
            val settingsRepo = remember { SettingsRepository(applicationContext) }
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.SettingsViewModelFactory(settingsRepo)
            )

            // Obserwacja ustawień z ViewModelu
            val fontSizeScale by settingsViewModel.fontSizeScale.collectAsState()
            val isHighContrast by settingsViewModel.isHighContrast.collectAsState()

            // Zastosowanie motywu z dynamicznymi ustawieniami
            AppTheme(fontSizeScale = fontSizeScale, isHighContrast = isHighContrast) {
                // Inicjalizacja głównych ViewModeli (współdzielonych w aplikacji)
                val authViewModel: AuthViewModel = viewModel()
                val userProfileViewModel: UserProfileViewModel = viewModel(
                    factory = UserProfileViewModel.Factory(authViewModel)
                )
                val helpRequestViewModel: HelpRequestViewModel = viewModel(
                    factory = HelpRequestViewModel.HelpRequestViewModelFactory(authViewModel)
                )

                // Inicjalizacja pozostałych ViewModeli dla konkretnych funkcji
                val calendarViewModel: SeniorCalendarViewModel = viewModel(
                    factory = SeniorCalendarViewModel.Factory(application, authViewModel)
                )

                val fallViewModel = viewModel<FallDetectorViewModel>()

                // === MODYFIKACJA (Zadanie 3: Firebase Seniorm2) ===
                // Inicjalizacja Seniorm2ViewModel z fabryką, aby wstrzyknąć AuthViewModel
                // Poprawna, oryginalna inicjalizacja
                val seniorm2ViewModel = viewModel<Seniorm2ViewModel>()
                // Uruchomienie głównego nawigatora aplikacji
                AppNavigator(
                    calendarViewModel = calendarViewModel,
                    fallDetectorViewModel = fallViewModel,
                    helpRequestViewModel = helpRequestViewModel,
                    seniorm2ViewModel = seniorm2ViewModel,
                    settingsViewModel = settingsViewModel,
                    authViewModel = authViewModel,
                    userProfileViewModel = userProfileViewModel
                )
            }
        }
    }

    /**
     * Tworzy kanał powiadomień wymagany dla Androida 8.0 (Oreo) i nowszych.
     * Kanał ten jest używany do wysyłania przypomnień z kalendarza i modułu Seniorm1.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // === MODYFIKACJA (Zadanie 4: Poprawka nazewnictwa) ===
            val name = "Przypomnienia (Kalendarz i Zadania Cykliczne)"
            val descriptionText = "Kanał dla przypomnień o zadaniach i zadaniach cyklicznych"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("SENIOR_APP_CHANNEL", name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 500, 500)
            }
            // Rejestracja kanału w systemie
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}


// ########## NAWIGACJA I MODELE DANYCH ##########

/**
 * Obiekt przechowujący stałe trasy nawigacji (route) używane w [NavHost].
 */
object AppRoutes {
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Ekrany powitalne dla ról
    const val SENIOR_WELCOME = "senior_welcome"
    const val WOLONTARIUSZ_WELCOME = "wolontariusz_welcome"
    const val ADMIN_WELCOME = "admin_welcome"

    // Trasy funkcyjne
    const val SENIOR_DASHBOARD = "senior_dashboard" // Ekran "Poproś o pomoc"
    const val HELPER_DASHBOARD = "helper_dashboard" // Lista publicznych zleceń
    const val SENIOR_CALENDAR = "senior_calendar"
    const val SOS_EMERGENCY = "sos_emergency"
    const val ASSISTANT = "assistant"
    const val SENIORM2_ITEMS = "seniorm2_items"
    const val SETTINGS = "settings"
    const val PROFILE = "profile" // Jeden ekran profilu dla wszystkich ról

    // Trasa dla szczegółów zlecenia publicznego (z argumentem)
    const val ZLECENIE_DETAILS_ROUTE = "zlecenie_details"
    const val ZLECENIE_DETAILS_ARG = "zlecenieId"
    val ZLECENIE_DETAILS = "$ZLECENIE_DETAILS_ROUTE/{$ZLECENIE_DETAILS_ARG}"

    // Trasy "Moje Zlecenia"
    const val SENIOR_MOJE_ZLECENIA = "senior_moje_zlecenia"
    const val WOLONTARIUSZ_MOJE_ZLECENIA = "wolontariusz_moje_zlecenia"

    // Trasy Admina
    const val ADMIN_MANAGE_USERS_ROUTE = "admin_manage_users"
    const val ADMIN_MANAGE_USERS_ARG = "userRole"
    val ADMIN_MANAGE_USERS = "$ADMIN_MANAGE_USERS_ROUTE/{$ADMIN_MANAGE_USERS_ARG}"
    const val ADMIN_WSZYSTKIE_ZLECENIA = "admin_wszystkie_zlecenia"

    // === MODYFIKACJA (Zadanie 1: Archiwum Admina) ===
    const val ADMIN_ARCHIWUM = "admin_archiwum"
}

/**
 * Model danych dla przykładowej usługi (używany w UI).
 */
data class Service(val title: String, val icon: ImageVector)
val mockServices = listOf(
    Service("Zakupy", Icons.Default.ShoppingCart),
    Service("Sprzątanie", Icons.Default.CleaningServices),
    Service("Transport", Icons.Default.MedicalServices),
    Service("Wyprowadzenie psa", Icons.Default.Pets),
    Service("Drobne naprawy", Icons.Default.Build)
)

/**
 * Model danych reprezentujący wydarzenie w kalendarzu.
 * @param firebaseDocId ID dokumentu z Firestore (używane do usuwania).
 * @param time Godzina wydarzenia (format "HH:mm").
 * @param title Tytuł/opis wydarzenia.
 */
data class CalendarEvent(val firebaseDocId: String, val time: String, val title: String)

/**
 * Model przechowujący wynik parsowania odpowiedzi od AI.
 * @param date Sparsowana data.
 * @param time Sparsowany czas (format "HH:mm").
 * @param taskTitle Sparsowany tytuł zadania.
 */
data class ParsedResult(val date: LocalDate, val time: String, val taskTitle: String)

/**
 * Sealed class reprezentująca odpowiedź od Asystenta AI.
 */
@RequiresApi(Build.VERSION_CODES.O)
sealed class AiResponse {
    /** Udana odpowiedź, zawierająca sparsowane dane. */
    data class Success(val result: ParsedResult) : AiResponse()
    /** Błąd lub wiadomość zwrotna od AI (np. prośba o doprecyzowanie). */
    data class Failure(val message: String) : AiResponse()
}

/**
 * Model danych dla przedmiotu/zadania w module Seniorm2 (np. przypomnienie o lekach).
 *
 * === MODYFIKACJA (Zadanie 3: Firebase Seniorm2) ===
 * ID jest teraz pobierane z Firestore, a nie generowane w aplikacji.
 *
 * @param id Unikalny identyfikator (ID dokumentu Firestore).
 * @param name Nazwa zadania/leku.
 * @param time Godzina przypomnienia (format "HH:mm").
 * @param daysOfWeek Zbiór dni tygodnia, w których przypomnienie ma być aktywne.
 */
@RequiresApi(Build.VERSION_CODES.O)
data class Seniorm2Item(
    val id: String = UUID.randomUUID().toString(), // ID jest generowane automatycznie
    val name: String,
    val time: String, // "HH:mm"
    val daysOfWeek: Set<DayOfWeek> // Zbiór dni tygodnia
)

// --- Modele Zleceń (Help Requests) ---

/**
 * Model referencyjny zlecenia (używany w kolekcjach użytkowników).
 */
data class ZlecenieRef(
    val id: String,
    val seniorId: String?,
    val wolontariuszId: String?,
    val opis: String?,
    /** Status zlecenia: "Wolne", "Aktywne", "DoPotwierdzenia", "Zakonczone". */
    val status: String?
)

/**
 * Pełny model zlecenia widoczny dla wolontariusza na liście publicznej.
 */
data class PelneZlecenie(
    val zlecenieId: String,
    val seniorImieNazwisko: String,
    val seniorAdres: String,
    val opisZlecenia: String,
    /** Status zlecenia: "Wolne", "Aktywne", "DoPotwierdzenia", "Zakonczone". */
    val status: String,
    val seniorId: String
)

/**
 * Pełny model zlecenia widoczny dla Seniora na liście "Moje Zlecenia".
 */
data class PelneZlecenieDlaSeniora(
    val zlecenieId: String,
    val opis: String,
    /** Status zlecenia: "Wolne", "Aktywne", "DoPotwierdzenia", "Zakonczone". */
    val status: String,
    val wolontariuszImieNazwisko: String,
    val wolontariuszTelefon: String
)

/**
 * Pełny model zlecenia widoczny dla Wolontariusza na liście "Moje Zlecenia".
 */
data class PelneZlecenieDlaWolontariusza(
    val zlecenieId: String,
    val opis: String,
    val seniorImieNazwisko: String,
    val seniorAdres: String,
    val seniorTelefon: String,
    /** Status zlecenia: "Wolne", "Aktywne", "DoPotwierdzenia", "Zakonczone". */
    val status: String
)
// ########## KONIEC MODELI DANYCH ##########


// ########## LOGIKA ASYSTENTA AI (Gemini) ##########

// 🔴 === MODYFIKACJA (Zadanie 4: KRYTYCZNA POPRAWKA BEZPIECZEŃSTWA) ===
//
// Usunięto klucz API z kodu.
// Pamiętaj, aby dodać klucz do `local.properties` i zaktualizować `build.gradle.kts`
// (zgodnie z instrukcją na początku odpowiedzi).
//
private val generativeModel = GenerativeModel(
    modelName = "gemini-2.5-flash",
    apiKey = BuildConfig.GEMINI_API_KEY, // Używamy klucza z BuildConfig
    generationConfig = generationConfig {
        responseMimeType = "application/json"
    }
)

/**
 * Przetwarza tekst wprowadzony przez użytkownika za pomocą modelu Gemini AI.
 * Oczekuje odpowiedzi JSON zgodnej ze zdefiniowanym [systemInstruction].
 *
 * @param textInput Surowy tekst od użytkownika.
 * @return [AiResponse] - [AiResponse.Success] z danymi lub [AiResponse.Failure] z wiadomością.
 */
@RequiresApi(Build.VERSION_CODES.O)
private suspend fun processInputWithGemini(textInput: String): AiResponse {
    val currentDate = LocalDate.now().toString()
    val currentDayOfWeek = LocalDate.now().dayOfWeek.toString()

    // Instrukcja systemowa dla AI, definiująca format odpowiedzi JSON i zasady logiki.
    val systemInstruction = """
        Jesteś Asystentem Seniora, ekspertem w planowaniu zadań i walidacji danych dla polskiej aplikacji.
        Twoja odpowiedź to always JSON. MUSISZ wypełnić pola "title", "date" (YYYY-MM-DD) i "time" (HH:mm).

        Dzisiejsza data: $currentDate ($currentDayOfWeek).
        
        ## 1. ZASADY SUKCESU
        Użyj {"status": "success", ...} TYLKO wtedy, gdy masz wszystkie 3 elementy:
        - **Tytuł (Title):** Zwięzły opis zadania.
        - **Data (Date):** Poprawna data w formacie YYYY-MM-DD.
        - **Czas (Time):** Poprawna godzina w formacie HH:mm.

        ## 2. ZASADY BŁĘDU (MUSISZ UŻYĆ {"status": "failure", ...} jeśli):

        A. BRAK DANYCH (BĄDŹ UPRZEJMY I ZADRĘCZ O BRAKUJĄCE INFO):
           1. **Brak Czasu:** Jeśli podana jest data, ale brakuje godziny, zapytaj: "Rozumiem. O której godzinie mam to zaplanować?".
           2. **Brak Daty:** Jeśli podana jest godzina, ale brakuje daty, zapytaj: "Na który dzień (datę) mam to zaplanować?".
           3. **Brak Tytułu:** Jeśli brakuje kontekstu, zapytaj: "Co konkretnie mam zaplanować i na kiedy?".

        B. DANE NIEPOPRAWNE (BĄDŹ PRECYZYJNY I WSKAŻ BŁĄD):
           1. **Niepoprawna Godzina:** Godziny > 23 lub Minuty > 59. Zgłoś: "Przepraszam, to niepoprawny format/zakres godziny. Czy możesz podać właściwy?".
           2. **Niepoprawna Data:** np. 32 marca, 30 lutego (poza przestępnym). Zgłoś: "Podana data jest niepoprawna. Czy możesz ją zweryfikować?".

        C. INNA PROŚBA (PRZYPOMNIJ O FUNKCJI):
           - Prośby niezwiązane z planowaniem (np. pytania o pogodę, definicje, opinie). Odpowiedz: "Jestem asystentem do planowania zadań. Czy chciałbyś coś zaplanować, np. Zakupy, Transport lub wizytę u lekarza?".
        
        ## 3. WAŻNE WSKAZÓWKI DLA AI:
        - **Precyzja Czasu:** Konwertuj "9 rano" na "09:00", "północ" na "00:00", "południe" na "12:00". Zawsze używaj formatu 24h (HH:mm).
        - **Precyzja Tytułu:** Automatycznie twórz tytuł z fraz kluczowych, np. zidentyfikuj "Zakupy", "Sprzątanie", "Lekarz", "Transport", "Wyprowadzenie psa" jako główne tytuły.
    """.trimIndent()

    try {
        val fullPrompt = "$systemInstruction\n\nUżytkownik: \"$textInput\"\nTy:"
        val response = generativeModel.generateContent(fullPrompt)
        val jsonText = response.text ?: throw Exception("Pusta odpowiedź od AI")

        Log.d("GeminiResponse", jsonText)

        val jsonObject = JSONObject(jsonText)

        // Parsowanie odpowiedzi JSON
        return when (jsonObject.getString("status")) {
            "success" -> {
                val title = jsonObject.getString("title")
                val time = jsonObject.getString("time")
                val date = LocalDate.parse(jsonObject.getString("date"))
                AiResponse.Success(ParsedResult(date = date, time = time, taskTitle = title))
            }
            "failure" -> {
                val message = jsonObject.getString("message")
                AiResponse.Failure(message)
            }
            else -> {
                throw Exception("AI zwróciło nieoczekiwany status.")
            }
        }

    } catch (e: Exception) {
        Log.e("GeminiError", "Błąd podczas przetwarzania AI: ${e.message}", e)
        return AiResponse.Failure("Wystąpił błąd podczas przetwarzania: ${e.message}")
    }
}
// ########## KONIEC LOGIKI AI ##########


// ########## VIEWMODELE ##########

/**
 * Reprezentuje stan procesu uwierzytelniania.
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class LoginSuccess(val role: String, val userId: String) : AuthState()
    data class LoginError(val message: String) : AuthState()
}

/**
 * Reprezentuje stan operacji wykonywanych przez administratora.
 */
sealed class AdminUiState {
    object Idle : AdminUiState()
    object Loading : AdminUiState()
    data class Success(val message: String) : AdminUiState()
    data class Error(val message: String) : AdminUiState()
}

/**
 * ViewModel zarządzający logiką uwierzytelniania (logowanie, wylogowywanie)
 * oraz stanem zalogowanego użytkownika (ID, rola).
 * Zawiera również logikę dla panelu administratora (dodawanie, usuwanie użytkowników).
 */
class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    private val _currentUserRole = MutableStateFlow<String?>(null)
    val currentUserRole: StateFlow<String?> = _currentUserRole.asStateFlow()

    private val _adminUiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val adminUiState: StateFlow<AdminUiState> = _adminUiState.asStateFlow()

    private val db = Firebase.firestore

    /**
     * Loguje użytkownika na podstawie numeru telefonu i hasła.
     * Przeszukuje kolekcje "Senior", "Wolontariusz" i "Admin".
     */
    fun loginUser(phone: String, pass: String) {
        if (phone.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.LoginError("Numer telefonu i hasło nie mogą być puste.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Szuka użytkownika w zdefiniowanych kolekcjach
                val (role, document) = findUserInCollections(phone, listOf("Senior", "Wolontariusz", "Admin"))

                if (document == null) {
                    _authState.value = AuthState.LoginError("Nie znaleziono użytkownika o tym numerze telefonu.")
                } else {
                    val correctPassword = document.getString("Haslo")

                    if (correctPassword == pass) {
                        val userId = document.id
                        _currentUserId.value = userId
                        _currentUserRole.value = role
                        _authState.value = AuthState.LoginSuccess(role!!, userId)
                    } else {
                        _authState.value = AuthState.LoginError("Niepoprawne hasło.")
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Błąd logowania: ${e.message}", e)
                _authState.value = AuthState.LoginError("Wystąpił błąd: ${e.message}")
            }
        }
    }

    /**
     * Prywatna funkcja pomocnicza do przeszukiwania wielu kolekcji Firestore
     * w poszukiwaniu użytkownika o danym numerze telefonu.
     */
    private suspend fun findUserInCollections(phone: String, collections: List<String>): Pair<String?, DocumentSnapshot?> {
        for (collectionName in collections) {
            val querySnapshot = db.collection(collectionName)
                .whereEqualTo("Telefon", phone)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                return Pair(collectionName, querySnapshot.documents.first())
            }
        }
        return Pair(null, null)
    }

    /**
     * Resetuje stan uwierzytelniania (np. po błędzie logowania).
     */
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    /**
     * Wylogowuje użytkownika, czyszcząc jego ID i rolę.
     */
    fun logout() {
        _currentUserId.value = null
        _currentUserRole.value = null
        _authState.value = AuthState.Idle
    }

    /**
     * Resetuje stan UI panelu administratora (np. po wyświetleniu komunikatu).
     */
    fun resetAdminState() {
        _adminUiState.value = AdminUiState.Idle
    }

    /**
     * [ADMIN] Dodaje nowego użytkownika (Seniora lub Wolontariusza) do bazy danych.
     * Sprawdza, czy numer telefonu nie jest już zajęty.
     */
    fun addUser(imie: String, nazwisko: String, telefon: String, adres: String, haslo: String, rola: String) {
        _adminUiState.value = AdminUiState.Loading
        viewModelScope.launch {
            try {
                // Sprawdzenie, czy użytkownik już istnieje
                val (existingRole, _) = findUserInCollections(telefon, listOf("Senior", "Wolontariusz", "Admin"))
                if (existingRole != null) {
                    _adminUiState.value = AdminUiState.Error("Ten numer telefonu jest już zarejestrowany jako $existingRole.")
                    return@launch
                }

                val newUser = mutableMapOf(
                    "Imie" to imie,
                    "Nazwisko" to nazwisko,
                    "Telefon" to telefon,
                    "Adres" to adres,
                    "Haslo" to haslo,
                    "Login" to telefon // Używamy telefonu jako loginu
                )

                // Seniorzy mają dodatkowe pole na numer opiekuna
                if (rola == "Senior") {
                    newUser["OpiekunNumer"] = ""
                }

                val collectionName = if (rola == "Senior") "Senior" else "Wolontariusz"
                db.collection(collectionName).add(newUser).await()

                _adminUiState.value = AdminUiState.Success("$rola $imie $nazwisko został pomyślnie dodany.")

            } catch (e: Exception) {
                _adminUiState.value = AdminUiState.Error("Błąd dodawania: ${e.message}")
            }
        }
    }

    /**
     * [ADMIN] Usuwa użytkownika na podstawie jego danych.
     * Przeprowadza również czyszczenie powiązanych z nim zleceń.
     */
    fun removeUserWithCleanup(imie: String, nazwisko: String, telefon: String, rola: String) {
        _adminUiState.value = AdminUiState.Loading
        viewModelScope.launch {
            try {
                val collectionName = if (rola == "Senior") "Senior" else "Wolontariusz"

                // Znalezienie dokumentu użytkownika
                val userQuery = db.collection(collectionName)
                    .whereEqualTo("Imie", imie)
                    .whereEqualTo("Nazwisko", nazwisko)
                    .whereEqualTo("Telefon", telefon)
                    .limit(1)
                    .get()
                    .await()

                if (userQuery.isEmpty) {
                    _adminUiState.value = AdminUiState.Error("Nie znaleziono $rola o podanych danych.")
                    return@launch
                }

                val userDoc = userQuery.documents.first()
                val userId = userDoc.id
                val batch = db.batch() // Używamy batcha do atomowych operacji

                // Logika czyszczenia w zależności od roli
                if (rola == "Senior") {
                    // Jeśli usuwamy Seniora, usuwamy wszystkie jego zlecenia
                    cleanupZleceniaForSenior(userId, batch)
                } else {
                    // Jeśli usuwamy Wolontariusza, zwalniamy jego aktywne zlecenia
                    releaseZleceniaFromWolontariusz(userId, batch)
                }

                // Usunięcie dokumentu użytkownika
                batch.delete(userDoc.reference)
                // Zatwierdzenie wszystkich operacji w batchu
                batch.commit().await()

                _adminUiState.value = AdminUiState.Success("$rola $imie $nazwisko został pomyślnie usunięty.")

            } catch (e: Exception) {
                _adminUiState.value = AdminUiState.Error("Błąd usuwania: ${e.message}")
            }
        }
    }

    /**
     * [ADMIN-Helper] Usuwa wszystkie zlecenia powiązane z danym SeniorID.
     */
    private suspend fun cleanupZleceniaForSenior(seniorId: String, batch: WriteBatch) {
        val zleceniaQuery = db.collection("Zlecenia")
            .whereEqualTo("SeniorID", seniorId)
            .get()
            .await()
        for (zlecenie in zleceniaQuery.documents) {
            batch.delete(zlecenie.reference)
        }
    }

    /**
     * [ADMIN-Helper] Uwalnia wszystkie zlecenia przypisane do danego Wolontariusza
     * (czyści WolontariuszID i zmienia status na "Wolne").
     */
    private suspend fun releaseZleceniaFromWolontariusz(wolontariuszId: String, batch: WriteBatch) {
        val zleceniaQuery = db.collection("Zlecenia")
            .whereEqualTo("WolontariuszID", wolontariuszId)
            .get()
            .await()

        val updates = mapOf(
            "Status" to "Wolne",
            "WolontariuszID" to null
        )
        for (zlecenie in zleceniaQuery.documents) {
            batch.update(zlecenie.reference, updates)
        }
    }
}

/**
 * Reprezentuje stan ładowania danych profilowych.
 */
sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    /** Sukces, przechowuje dane profilu jako mapę. */
    data class Success(val data: Map<String, Any>) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

/**
 * ViewModel zarządzający danymi profilowymi zalogowanego użytkownika.
 * Pobiera i aktualizuje dane z Firestore.
 *
 * @param authViewModel Współdzielony ViewModel autentykacji, dostarczający ID i rolę.
 */
class UserProfileViewModel(private val authViewModel: AuthViewModel) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        // Obserwuj zmiany w ID użytkownika (logowanie/wylogowanie)
        viewModelScope.launch {
            authViewModel.currentUserId.collect { userId ->
                if (userId != null) {
                    loadProfile() // Załaduj profil, gdy użytkownik się zaloguje
                } else {
                    _profileState.value = ProfileState.Idle // Wyczyść profil po wylogowaniu
                }
            }
        }
    }

    /**
     * Ładuje profil zalogowanego użytkownika (wersja z callback).
     * Pobiera ID i rolę z [authViewModel].
     */
    fun loadProfile() {
        val userId = authViewModel.currentUserId.value
        val role = authViewModel.currentUserRole.value

        if (userId == null || role == null) {
            _profileState.value = ProfileState.Error("Brak zalogowanego użytkownika.")
            return
        }

        _profileState.value = ProfileState.Loading
        db.collection(role).document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    _profileState.value = ProfileState.Success(document.data ?: emptyMap())
                } else {
                    _profileState.value = ProfileState.Error("Nie znaleziono profilu.")
                }
            }
            .addOnFailureListener { e ->
                _profileState.value = ProfileState.Error("Błąd ładowania profilu: ${e.message}")
            }
    }

    /**
     * Zapisuje (aktualizuje) dane profilu użytkownika.
     * Używa [SetOptions.merge()], aby nie nadpisać pól, których nie edytowano.
     * Dodaje [opiekunNumer] tylko jeśli rola to "Senior".
     */
    fun saveProfile(
        imie: String,
        nazwisko: String,
        adres: String,
        telefon: String,
        opiekunNumer: String
    ) {
        val userId = authViewModel.currentUserId.value
        val role = authViewModel.currentUserRole.value

        if (userId == null || role == null) {
            _profileState.value = ProfileState.Error("Błąd zapisu: Brak użytkownika.")
            return
        }

        _profileState.value = ProfileState.Loading
        val profileData = mutableMapOf(
            "Imie" to imie,
            "Nazwisko" to nazwisko,
            "Adres" to adres,
            "Telefon" to telefon
        )

        // Pole specyficzne dla Seniora
        if (role == "Senior") {
            profileData["OpiekunNumer"] = opiekunNumer
        }

        db.collection(role).document(userId)
            .set(profileData, SetOptions.merge())
            .addOnSuccessListener {
                loadProfile() // Przeładuj profil po pomyślnym zapisie, aby odświeżyć stan
            }
            .addOnFailureListener { e ->
                _profileState.value = ProfileState.Error("Błąd zapisu: ${e.message}")
            }
    }

    /**
     * Fabryka do tworzenia instancji [UserProfileViewModel] z zależnościami.
     */
    class Factory(private val authViewModel: AuthViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserProfileViewModel(authViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/**
 * Reprezentuje stan UI dla list zleceń (publicznych, "moich", wszystkich).
 */
sealed class ZleceniaListUiState {
    object Loading : ZleceniaListUiState()
    /** @param zlecenia Lista może zawierać obiekty typu [ZlecenieRef], [PelneZlecenieDlaSeniora] lub [PelneZlecenieDlaWolontariusza]. */
    data class Success(val zlecenia: List<Any>) : ZleceniaListUiState()
    data class Error(val message: String) : ZleceniaListUiState()
}

/**
 * Reprezentuje stan UI dla ekranu szczegółów pojedynczego zlecenia.
 */
sealed class ZlecenieDetailsUiState {
    object Loading : ZlecenieDetailsUiState()
    data class Success(val pelneZlecenie: PelneZlecenie) : ZlecenieDetailsUiState()
    data class Error(val message: String) : ZlecenieDetailsUiState()
}

/**
 * Reprezentuje stan UI dla akcji wykonywanych na zleceniu
 * (np. tworzenie, podejmowanie, kończenie, potwierdzanie).
 */
sealed class ZlecenieActionUiState {
    object Idle : ZlecenieActionUiState()
    object Loading : ZlecenieActionUiState()
    data class Success(val message: String) : ZlecenieActionUiState()
    data class Error(val message: String) : ZlecenieActionUiState()
}


/**
 * ViewModel zarządzający całą logiką związaną ze zleceniami pomocy.
 * Obsługuje pobieranie list, tworzenie, podejmowanie oraz zmianę statusów zleceń.
 * Logika jest zależna od roli użytkownika pobieranej z [authViewModel].
 */
class HelpRequestViewModel(private val authViewModel: AuthViewModel) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _zleceniaListUiState = MutableStateFlow<ZleceniaListUiState>(ZleceniaListUiState.Loading)
    val zleceniaListUiState: StateFlow<ZleceniaListUiState> = _zleceniaListUiState.asStateFlow()

    private val _zlecenieDetailsUiState = MutableStateFlow<ZlecenieDetailsUiState>(ZlecenieDetailsUiState.Loading)
    val zlecenieDetailsUiState: StateFlow<ZlecenieDetailsUiState> = _zlecenieDetailsUiState.asStateFlow()

    private val _actionUiState = MutableStateFlow<ZlecenieActionUiState>(ZlecenieActionUiState.Idle)
    val actionUiState: StateFlow<ZlecenieActionUiState> = _actionUiState.asStateFlow()

    /**
     * Resetuje stan akcji (np. po wyświetleniu komunikatu o sukcesie/błędzie).
     */
    fun resetActionState() {
        _actionUiState.value = ZlecenieActionUiState.Idle
    }

    // --- FUNKCJE POBIERANIA LIST ---

    /**
     * [ADMIN] Pobiera wszystkie zlecenia, które nie są jeszcze "Zakonczone".
     * Sortuje je wg statusu: Wolne, DoPotwierdzenia, Aktywne.
     */
    fun fetchAllZlecenia() {
        _zleceniaListUiState.value = ZleceniaListUiState.Loading
        viewModelScope.launch {
            try {
                val result = db.collection("Zlecenia")
                    .whereIn("Status", listOf("Wolne", "Aktywne", "DoPotwierdzenia"))
                    .get()
                    .await()

                val zleceniaList = result.map { doc ->
                    ZlecenieRef(
                        id = doc.id,
                        seniorId = doc.getString("SeniorID"),
                        wolontariuszId = doc.getString("WolontariuszID"),
                        opis = doc.getString("Zlecenie"),
                        status = doc.getString("Status") ?: "Wolne"
                    )
                }
                // Sortuj: Wolne, DoPotwierdzenia, Aktywne
                val sortedList = zleceniaList.sortedWith(compareBy {
                    when (it.status) {
                        "Wolne" -> 1
                        "DoPotwierdzenia" -> 2
                        "Aktywne" -> 3
                        else -> 4
                    }
                })
                _zleceniaListUiState.value = ZleceniaListUiState.Success(sortedList)
            } catch (e: Exception) {
                _zleceniaListUiState.value = ZleceniaListUiState.Error("Błąd ładowania zleceń: ${e.message}")
            }
        }
    }

    // === MODYFIKACJA (Zadanie 1: Archiwum Admina) ===
    /**
     * [ADMIN] Pobiera wyłącznie zakończone zlecenia (archiwalne).
     */
    fun fetchArchiwalneZlecenia() {
        _zleceniaListUiState.value = ZleceniaListUiState.Loading
        viewModelScope.launch {
            try {
                val result = db.collection("Zlecenia")
                    .whereEqualTo("Status", "Zakonczone")
                    .get()
                    .await()

                val zleceniaList = result.map { doc ->
                    ZlecenieRef(
                        id = doc.id,
                        seniorId = doc.getString("SeniorID"),
                        wolontariuszId = doc.getString("WolontariuszID"),
                        opis = doc.getString("Zlecenie"),
                        status = "Zakonczone"
                    )
                }
                _zleceniaListUiState.value = ZleceniaListUiState.Success(zleceniaList)
            } catch (e: Exception) {
                _zleceniaListUiState.value = ZleceniaListUiState.Error("Błąd ładowania archiwum: ${e.message}")
            }
        }
    }

    /**
     * [WOLONTARIUSZ] Pobiera tylko publiczne, "Wolne" zlecenia.
     */
    fun fetchPublicZlecenia() {
        _zleceniaListUiState.value = ZleceniaListUiState.Loading
        viewModelScope.launch {
            try {
                val result = db.collection("Zlecenia")
                    .whereEqualTo("Status", "Wolne")
                    .get()
                    .await()

                val zleceniaList = result.map { doc ->
                    ZlecenieRef(
                        id = doc.id,
                        seniorId = doc.getString("SeniorID"),
                        wolontariuszId = doc.getString("WolontariuszID"),
                        opis = doc.getString("Zlecenie"),
                        status = doc.getString("Status") ?: "Wolne"
                    )
                }
                _zleceniaListUiState.value = ZleceniaListUiState.Success(zleceniaList)
            } catch (e: Exception) {
                _zleceniaListUiState.value = ZleceniaListUiState.Error("Błąd ładowania zleceń: ${e.message}")
            }
        }
    }

    /**
     * [SENIOR] Tworzy nowe zlecenie pomocy.
     * Aktualizuje [actionUiState] o wynik operacji.
     */
    fun utworzZlecenie(opis: String) {
        val seniorId = authViewModel.currentUserId.value
        if (seniorId == null) {
            _actionUiState.value = ZlecenieActionUiState.Error("Błąd: Użytkownik nie jest zalogowany.")
            return
        }
        if (opis.isBlank()) {
            _actionUiState.value = ZlecenieActionUiState.Error("Opis zlecenia nie może być pusty.")
            return
        }

        _actionUiState.value = ZlecenieActionUiState.Loading
        viewModelScope.launch {
            try {
                val noweZlecenie = hashMapOf(
                    "SeniorID" to seniorId,
                    "Zlecenie" to opis,
                    "Status" to "Wolne",
                    "WolontariuszID" to null
                )
                db.collection("Zlecenia").add(noweZlecenie).await()
                _actionUiState.value = ZlecenieActionUiState.Success("Zlecenie zostało utworzone!")
            } catch (e: Exception) {
                _actionUiState.value = ZlecenieActionUiState.Error("Błąd tworzenia zlecenia: ${e.message}")
            }
        }
    }

    /**
     * Pobiera pełne, połączone dane dla pojedynczego zlecenia (zlecenie + dane seniora).
     * Używane na ekranie szczegółów zlecenia.
     */
    fun pobierzPelneDaneZlecenia(zlecenieId: String) {
        _zlecenieDetailsUiState.value = ZlecenieDetailsUiState.Loading
        viewModelScope.launch {
            try {
                val zlecenieDoc = db.collection("Zlecenia").document(zlecenieId).get(Source.SERVER).await()

                if (!zlecenieDoc.exists()) {
                    _zlecenieDetailsUiState.value = ZlecenieDetailsUiState.Error("Zlecenie o ID $zlecenieId nie istnieje.")
                    return@launch
                }

                val seniorId = zlecenieDoc.getString("SeniorID")
                val opisZlecenia = zlecenieDoc.getString("Zlecenie") ?: "Brak opisu"
                val status = zlecenieDoc.getString("Status") ?: "Wolne"

                if (seniorId == null) {
                    _zlecenieDetailsUiState.value = ZlecenieDetailsUiState.Error("Brak SeniorID w zleceniu.")
                    return@launch
                }

                val seniorDoc = db.collection("Senior").document(seniorId).get().await()
                val seniorImie = seniorDoc.getString("Imie") ?: "Nieznane"
                val seniorNazwisko = seniorDoc.getString("Nazwisko") ?: "Nieznane"
                val seniorAdres = seniorDoc.getString("Adres") ?: "Brak Adresu"

                val pelneZlecenie = PelneZlecenie(
                    zlecenieId = zlecenieId,
                    seniorImieNazwisko = "$seniorImie $seniorNazwisko",
                    seniorAdres = seniorAdres,
                    opisZlecenia = opisZlecenia,
                    status = status,
                    seniorId = seniorId
                )
                _zlecenieDetailsUiState.value = ZlecenieDetailsUiState.Success(pelneZlecenie)
            } catch (e: Exception) {
                _zlecenieDetailsUiState.value = ZlecenieDetailsUiState.Error("Błąd pobierania danych: ${e.message}")
            }
        }
    }

    /**
     * [WOLONTARIUSZ] Podejmuje zlecenie.
     * Sprawdza limit (max 1 aktywne zlecenie).
     * Aktualizuje [actionUiState] o wynik operacji.
     */
    fun podejmijZlecenie(zlecenieId: String) {
        val wolontariuszId = authViewModel.currentUserId.value
        if (wolontariuszId == null) {
            _actionUiState.value = ZlecenieActionUiState.Error("Błąd: Użytkownik nie jest zalogowany.")
            return
        }

        _actionUiState.value = ZlecenieActionUiState.Loading
        viewModelScope.launch {
            try {
                // Krok 1: Sprawdź limit zleceń (teraz 1)
                val myActiveZlecenia = db.collection("Zlecenia")
                    .whereEqualTo("WolontariuszID", wolontariuszId)
                    .whereEqualTo("Status", "Aktywne")
                    .get()
                    .await()

                if (myActiveZlecenia.size() >= 1) {
                    _actionUiState.value = ZlecenieActionUiState.Error("Osiągnięto limit 1 aktywnego zlecenia. Ukończ je, aby przyjąć kolejne.")
                    return@launch
                }

                // Krok 2: Podejmij zlecenie (atomowa aktualizacja)
                db.collection("Zlecenia").document(zlecenieId)
                    .update(
                        "Status", "Aktywne",
                        "WolontariuszID", wolontariuszId
                    )
                    .await()

                _actionUiState.value = ZlecenieActionUiState.Success("Zlecenie przejęte pomyślnie!")
                // Odśwież widok szczegółów, jeśli na nim jesteśmy
                if (_zlecenieDetailsUiState.value is ZlecenieDetailsUiState.Success) {
                    pobierzPelneDaneZlecenia(zlecenieId)
                }

            } catch (e: Exception) {
                _actionUiState.value = ZlecenieActionUiState.Error("Błąd przejęcia: ${e.message}")
            }
        }
    }

    // --- FUNKCJE ZARZĄDZANIA STANEM ZLECENIA ---
    // === BEZ ZMIAN (Zadanie 2) - Logika jest poprawna ===

    /**
     * [WOLONTARIUSZ] Używane w "Moje Zlecenia", aby oznaczyć zlecenie jako wykonane.
     * Zmienia status na "DoPotwierdzenia".
     */
    fun oznaczJakoWykonane(zlecenieId: String) {
        _actionUiState.value = ZlecenieActionUiState.Loading
        viewModelScope.launch {
            try {
                db.collection("Zlecenia").document(zlecenieId)
                    .update("Status", "DoPotwierdzenia")
                    .await()
                _actionUiState.value = ZlecenieActionUiState.Success("Oznaczono jako wykonane. Oczekuje na potwierdzenie seniora.")
                fetchMojeZleceniaDlaWolontariusza() // Odśwież listę "Moje Zlecenia"
            } catch (e: Exception) {
                _actionUiState.value = ZlecenieActionUiState.Error("Błąd: ${e.message}")
            }
        }
    }

    /**
     * [SENIOR] Używane w "Moje Zlecenia", aby potwierdzić wykonanie.
     * Zmienia status na "Zakonczone".
     */
    fun potwierdzWykonanie(zlecenieId: String) {
        _actionUiState.value = ZlecenieActionUiState.Loading
        viewModelScope.launch {
            try {
                db.collection("Zlecenia").document(zlecenieId)
                    .update("Status", "Zakonczone")
                    .await()
                _actionUiState.value = ZlecenieActionUiState.Success("Zlecenie zostało zakończone. Dziękujemy!")
                fetchMojeZleceniaDlaSeniora() // Odśwież listę "Moje Zlecenia"
            } catch (e: Exception) {
                _actionUiState.value = ZlecenieActionUiState.Error("Błąd: ${e.message}")
            }
        }
    }

    // --- FUNKCJE DLA "MOJE ZLECENIA" ---

    /**
     * [SENIOR] Pobiera listę wszystkich zleceń utworzonych przez zalogowanego seniora,
     * które nie są jeszcze "Zakonczone".
     */
    fun fetchMojeZleceniaDlaSeniora() {
        val seniorId = authViewModel.currentUserId.value
        if (seniorId == null) {
            _zleceniaListUiState.value = ZleceniaListUiState.Error("Brak zalogowanego użytkownika.")
            return
        }
        _zleceniaListUiState.value = ZleceniaListUiState.Loading

        viewModelScope.launch {
            try {
                val zleceniaQuery = db.collection("Zlecenia")
                    .whereEqualTo("SeniorID", seniorId)
                    .whereIn("Status", listOf("Wolne", "Aktywne", "DoPotwierdzenia"))
                    .get()
                    .await()

                val listaPelnychZlecen = mutableListOf<PelneZlecenieDlaSeniora>()

                for (doc in zleceniaQuery.documents) {
                    val wolontariuszId = doc.getString("WolontariuszID")
                    val status = doc.getString("Status") ?: "Wolne"

                    var wolontariuszName = "Oczekuje na wolontariusza"
                    var wolontariuszPhone = "---"

                    // Pobierz dane wolontariusza tylko jeśli jest przypisany
                    if (status != "Wolne" && wolontariuszId != null) {
                        val wolontariuszDoc = db.collection("Wolontariusz").document(wolontariuszId).get().await()
                        val imie = wolontariuszDoc.getString("Imie") ?: "Brak"
                        val nazwisko = wolontariuszDoc.getString("Nazwisko") ?: "Danych"
                        wolontariuszName = "$imie $nazwisko"
                        wolontariuszPhone = wolontariuszDoc.getString("Telefon") ?: "Brak numeru"
                    }

                    listaPelnychZlecen.add(
                        PelneZlecenieDlaSeniora(
                            zlecenieId = doc.id,
                            opis = doc.getString("Zlecenie") ?: "Brak opisu",
                            status = status,
                            wolontariuszImieNazwisko = wolontariuszName,
                            wolontariuszTelefon = wolontariuszPhone
                        )
                    )
                }
                // Sortuj: DoPotwierdzenia, Aktywne, Wolne
                val sortedList = listaPelnychZlecen.sortedWith(compareBy {
                    when (it.status) {
                        "DoPotwierdzenia" -> 1
                        "Aktywne" -> 2
                        "Wolne" -> 3
                        else -> 4
                    }
                })
                _zleceniaListUiState.value = ZleceniaListUiState.Success(sortedList)

            } catch (e: Exception) {
                _zleceniaListUiState.value = ZleceniaListUiState.Error("Błąd pobierania zleceń: ${e.message}")
            }
        }
    }

    /**
     * [WOLONTARIUSZ] Pobiera listę zleceń podjętych przez zalogowanego wolontariusza.
     * Pokazuje tylko statusy "Aktywne" i "DoPotwierdzenia".
     */
    fun fetchMojeZleceniaDlaWolontariusza() {
        val wolontariuszId = authViewModel.currentUserId.value
        if (wolontariuszId == null) {
            _zleceniaListUiState.value = ZleceniaListUiState.Error("Brak zalogowanego użytkownika.")
            return
        }
        _zleceniaListUiState.value = ZleceniaListUiState.Loading

        viewModelScope.launch {
            try {
                val zleceniaQuery = db.collection("Zlecenia")
                    .whereEqualTo("WolontariuszID", wolontariuszId)
                    .whereIn("Status", listOf("Aktywne", "DoPotwierdzenia"))
                    .get()
                    .await()

                val listaPelnychZlecen = mutableListOf<PelneZlecenieDlaWolontariusza>()
                val seniorTasks = mutableMapOf<String, Task<DocumentSnapshot>>()

                // Zbierz ID wszystkich unikalnych seniorów do pobrania
                for (doc in zleceniaQuery.documents) {
                    val seniorId = doc.getString("SeniorID")
                    if (seniorId != null && !seniorTasks.containsKey(seniorId)) {
                        seniorTasks[seniorId] = db.collection("Senior").document(seniorId).get()
                    }
                }

                // Poczekaj na pobranie danych wszystkich seniorów równolegle
                Tasks.whenAllSuccess<DocumentSnapshot>(seniorTasks.values).await()

                // Zmapuj pobrane dane seniorów dla łatwego dostępu
                val seniorDataMap = seniorTasks.mapValues {
                    val doc = it.value.result
                    Triple(
                        doc.getString("Imie") ?: "Brak",
                        doc.getString("Adres") ?: "Brak",
                        doc.getString("Telefon") ?: "Brak"
                    )
                }

                // Zbuduj listę zleceń z dołączonymi danymi seniorów
                for (doc in zleceniaQuery.documents) {
                    val seniorId = doc.getString("SeniorID")
                    val seniorInfo = seniorDataMap[seniorId]

                    listaPelnychZlecen.add(
                        PelneZlecenieDlaWolontariusza(
                            zlecenieId = doc.id,
                            opis = doc.getString("Zlecenie") ?: "Brak opisu",
                            seniorImieNazwisko = seniorInfo?.first ?: "Brak danych",
                            seniorAdres = seniorInfo?.second ?: "Brak danych",
                            seniorTelefon = seniorInfo?.third ?: "Brak numeru",
                            status = doc.getString("Status") ?: "Aktywne"
                        )
                    )
                }
                // Sortuj: DoPotwierdzenia, Aktywne
                val sortedList = listaPelnychZlecen.sortedWith(compareBy {
                    when (it.status) {
                        "DoPotwierdzenia" -> 1
                        "Aktywne" -> 2
                        else -> 3
                    }
                })
                _zleceniaListUiState.value = ZleceniaListUiState.Success(sortedList)

            } catch (e: Exception) {
                _zleceniaListUiState.value = ZleceniaListUiState.Error("Błąd pobierania zleceń: ${e.message}")
            }
        }
    }

    /**
     * Fabryka do tworzenia instancji [HelpRequestViewModel] z zależnościami.
     */
    class HelpRequestViewModelFactory(private val authViewModel: AuthViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HelpRequestViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HelpRequestViewModel(authViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/**
 * ViewModel zarządzający wydarzeniami w kalendarzu.
 * Obsługuje logikę zależną od roli (Senior/Wolontariusz), pobierając dane
 * z odpowiednich kolekcji w Firestore.
 * Zarządza również planowaniem i anulowaniem powiadomień [AlarmManager].
 *
 * @param application Kontekst aplikacji, potrzebny do [AlarmManager].
 * @param authViewModel Dostarcza ID i rolę zalogowanego użytkownika.
 */
@RequiresApi(Build.VERSION_CODES.O)
class SeniorCalendarViewModel(
    application: Application,
    private val authViewModel: AuthViewModel
) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val _events = MutableStateFlow<Map<LocalDate, List<CalendarEvent>>>(emptyMap())
    val events = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val context: Context
        get() = getApplication<Application>().applicationContext
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        // Obserwuj zmianę użytkownika (logowanie/wylogowanie)
        viewModelScope.launch {
            authViewModel.currentUserRole.collect { role ->
                if (role != null) {
                    loadEvents() // Załaduj wydarzenia, gdy użytkownik się zaloguje
                } else {
                    _events.value = emptyMap() // Wyczyść wydarzenia po wylogowaniu
                }
            }
        }
    }

    /**
     * Zwraca parę (Nazwa Kolekcji, Nazwa Pola ID) na podstawie roli użytkownika.
     * Np. ("WydarzeniaSenior", "SeniorID") dla Seniora.
     * Zwraca null, jeśli rola nie jest obsługiwana.
     */
    private fun getCollectionInfo(): Pair<String, String>? {
        val role = authViewModel.currentUserRole.value
        val collectionName = when (role) {
            "Senior" -> "WydarzeniaSenior"
            "Wolontariusz" -> "WydarzeniaWolontariusz"
            else -> null
        }
        val idField = when (role) {
            "Senior" -> "SeniorID"
            "Wolontariusz" -> "WolontariuszID"
            else -> null
        }
        if (collectionName == null || idField == null) {
            if (role != null) {
                _error.value = "Błąd: Nieznana rola użytkownika ($role)."
            }
            return null
        }
        return Pair(collectionName, idField)
    }

    /**
     * Ładuje wydarzenia z Firestore dla zalogowanego użytkownika i jego roli.
     */
    fun loadEvents() {
        val userId = authViewModel.currentUserId.value
        val (collectionName, idField) = getCollectionInfo() ?: return
        if (userId == null) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = db.collection(collectionName)
                    .whereEqualTo(idField, userId)
                    .get()
                    .await()

                val eventsMap = mutableMapOf<LocalDate, MutableList<CalendarEvent>>()
                for (doc in result.documents) {
                    try {
                        val timestamp = doc.getTimestamp("Data") ?: continue
                        val title = doc.getString("Tytul") ?: "Brak tytułu"
                        val docId = doc.id

                        val localDateTime = timestamp.toDate().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()

                        val date = localDateTime.toLocalDate()
                        val time = localDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

                        val event = CalendarEvent(firebaseDocId = docId, time = time, title = title)

                        val dayEvents = eventsMap.getOrPut(date) { mutableListOf() }
                        dayEvents.add(event)
                    } catch (e: Exception) {
                        Log.e("CalendarVM", "Błąd parsowania wydarzenia: ${e.message}", e)
                    }
                }
                // Sortuj wydarzenia w każdym dniu po godzinie
                eventsMap.values.forEach { it.sortBy { e -> e.time } }
                _events.value = eventsMap
            } catch (e: Exception) {
                _error.value = "Błąd ładowania wydarzeń: ${e.message}"
                Log.e("CalendarVM", "Błąd ładowania: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Dodaje nowe wydarzenie do Firestore i planuje powiadomienie.
     */
    fun addEvent(date: LocalDate, time: String, title: String) {
        val userId = authViewModel.currentUserId.value ?: return
        val (collectionName, idField) = getCollectionInfo() ?: return

        _isLoading.value = true // Używamy isLoading, aby dać znać UI
        viewModelScope.launch {
            try {
                val localTime = LocalTime.parse(time)
                val dateTime = LocalDateTime.of(date, localTime)
                val firebaseTimestamp = Timestamp(dateTime.atZone(ZoneId.systemDefault()).toInstant())

                val newEventData = hashMapOf(
                    "Tytul" to title,
                    "Data" to firebaseTimestamp,
                    idField to userId
                )

                val documentReference = db.collection(collectionName).add(newEventData).await()
                val notificationId = documentReference.id.hashCode().toLong()
                scheduleNotification(date, time, title, notificationId)
                loadEvents() // Przeładuj wydarzenia po dodaniu
            } catch (e: Exception) {
                _error.value = "Błąd dodawania wydarzenia: ${e.message}"
                Log.e("CalendarVM", "Błąd dodawania/parsowania: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Usuwa wydarzenie z Firestore i anuluje powiązane powiadomienie.
     */
    fun deleteEvent(event: CalendarEvent) {
        val (collectionName, _) = getCollectionInfo() ?: return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                db.collection(collectionName).document(event.firebaseDocId).delete().await()
                cancelNotification(event)
                loadEvents() // Przeładuj wydarzenia po usunięciu
            } catch (e: Exception) {
                _error.value = "Błąd usuwania wydarzenia: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Czyści stan błędu (np. po wyświetleniu go użytkownikowi).
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Planuje dokładny alarm (powiadomienie) na określony czas.
     * Wymaga uprawnienia `SCHEDULE_EXACT_ALARM` na Android 12+.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNotification(date: LocalDate, time: String, title: String, eventId: Long) {
        val localTime = try { LocalTime.parse(time) } catch (e: Exception) { return }
        val dateTime = LocalDateTime.of(date, localTime)
        val triggerAtMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // Nie planuj powiadomień dla przeszłych wydarzeń
        if (triggerAtMillis < System.currentTimeMillis()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "EVENT_ALARM"
            putExtra("NOTIFICATION_TITLE", "Przypomnienie: $title")
            putExtra("NOTIFICATION_MESSAGE", "Zaplanowane na $time")
            putExtra("NOTIFICATION_ID", eventId.toInt())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Sprawdzenie uprawnień dla Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            } else {
                Log.w("CalendarViewModel", "Nie można ustawić dokładnego alarmu. Brak uprawnień.")
            }
        } else {
            // Starsze wersje Androida
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    /**
     * Anuluje zaplanowane powiadomienie dla danego wydarzenia.
     */
    private fun cancelNotification(event: CalendarEvent) {
        val eventId = event.firebaseDocId.hashCode().toLong()

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "EVENT_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Fabryka do tworzenia instancji [SeniorCalendarViewModel] z zależnościami.
     */
    class Factory(
        private val application: Application,
        private val authViewModel: AuthViewModel
    ) : ViewModelProvider.Factory {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SeniorCalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SeniorCalendarViewModel(application, authViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/**
 * Reprezentuje stan automatu skończonego wykrywacza upadku.
 */
sealed class FallDetectionState {
    /** Stan spoczynku, monitorowanie. */
    object Idle : FallDetectionState()
    /** Stan odliczania po wykryciu potencjalnego upadku. */
    data class Countdown(val secondsLeft: Int) : FallDetectionState()
    /** Stan alarmu, uruchamiany po zakończeniu odliczania. */
    object Alarm : FallDetectionState()
}

/**
 * ViewModel zarządzający logiką wykrywania upadku za pomocą akcelerometru.
 * Implementuje [SensorEventListener] do nasłuchiwania zmian czujnika.
 */
class FallDetectorViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {
    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    // Stałe progowe do kalibracji
    private val FREE_FALL_THRESHOLD = 2.0f
    private val IMPACT_THRESHOLD = 25.0f

    private var wasInFreeFall = false
    private var freeFallTimestamp: Long = 0
    private var countDownTimer: CountDownTimer? = null

    private val _fallDetectionState = MutableStateFlow<FallDetectionState>(FallDetectionState.Idle)
    val fallDetectionState = _fallDetectionState.asStateFlow()

    init {
        startMonitoring()
    }

    /**
     * Rejestruje listener czujnika akcelerometru.
     */
    private fun startMonitoring() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    /**
     * Zatrzymuje monitorowanie czujnika (wyrejestrowuje listener).
     * Należy wywołać, gdy odliczanie jest aktywne lub aplikacja jest w tle.
     */
    fun stopMonitoring() {
        sensorManager.unregisterListener(this)
    }

    /**
     * Główna logika wykrywania upadku. Wywoływana przy każdej zmianie odczytu akcelerometru.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Obliczenie wektora przyspieszenia
            val currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            // Ignoruj odczyty, jeśli już jesteśmy w trakcie odliczania lub alarmu
            if (_fallDetectionState.value != FallDetectionState.Idle) return

            // Faza 1: Wykrycie swobodnego spadku (przyspieszenie bliskie 0)
            if (currentAcceleration < FREE_FALL_THRESHOLD) {
                wasInFreeFall = true
                freeFallTimestamp = System.currentTimeMillis()
                return
            }

            // Faza 2: Wykrycie uderzenia (wysokie przyspieszenie) po swobodnym spadku
            if (wasInFreeFall && currentAcceleration > IMPACT_THRESHOLD) {
                val timeSinceFreeFall = System.currentTimeMillis() - freeFallTimestamp
                // Upewnij się, że uderzenie nastąpiło krótko po spadku (np. w ciągu 1 sekundy)
                if (timeSinceFreeFall < 1000) {
                    startCountdown() // Rozpocznij odliczanie
                }
                wasInFreeFall = false // Zresetuj stan swobodnego spadku
            }

            // Resetuj stan swobodnego spadku, jeśli nie było uderzenia przez 1.5 sekundy
            if (wasInFreeFall && (System.currentTimeMillis() - freeFallTimestamp > 1500)) {
                wasInFreeFall = false
            }
        }
    }

    /**
     * Wywoływane przy zmianie dokładności czujnika (nieużywane w tej logice).
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Celowo puste
    }

    /**
     * Rozpoczyna 15-sekundowe odliczanie po wykryciu potencjalnego upadku.
     * Zatrzymuje monitorowanie i uruchamia wibracje odliczania.
     */
    private fun startCountdown() {
        stopMonitoring() // Zatrzymaj czujnik, aby uniknąć wielokrotnych wywołań
        triggerVibration(isCountdown = true)
        _fallDetectionState.value = FallDetectionState.Countdown(15)

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _fallDetectionState.value = FallDetectionState.Countdown((millisUntilFinished / 1000).toInt() + 1)
            }

            override fun onFinish() {
                triggerAlarm() // Po 15 sekundach uruchom alarm
            }
        }.start()
    }

    /**
     * Anuluje aktywne odliczanie (np. gdy użytkownik naciśnie "Wszystko OK").
     * Zatrzymuje wibracje i wznawia monitorowanie.
     */
    fun cancelCountdown() {
        countDownTimer?.cancel()
        stopVibration()
        _fallDetectionState.value = FallDetectionState.Idle
        startMonitoring() // Wznów monitorowanie
    }

    /**
     * Uruchamia stan alarmu (po zakończeniu odliczania).
     * Uruchamia wibracje alarmowe (ciągłe).
     */
    private fun triggerAlarm() {
        _fallDetectionState.value = FallDetectionState.Alarm
        triggerVibration(isCountdown = false)
    }

    /**
     * Anuluje stan alarmu (np. po interwencji opiekuna).
     * Zatrzymuje wibracje i wznawia monitorowanie.
     */
    fun cancelAlarm() {
        stopVibration()
        _fallDetectionState.value = FallDetectionState.Idle
        startMonitoring()
    }

    /**
     * Uruchamia wibracje w zależności od trybu (odliczanie lub alarm).
     * @param isCountdown Jeśli true, wibracje są krótkie; jeśli false, długie i powtarzalne.
     */
    fun triggerVibration(isCountdown: Boolean) {
        val pattern = if (isCountdown) longArrayOf(0, 300) else longArrayOf(0, 500, 500, 500)
        // repeatMode: -1 = brak powtórzeń, 0 = powtarzaj od początku, 1 = powtarzaj (przestarzałe)
        val repeatMode = if (isCountdown) 1 else 0 // Używamy 1 dla krótkich pulsów, 0 dla ciągłego wzorca

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeatMode))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, repeatMode)
        }
    }

    /**
     * Natychmiast zatrzymuje wszystkie aktywne wibracje.
     */
    fun stopVibration() {
        vibrator.cancel()
    }

    /**
     * Metoda cyklu życia ViewModel. Zwalnia zasoby (zatrzymuje czujniki, wibracje, timery).
     */
    override fun onCleared() {
        stopMonitoring()
        stopVibration()
        countDownTimer?.cancel()
        super.onCleared()
    }
}

// === MODYFIKACJA (Zadanie 3: Firebase Seniorm2) ===
// Cały poniższy ViewModel został zastąpiony wersją zintegrowaną z Firestore
// i wstrzykniętym AuthViewModel.

/**
 * ViewModel do zarządzania cyklicznymi zadaniami (Seniorm2), np. przypomnieniami o lekach.
 *
 * Dane są teraz trwale zapisywane w kolekcji "ZadaniaCykliczne" w Firestore
 * i powiązane z ID zalogowanego użytkownika.
 *
 * @param application Kontekst aplikacji, potrzebny do [AlarmManager].
 * @param authViewModel Dostarcza ID i rolę zalogowanego użytkownika.
 */


/**
 * ViewModel do zarządzania cyklicznymi zadaniami (Seniorm2), np. przypomnieniami o lekach.
 *
 * UWAGA: Ta implementacja przechowuje zadania **tylko w pamięci** (`MutableStateFlow`).
 * Dane zostaną utracone po zamknięciu aplikacji.
 */
@RequiresApi(Build.VERSION_CODES.O)
class Seniorm2ViewModel(application: Application) : AndroidViewModel(application) {

    private val _seniorm2Items = MutableStateFlow<List<Seniorm2Item>>(emptyList())
    val seniorm2Items = _seniorm2Items.asStateFlow()

    private val context: Context
        get() = getApplication<Application>().applicationContext
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Dodaje nowe zadanie cykliczne i planuje powiadomienia dla wybranych dni.
     */
    fun addSeniorm2(name: String, time: String, daysOfWeek: Set<DayOfWeek>) {
        val newSeniorm2Item = Seniorm2Item(name = name, time = time, daysOfWeek = daysOfWeek)
        // Dodaj do listy (w pamięci) i posortuj
        _seniorm2Items.update { (it + newSeniorm2Item).sortedBy { item -> item.time } }

        // Zaplanuj alarm dla każdego wybranego dnia
        for (day in daysOfWeek) {
            scheduleWeeklyRepeatingNotification(newSeniorm2Item.time, newSeniorm2Item.name, newSeniorm2Item.id, day)
        }
    }

    /**
     * Aktualizuje istniejące zadanie.
     * Anuluje *wszystkie* poprzednie alarmy dla tego zadania i planuje nowe.
     */
    fun updateSeniorm2(updatedItem: Seniorm2Item) {
        // Anuluj wszystkie potencjalne alarmy (dla wszystkich 7 dni) dla tego ID
        for (day in DayOfWeek.values()) {
            cancelWeeklyRepeatingNotification(updatedItem.id, day)
        }

        // Zaplanuj nowe alarmy na podstawie zaktualizowanych dni
        for (day in updatedItem.daysOfWeek) {
            scheduleWeeklyRepeatingNotification(updatedItem.time, updatedItem.name, updatedItem.id, day)
        }

        // Zaktualizuj listę (w pamięci)
        _seniorm2Items.update { currentList ->
            currentList.map {
                if (it.id == updatedItem.id) updatedItem else it
            }.sortedBy { item -> item.time }
        }
    }

    /**
     * Usuwa zadanie cykliczne i anuluje wszystkie powiązane z nim powiadomienia.
     */
    fun deleteSeniorm2(itemToDelete: Seniorm2Item) {
        // Anuluj wszystkie potencjalne alarmy (dla wszystkich 7 dni)
        for (day in DayOfWeek.values()) {
            cancelWeeklyRepeatingNotification(itemToDelete.id, day)
        }
        // Usuń z listy (w pamięci)
        _seniorm2Items.update { currentList ->
            currentList.filterNot { it.id == itemToDelete.id }
        }
    }

    /**
     * Generuje unikalne ID alarmu na podstawie ID przedmiotu i dnia tygodnia.
     */
    private fun getUniqueAlarmId(itemId: String, day: DayOfWeek): Int {
        return (itemId + day.name).hashCode()
    }

    /**
     * Planuje cotygodniowy, powtarzalny alarm dla zadania.
     */
    private fun scheduleWeeklyRepeatingNotification(time: String, title: String, itemId: String, day: DayOfWeek) {
        val localTime = try { LocalTime.parse(time) } catch (e: Exception) { return }

        val now = LocalDateTime.now()
        // Znajdź następne lub dzisiejsze wystąpienie danego dnia tygodnia o danej godzinie
        var nextTriggerDateTime = now.with(TemporalAdjusters.nextOrSame(day)).with(localTime)

        // Jeśli obliczony czas jest w przeszłości (np. dzisiaj, ale o wcześniejszej godzinie),
        // ustaw alarm na następny tydzień
        if (nextTriggerDateTime.isBefore(now)) {
            nextTriggerDateTime = nextTriggerDateTime.plusWeeks(1)
        }

        val triggerAtMillis = nextTriggerDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val alarmId = getUniqueAlarmId(itemId, day)

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "SENIORM1_ALARM" // Używamy "SENIORM1" jako akcji
            putExtra("NOTIFICATION_TITLE", "Pora na: $title")
            putExtra("NOTIFICATION_MESSAGE", "Zaplanowane na $time (dziś)")
            putExtra("NOTIFICATION_ID", alarmId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Sprawdzenie uprawnień dla Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Log.w("Seniorm2ViewModel", "Brak uprawnień do dokładnych alarmów.")
            return // Nie można zaplanować alarmu
        }

        // Ustaw alarm powtarzalny co 7 dni
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            AlarmManager.INTERVAL_DAY * 7, // Powtarzaj co tydzień
            pendingIntent
        )
        Log.d("Seniorm2ViewModel", "Ustawiono alarm dla $itemId ($title) na $day o $time (ID: $alarmId)")
    }

    /**
     * Anuluje cotygodniowy alarm dla zadania w określonym dniu.
     */
    private fun cancelWeeklyRepeatingNotification(itemId: String, day: DayOfWeek) {
        val alarmId = getUniqueAlarmId(itemId, day)
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "SENIORM1_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d("Seniorm2ViewModel", "Anulowano alarm dla $itemId na $day (ID: $alarmId)")
    }
}


/**
 * Repozytorium zarządzające trwałym zapisem ustawień aplikacji
 * (tryb wysokiego kontrastu, skalowanie czcionki) w [SharedPreferences].
 */
class SettingsRepository(context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE)
    companion object {
        private const val KEY_HIGH_CONTRAST = "key_high_contrast"
        private const val KEY_FONT_SCALE = "key_font_scale"
        private const val DEFAULT_FONT_SCALE = 1.0f
    }

    /** Zapisuje stan trybu wysokiego kontrastu. */
    fun saveHighContrast(isEnabled: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_HIGH_CONTRAST, isEnabled).apply()
    }
    /** Odczytuje stan trybu wysokiego kontrastu. */
    fun isHighContrastEnabled() = sharedPrefs.getBoolean(KEY_HIGH_CONTRAST, false)

    /** Zapisuje mnożnik skali czcionki. */
    fun saveFontSizeScale(scale: Float) {
        sharedPrefs.edit().putFloat(KEY_FONT_SCALE, scale).apply()
    }
    /** Odczytuje mnożnik skali czcionki. */
    fun getFontSizeScale(): Float {
        return sharedPrefs.getFloat(KEY_FONT_SCALE, DEFAULT_FONT_SCALE)
    }
}

/**
 * ViewModel zarządzający stanem ustawień aplikacji.
 * Udostępnia [StateFlow] dla [fontSizeScale] i [isHighContrast],
 * umożliwiając UI reakcję na zmiany.
 */
class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    private val _fontSizeScale = MutableStateFlow(repository.getFontSizeScale())
    val fontSizeScale = _fontSizeScale.asStateFlow()

    private val _isHighContrast = MutableStateFlow(repository.isHighContrastEnabled())
    val isHighContrast = _isHighContrast.asStateFlow()

    /** Aktualizuje skalę czcionki i zapisuje ją w repozytorium. */
    fun setFontSizeScale(scale: Float) {
        _fontSizeScale.value = scale
        repository.saveFontSizeScale(scale)
    }

    /** Aktualizuje tryb wysokiego kontrastu i zapisuje go w repozytorium. */
    fun setHighContrast(isEnabled: Boolean) {
        _isHighContrast.value = isEnabled
        repository.saveHighContrast(isEnabled)
    }

    /** Fabryka do tworzenia [SettingsViewModel] z wstrzykniętym [SettingsRepository]. */
    class SettingsViewModelFactory(private val repository: SettingsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
// ########## KONIEC VIEWMODELI ##########


// ########## ODBIORNIK POWIADOMIEŃ i Pomocnik SMS ##########

/**
 * Globalny [BroadcastReceiver] do obsługi alarmów (z Kalendarza i Seniorm2).
 * Odbiera [Intent], buduje powiadomienie i wyświetla je.
 */
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val title = intent.getStringExtra("NOTIFICATION_TITLE") ?: "Przypomnienie"
        val message = intent.getStringExtra("NOTIFICATION_MESSAGE") ?: "Masz nowe zadanie"
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)

        val notification = NotificationCompat.Builder(context, "SENIOR_APP_CHANNEL")
            .setSmallIcon(R.mipmap.ic_launcher) // Upewnij się, że masz tę ikonę w `res/mipmap`
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVibrate(longArrayOf(0, 500, 500, 500)) // Wzór wibracji
            .setAutoCancel(true) // Zamyka powiadomienie po kliknięciu
            .build()

        notificationManager.notify(notificationId, notification)
    }
}

/**
 * Funkcja pomocnicza do wysyłania wiadomości SMS.
 * Obsługuje sprawdzanie uprawnień i błędów.
 * Wyświetla [Toast] na głównym wątku (UI) niezależnie od wątku wywołania.
 *
 * @param context Kontekst (np. z Activity lub ViewModel).
 * @param phoneNumber Numer telefonu odbiorcy.
 * @param message Treść wiadomości.
 */
fun sendSms(context: Context, phoneNumber: String, message: String) {
    if (phoneNumber.isBlank() || message.isBlank()) {
        Log.w("sendSms", "Numer telefonu lub wiadomość są puste. Przerywam wysyłanie SMS.")
        // Użyj Handlera, aby Toast działał z dowolnego wątku (np. z ViewModelu)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Brak numeru opiekuna w profilu.", Toast.LENGTH_LONG).show()
        }
        return
    }
    try {
        // Pobranie SmsManager w sposób bezpieczny dla różnych wersji API
        val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(SmsManager::class.java)
        } else {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        }
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Log.d("sendSms", "Wiadomość SMS została wysłana do $phoneNumber.")
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Wysłano SMS S.O.S. do opiekuna.", Toast.LENGTH_LONG).show()
        }
    } catch (e: SecurityException) {
        // Brak uprawnienia SEND_SMS
        Log.e("sendSms", "Brak uprawnień do wysyłania SMS.", e)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Błąd: Brak uprawnień do wysłania SMS.", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        // Inne błędy (np. brak sygnału)
        Log.e("sendSms", "Nie udało się wysłać SMS: ${e.message}", e)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Błąd podczas próby wysłania SMS.", Toast.LENGTH_LONG).show()
        }
    }
}
// ########## KONIEC ODBIORNIKÓW ##########


// ########## KOMPONENTY UI (Współdzielone) ##########

/**
 * Przycisk z gradientowym tłem, obsługujący tryb wysokiego kontrastu
 * oraz wyświetlanie wskaźnika ładowania.
 *
 * @param text Tekst na przycisku. Jeśli tekst to np. "Logowanie...", "Analizuję...",
 * wyświetli się [CircularProgressIndicator].
 * @param onClick Akcja do wykonania.
 * @param enabled Czy przycisk jest aktywny.
 * @param colors Niestandardowe kolory. Używane do tworzenia przycisków
 * o jednolitym kolorze (np. zielonych) zamiast gradientu.
 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )
) {
    val isHighContrast = MaterialTheme.colorScheme.background == Color.Black

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(),
        colors = if (isHighContrast) {
            // W trybie wysokiego kontrastu użyj standardowych kolorów motywu
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        } else {
            colors // W trybie domyślnym użyj przekazanych kolorów
        }
    ) {
        // Logika tła (gradient lub jednolity kolor)
        val boxModifier = if (isHighContrast) {
            Modifier.fillMaxSize() // Brak gradientu w trybie HC
        } else {
            // Jeśli przekazane kolory *nie są* przezroczyste, użyj ich (bez gradientu)
            if (colors.containerColor != Color.Transparent) {
                Modifier.fillMaxSize()
            } else {
                // W przeciwnym razie zastosuj gradient (tylko jeśli kolory są domyślne)
                val gradient = if (enabled) {
                    Brush.horizontalGradient(colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    ))
                } else {
                    Brush.horizontalGradient(colors = listOf(
                        Color.Gray.copy(alpha = 0.5f),
                        Color.Gray.copy(alpha = 0.3f)
                    ))
                }
                Modifier
                    .fillMaxSize()
                    .background(gradient, shape = RoundedCornerShape(50))
            }
        }

        // Logika koloru tekstu
        val textColor = if (isHighContrast) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            if (colors.containerColor != Color.Transparent) {
                // Jeśli tło jest jednolite (np. zielone), użyj koloru na tym tle
                MaterialTheme.colorScheme.onPrimary
            } else {
                // Domyślny kolor tekstu dla tła gradientowego
                Color.White
            }
        }

        Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
            // Wyświetl wskaźnik ładowania, jeśli tekst na to wskazuje
            if (enabled && (text == "Analizuję..." || text == "Logowanie..." || text == "Wysyłanie..." || text == "Zapisywanie..." || text == "Usuwanie...")) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text,
                    color = if (enabled) textColor else textColor.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Niestandardowe, obramowane pole tekstowe (wrapper na [OutlinedTextField]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary,
        disabledBorderColor = MaterialTheme.colorScheme.outline,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTextColor = MaterialTheme.colorScheme.onSurface
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        enabled = enabled,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        colors = colors
    )
}

/**
 * Niestandardowe pole do wprowadzania hasła z ikoną widoczności.
 * Używa [CustomTextField] wewnątrz.
 */
@Composable
fun CustomPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        enabled = enabled,
        keyboardType = KeyboardType.Password,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Ukryj hasło" else "Pokaż hasło"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
    )
}

/**
 * Kafelek w stylu "Metro" (Windows Phone) do nawigacji na ekranie głównym.
 * Dostosowuje kolory do trybu wysokiego kontrastu.
 * Skaluje rozmiar ikon i paddingów w oparciu o ustawienia czcionki systemowej.
 */
@Composable
fun MetroTile(
    title: String,
    description: String? = null,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 2f
) {
    val isHighContrast = MaterialTheme.colorScheme.background == Color.Black
    val finalBackgroundColor = if (isHighContrast) MaterialTheme.colorScheme.primary else backgroundColor
    // Wybierz kolor treści (biały lub czarny) na podstawie luminancji tła
    val contentColor = if (isHighContrast) MaterialTheme.colorScheme.onPrimary else {
        if (backgroundColor.luminance() > 0.5) Color.Black else Color.White
    }

    // Ręczne skalowanie DP na podstawie skali czcionki (dla ułatwień dostępu)
    val fontScale = LocalDensity.current.fontScale
    val baseIconSize = 48.dp
    val basePadding = 16.dp
    val baseSpacer = 8.dp
    val scaledIconSize = baseIconSize * fontScale
    val scaledPadding = basePadding * fontScale
    val scaledSpacer = baseSpacer * fontScale

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = finalBackgroundColor),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaledPadding),
            verticalArrangement = Arrangement.Bottom // Treść wyrównana do dołu
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(scaledIconSize)
            )
            Spacer(Modifier.height(scaledSpacer))
            Text(
                text = title,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2
            )
            if (description != null) {
                Text(
                    text = description,
                    color = contentColor.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Prosty element listy dla [mockServices] (np. Zakupy, Sprzątanie).
 */
@Composable
fun ServiceItem(service: Service, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(service.icon, null, Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Text(service.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }
    }
}

/**
 * Dialog do dodawania nowej prośby o pomoc (zlecenia).
 *
 * (NAPRAWIONY) Ten komponent został zrefaktoryzowany, aby poprawnie współpracować
 * ze stanem `actionUiState` z [HelpRequestViewModel].
 * Automatycznie pokazuje `Toast` i zamyka się po pomyślnym dodaniu zlecenia.
 *
 * @param serviceTitle Tytuł usługi (np. "Zakupy").
 * @param onDismiss Funkcja wywoływana przy zamknięciu dialogu.
 * @param helpRequestViewModel ViewModel zarządzający logiką zleceń.
 */
@Composable
fun AddHelpRequestDialog(
    serviceTitle: String,
    onDismiss: () -> Unit,
    helpRequestViewModel: HelpRequestViewModel
) {
    var details by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Obserwuj stan akcji z ViewModelu
    val actionState by helpRequestViewModel.actionUiState.collectAsState()
    val isLoading = actionState is ZlecenieActionUiState.Loading

    // Efekt, który reaguje na zmianę stanu
    LaunchedEffect(actionState) {
        when (val state = actionState) {
            is ZlecenieActionUiState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                helpRequestViewModel.resetActionState() // Zresetuj stan
                onDismiss() // Zamknij dialog
            }
            is ZlecenieActionUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                helpRequestViewModel.resetActionState() // Zresetuj stan
            }
            else -> {
                // Idle lub Loading
            }
        }
    }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() }, // Nie zamykaj podczas ładowania
        title = { Text("Prośba o pomoc: $serviceTitle") },
        text = {
            Column {
                Text("Opisz krótko, czego potrzebujesz (np. 'Mleko, chleb' lub 'W kuchni nie świeci światło').")
                Spacer(Modifier.height(16.dp))
                CustomTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = "Szczegóły...",
                    enabled = !isLoading
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val pelnyOpis = "$serviceTitle: $details"
                    helpRequestViewModel.utworzZlecenie(pelnyOpis)
                },
                enabled = details.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Wyślij prośbę", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Anuluj", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

/**
 * Element listy dla publicznych zleceń (widok Wolontariusza lub Admina).
 *
 * @param zlecenie Referencja do zlecenia.
 * @param isAdminView Jeśli true, pokazuje dodatkowo status zlecenia.
 * @param onClick Akcja po kliknięciu.
 */
@Composable
fun ZlecenieItemPublic(
    zlecenie: ZlecenieRef,
    isAdminView: Boolean,
    onClick: () -> Unit
) {
    val statusText = zlecenie.status ?: "Wolne"
    val statusColor = when (statusText) {
        "Wolne" -> MaterialTheme.colorScheme.primary
        "Aktywne" -> AccentGreen
        "DoPotwierdzenia" -> AccentOrange
        "Zakonczone" -> MaterialTheme.colorScheme.onSurfaceVariant // Dodano dla archiwum
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.WorkOutline, contentDescription = "Zlecenie", tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = zlecenie.opis ?: "Brak opisu",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                // W widoku admina pokaż status
                if (isAdminView) {
                    Text(
                        text = "Status: $statusText",
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = "Szczegóły", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Element listy "Moje Zlecenia" dla widoku Seniora.
 * Pokazuje status, dane wolontariusza (jeśli jest) oraz przycisk do potwierdzenia.
 *
 * @param zlecenie Pełne dane zlecenia dla Seniora.
 * @param onPotwierdz Akcja wywoływana po naciśnięciu "POTWIERDŹ WYKONANIE".
 * @param isLoading Czy trwa operacja (np. potwierdzanie).
 */
@Composable
fun ZlecenieItemSenior(
    zlecenie: PelneZlecenieDlaSeniora,
    onPotwierdz: () -> Unit,
    isLoading: Boolean
) {
    val context = LocalContext.current
    val statusColor = when (zlecenie.status) {
        "Wolne" -> MaterialTheme.colorScheme.primary
        "Aktywne" -> AccentGreen
        "DoPotwierdzenia" -> AccentOrange
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        // Specjalna ramka dla zleceń oczekujących na potwierdzenie
        border = if (zlecenie.status == "DoPotwierdzenia") BorderStroke(2.dp, AccentOrange) else null
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = zlecenie.opis,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            Text("Status:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = when(zlecenie.status) {
                    "Wolne" -> "OCZEKUJE NA WOLONTARIUSZA"
                    "Aktywne" -> "W TRAKCIE REALIZACJI"
                    "DoPotwierdzenia" -> "OCZEKUJE NA TWOJE POTWIERDZENIE"
                    else -> zlecenie.status.uppercase()
                },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )

            // Pokaż dane wolontariusza, jeśli jest przypisany
            if (zlecenie.status != "Wolne") {
                Spacer(Modifier.height(8.dp))
                Text("Przypisany Wolontariusz:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = zlecenie.wolontariuszImieNazwisko,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Tel: ${zlecenie.wolontariuszTelefon}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable {
                        // Uruchomienie dialera po kliknięciu numeru
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${zlecenie.wolontariuszTelefon}"))
                        context.startActivity(intent)
                    }
                )
            }

            // Pokaż przycisk potwierdzenia tylko jeśli status to "DoPotwierdzenia"
            if (zlecenie.status == "DoPotwierdzenia") {
                Spacer(Modifier.height(16.dp))
                GradientButton(
                    text = if(isLoading) "Zapisywanie..." else "POTWIERDŹ WYKONANIE",
                    onClick = onPotwierdz,
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen) // Zielony przycisk
                )
            }
        }
    }
}

/**
 * Element listy "Moje Zlecenia" dla widoku Wolontariusza.
 * Pokazuje dane seniora oraz przycisk do oznaczenia jako wykonane.
 *
 * @param zlecenie Pełne dane zlecenia dla Wolontariusza.
 * @param onZakoncz Akcja wywoływana po naciśnięciu "ZAKOŃCZ ZLECENIE".
 * @param isLoading Czy trwa operacja (np. kończenie).
 */
@Composable
fun ZlecenieItemWolontariusz(
    zlecenie: PelneZlecenieDlaWolontariusza,
    onZakoncz: () -> Unit,
    isLoading: Boolean
) {
    val context = LocalContext.current
    val isPending = zlecenie.status == "DoPotwierdzenia"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        // Ramka, jeśli oczekuje na potwierdzenie
        border = if (isPending) BorderStroke(2.dp, AccentOrange) else null
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = zlecenie.opis,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            Text("Senior:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = zlecenie.seniorImieNazwisko,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))
            Text("Adres:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = zlecenie.seniorAdres,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))
            Text("Telefon Seniora:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = zlecenie.seniorTelefon,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    // Uruchomienie dialera
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${zlecenie.seniorTelefon}"))
                    context.startActivity(intent)
                }
            )

            Spacer(Modifier.height(16.dp))
            GradientButton(
                text = if (isPending) "OCZEKUJE NA POTWIERDZENIE" else (if(isLoading) "Zapisywanie..." else "ZAKOŃCZ ZLECENIE"),
                onClick = onZakoncz,
                enabled = !isPending && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    // Przycisk jest zielony lub szary (jeśli oczekuje lub jest wyłączony)
                    containerColor = if(isPending) Color.Gray else AccentGreen,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
        }
    }
}

/**
 * Komponent UI reprezentujący pojedyncze wydarzenie na liście w kalendarzu.
 * Zawiera przycisk do usunięcia wydarzenia.
 *
 * @param event Dane wydarzenia do wyświetlenia.
 * @param onDelete Funkcja wywoływana po naciśnięciu ikony usunięcia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(event: CalendarEvent, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(
            Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Alarm, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(event.time, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.width(16.dp))
            Text(event.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))

            // Przycisk usuwania
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Usuń", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
            }
        }
    }
}

/**
 * Dialog do dodawania nowego wydarzenia do kalendarza.
 * Używa niestandardowego [TimePickerDialog] do wyboru godziny.
 *
 * @param onDismiss Funkcja wywoływana przy zamknięciu dialogu.
 * @param onSave Funkcja wywoływana po naciśnięciu "Zapisz", przekazująca (time, title).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(is24Hour = true)

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                // Formatowanie czasu do HH:mm
                time = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            },
            state = timePickerState
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj nowe zadanie") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Pole godziny (blokuje edycję, otwiera picker po kliknięciu)
                Box {
                    CustomTextField(
                        value = time,
                        onValueChange = { },
                        label = "Godzina (np. 14:30)",
                        enabled = false // Zablokowane do ręcznej edycji
                    )
                    // Niewidzialny Box przechwytujący kliknięcia
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // Bez efektu "ripple"
                            ) { showTimePicker = true }
                    )
                }
                // Pole tytułu
                CustomTextField(value = title, onValueChange = { title = it }, label = "Tytuł zadania (np. 'Ważna wizyta')")
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank() && time.isNotBlank()) onSave(time, title) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = title.isNotBlank() && time.isNotBlank() // Wymagaj obu pól
            ) {
                Text("Zapisz", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Anuluj", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
    )
}

/**
 * Niestandardowy kontener [AlertDialog] dla komponentu [TimePicker] Material 3.
 * Zapewnia standardowe przyciski "Anuluj" i "Zatwierdź".
 *
 * @param onDismiss Funkcja wywoływana przy zamknięciu dialogu.
 * @param onConfirm Funkcja wywoływana po zatwierdzeniu wyboru godziny.
 * @param state Stan [TimePickerState] zarządzający wybraną godziną.
 * @param title Tytuł dialogu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    state: TimePickerState,
    title: String = "Wybierz godzinę"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false), // Użyj mniejszej, niestandardowej szerokości
        modifier = Modifier.width(IntrinsicSize.Min) // Dopasuj szerokość do zawartości
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(16.dp))
            TimePicker(state = state) // Komponent M3 do wyboru godziny
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismiss) { Text("Anuluj") }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = onConfirm) { Text("Zatwierdź") }
            }
        }
    }
}

/**
 * Dialog wyświetlany podczas 15-sekundowego odliczania po wykryciu upadku.
 * Jest modalny i nie można go zamknąć klikając obok (onDismissRequest = {}).
 *
 * @param secondsLeft Liczba sekund pozostałych do alarmu.
 * @param onCancel Funkcja wywoływana po naciśnięciu przycisku "ANULUJ".
 */
@Composable
fun FallCountdownDialog(
    secondsLeft: Int,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = { /* Celowo puste, aby zablokować zamknięcie */ }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(Icons.Default.Warning, "Alarm", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(64.dp))
                Text(
                    "Wykryto możliwy upadek!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Alarm zostanie uruchomiony za:",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    "$secondsLeft",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    "Jeśli wszystko w porządku, naciśnij 'Anuluj'.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("ANULUJ", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Komponent UI reprezentujący pojedynczy element Seniorm2 (zadanie cykliczne) na liście.
 *
 * @param item Dane zadania do wyświetlenia.
 * @param onClick Funkcja wywoływana po kliknięciu na element (przejście do edycji).
 * @param onDelete Funkcja wywoływana po naciśnięciu ikony usunięcia.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Seniorm2DisplayItem(
    item: Seniorm2Item,
    onClick: () -> Unit, // Do edycji
    onDelete: () -> Unit
) {
    // Funkcja pomocnicza do formatowania zbioru dni tygodnia na czytelny tekst
    val daysFormatter: (Set<DayOfWeek>) -> String = { days ->
        if (days.size == 7) "Codziennie"
        else if (days.isEmpty()) "Nigdy"
        else {
            days.sorted() // Sortuj dni (Pon, Wt, Śr...)
                .joinToString(", ") { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Cały kafelek klikalny
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.time,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = daysFormatter(item.daysOfWeek),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Usuń",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Dialog służący do dodawania nowego lub edytowania istniejącego zadania Seniorm2.
 *
 * @param itemToEdit Dane zadania do edycji. Jeśli tworzysz nowe, przekaż
 * `Seniorm2Item(id="TEMP_ID", name = "", time = "08:00", daysOfWeek = emptySet())`.
 * @param onDismiss Funkcja wywoływana przy zamknięciu dialogu.
 * @param onSave Funkcja wywoływana po naciśnięciu "Zapisz", przekazująca zaktualizowany [Seniorm2Item].
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class) // FlowRow jest Experimental
@Composable
fun AddSeniorm2Dialog(
    itemToEdit: Seniorm2Item,
    onDismiss: () -> Unit,
    onSave: (Seniorm2Item) -> Unit
) {
    // Stany dla pól formularza, inicjalizowane danymi z `itemToEdit`
    var name by remember { mutableStateOf(itemToEdit.name) }
    var time by remember { mutableStateOf(itemToEdit.time) }
    var selectedDays by remember { mutableStateOf(itemToEdit.daysOfWeek) }

    val isEditing = itemToEdit.name.isNotBlank() // Sprawdzenie, czy to tryb edycji

    // Logika dla TimePickera
    var showTimePicker by remember { mutableStateOf(false) }
    val initialTime = try { LocalTime.parse(time) } catch (e: Exception) { LocalTime.now() }
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                time = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            },
            state = timePickerState
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edytuj zadanie" else "Dodaj zadanie") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nazwa (np. 'Leki poranne')"
                )

                // Pole wyboru godziny (tak jak w AddEventDialog)
                Box {
                    CustomTextField(
                        value = time,
                        onValueChange = { },
                        label = "Godzina",
                        enabled = false
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                showTimePicker = true
                            }
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text("Powtarzaj w:", style = MaterialTheme.typography.labelLarge)

                // Komponent FlowRow (z ExperimentalLayoutApi) automatycznie zawija
                // elementy do nowej linii, jeśli się nie mieszczą.
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    maxItemsInEachRow = 7
                ) {
                    val days = DayOfWeek.values().sortedBy { it.value } // Posortowane od poniedziałku

                    days.forEach { day ->
                        FilterChip(
                            modifier = Modifier.padding(horizontal = 2.dp),
                            selected = selectedDays.contains(day),
                            onClick = {
                                // Logika dodawania/usuwania dnia ze zbioru
                                selectedDays = if (selectedDays.contains(day)) {
                                    selectedDays - day
                                } else {
                                    selectedDays + day
                                }
                            },
                            label = { Text(day.getDisplayName(TextStyle.SHORT, Locale.getDefault())) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Tworzy kopię obiektu z nowymi danymi i przekazuje do zapisu
                    // WAŻNE: zachowujemy oryginalny ID (z Firestore lub tymczasowy)
                    val newItem = itemToEdit.copy(name = name, time = time, daysOfWeek = selectedDays)
                    onSave(newItem)
                },
                // Wymagaj wypełnienia wszystkich pól
                enabled = name.isNotBlank() && time.isNotBlank() && selectedDays.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Zapisz", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Anuluj", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
    )
}
// ########## KONIEC KOMPONENTÓW UI ##########


// ########## EKRANY APLIKACJI ##########

/**
 * Ekran Onboardingu (wprowadzenia) z trzema stronami.
 * Używa [HorizontalPager] do przewijania stron.
 * Zapisuje w [SharedPreferences], że użytkownik widział już onboarding.
 *
 * @param navController Kontroler nawigacji do przejścia do logowania.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    // Zapisz, że onboarding został wyświetlony
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val sharedPrefs = context.getSharedPreferences("OnboardingPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("hasSeenOnboarding", true).apply()
    }

    // Funkcja nawigująca do logowania i usuwająca Onboarding ze stosu
    fun navigateToLogin() {
        navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.ONBOARDING) { inclusive = true }
        }
    }

    Scaffold(
        bottomBar = {
            // Dolny pasek z przyciskami "Pomiń", "Dalej" i wskaźnikiem strony
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentPage = pagerState.currentPage
                val isLastPage = currentPage == pagerState.pageCount - 1

                // Przycisk "Pomiń" (widoczny na wszystkich stronach oprócz ostatniej)
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (!isLastPage) {
                        TextButton(onClick = { navigateToLogin() }) {
                            Text(
                                "Pomiń",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                // Wskaźnik kropek
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (currentPage == iteration)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                // Przycisk "Dalej" / "Zacznij"
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    val buttonText = if (isLastPage) "Zacznij" else "Dalej"

                    TextButton(onClick = {
                        if (isLastPage) {
                            navigateToLogin()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(currentPage + 1)
                            }
                        }
                    }) {
                        Text(
                            buttonText,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            // Definicja zawartości dla każdej strony
            when (page) {
                0 -> OnboardingPage("Witaj w Opieka24 Senior", "Aplikacja do łatwego proszenia o pomoc...", icon = Icons.Default.FavoriteBorder)
                1 -> OnboardingPage("Zalety Opieka24 Senior", "Bezpieczeństwo, Weryfikacja, Łatwa obsługa, Darmowa pomoc.", icon = Icons.Default.Shield)
                2 -> OnboardingPage("Wszystko gotowe!", "Sprawdź jakie to proste!", icon = Icons.Default.Celebration, isFinalPage = true, onStartClick = { navigateToLogin() })
            }
        }
    }
}

/**
 * Bezstanowy komponent UI dla pojedynczej strony Onboardingu.
 *
 * @param title Główny tytuł.
 * @param description Opis.
 * @param icon Ikona strony.
 * @param isFinalPage Czy to ostatnia strona (wyświetla przycisk "ZACZYNAMY").
 * @param onStartClick Akcja dla przycisku na ostatniej stronie.
 */
@Composable
fun OnboardingPage(title: String, description: String, icon: ImageVector, isFinalPage: Boolean = false, onStartClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, title, Modifier.size(200.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(48.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text(description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        if (isFinalPage) {
            Spacer(Modifier.height(48.dp))
            GradientButton(text = "ZACZYNAMY", onClick = onStartClick)
        }
    }
}

/**
 * Ekran logowania użytkownika.
 * Obsługuje stany ładowania, sukcesu (nawigacja) i błędu (Snackbar)
 * na podstawie [authState] z [AuthViewModel].
 *
 * @param navController Kontroler nawigacji.
 * @param authViewModel ViewModel zarządzający logiką uwierzytelniania.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Efekt reagujący na zmiany stanu uwierzytelniania
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.LoginSuccess -> {
                // Nawigacja do odpowiedniego panelu w zależności od roli
                val destination = when (state.role) {
                    "Senior" -> AppRoutes.SENIOR_WELCOME
                    "Wolontariusz" -> AppRoutes.WOLONTARIUSZ_WELCOME
                    "Admin" -> AppRoutes.ADMIN_WELCOME
                    else -> AppRoutes.LOGIN // Domyślnie wróć do logowania (choć nie powinno się zdarzyć)
                }

                navController.navigate(destination) {
                    popUpTo(AppRoutes.LOGIN) { inclusive = true } // Wyczyść stos nawigacji
                }
                authViewModel.resetAuthState()
            }
            is AuthState.LoginError -> {
                // Pokaż błąd w Snackbarze
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                    authViewModel.resetAuthState()
                }
            }
            else -> {
                // Stan Idle lub Loading
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    // Pokaż strzałkę "wstecz" tylko jeśli przyszliśmy z ekranu rejestracji
                    if (navController.previousBackStackEntry?.destination?.route == AppRoutes.REGISTER) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wstecz")
                        }
                    }
                })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGradient) // Tło z gradientem
                .padding(paddingValues)
        ) {
            val isLoading = authState is AuthState.Loading

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(Modifier.height(16.dp))

                Text("Witaj ponownie!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Zaloguj się, aby kontynuować.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(24.dp)) {
                        CustomTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "NUMER TELEFONU",
                            keyboardType = KeyboardType.Phone,
                            enabled = !isLoading
                        )
                        Spacer(Modifier.height(16.dp))
                        CustomPasswordTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "HASŁO",
                            enabled = !isLoading
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                GradientButton(
                    text = if (isLoading) "Logowanie..." else "ZALOGUJ SIĘ",
                    enabled = !isLoading && phoneNumber.isNotBlank() && password.isNotBlank(),
                    onClick = {
                        authViewModel.loginUser(phoneNumber, password)
                    }
                )

                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                    Text("Nie masz konta? ", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Zarejestruj się.",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate(AppRoutes.REGISTER) },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Ekran rejestracji nowego użytkownika (Seniora lub Wolontariusza).
 * Używa [adminUiState] z [AuthViewModel] do obsługi procesu dodawania użytkownika.
 *
 * @param navController Kontroler nawigacji.
 * @param authViewModel ViewModel zarządzający logiką użytkowników.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var imie by remember { mutableStateOf("") }
    var nazwisko by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var adres by remember { mutableStateOf("") }
    var haslo by remember { mutableStateOf("") }
    var rola by remember { mutableStateOf("Senior") } // Domyślna rola

    val adminState by authViewModel.adminUiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Efekt reagujący na stan operacji dodawania użytkownika
    LaunchedEffect(adminState) {
        when (val state = adminState) {
            is AdminUiState.Success -> {
                // Sukces: pokaż wiadomość i wróć do logowania
                scope.launch {
                    snackbarHostState.showSnackbar(state.message + " Możesz się teraz zalogować.")
                }
                authViewModel.resetAdminState()
                navController.popBackStack() // Wróć do LoginScreen
            }
            is AdminUiState.Error -> {
                // Błąd: pokaż wiadomość i pozwól na poprawę danych
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
                authViewModel.resetAdminState()
            }
            else -> {
                // Stan Idle lub Loading
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wstecz") } }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGradient)
                .padding(paddingValues)
        ) {
            val isLoading = adminState is AdminUiState.Loading

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Rejestracja",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(Modifier.height(16.dp))

                Text("Utwórz konto", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Wypełnij formularz, aby dołączyć.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(24.dp)) {
                        // Wybór roli
                        Text("Rejestruję się jako:", style = MaterialTheme.typography.bodyLarge)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            RadioButton(selected = rola == "Senior", onClick = { rola = "Senior" }, enabled = !isLoading)
                            Text("Senior", Modifier.align(Alignment.CenterVertically))
                            Spacer(Modifier.width(16.dp))
                            RadioButton(selected = rola == "Wolontariusz", onClick = { rola = "Wolontariusz" }, enabled = !isLoading)
                            Text("Wolontariusz", Modifier.align(Alignment.CenterVertically))
                        }
                        Spacer(Modifier.height(16.dp))

                        // Pola formularza
                        CustomTextField(imie, { imie = it }, "IMIĘ", enabled = !isLoading)
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(nazwisko, { nazwisko = it }, "NAZWISKO", enabled = !isLoading)
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(telefon, { telefon = it }, "NUMER TELEFONU", KeyboardType.Phone, enabled = !isLoading)
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(adres, { adres = it }, "ADRES", enabled = !isLoading)
                        Spacer(Modifier.height(8.dp))
                        CustomPasswordTextField(haslo, { haslo = it }, "HASŁO", enabled = !isLoading)
                    }
                }

                Spacer(Modifier.height(24.dp))

                val allFieldsFilled = imie.isNotBlank() && nazwisko.isNotBlank() && telefon.isNotBlank() && adres.isNotBlank() && haslo.isNotBlank()

                GradientButton(
                    text = if (isLoading) "Zapisywanie..." else "ZAREJESTRUJ SIĘ",
                    enabled = allFieldsFilled && !isLoading,
                    onClick = {
                        authViewModel.addUser(imie, nazwisko, telefon, adres, haslo, rola)
                    }
                )
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                    Text("Masz już konto? ", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Zaloguj się.",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.popBackStack() }, // Wróć do logowania
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Główny ekran (panel nawigacyjny) dla roli Seniora.
 * Używa [MetroTile] do nawigacji do różnych funkcji.
 *
 * @param navController Kontroler nawigacji.
 * @param authViewModel ViewModel do obsługi wylogowania.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorWelcomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Opieka24 Senior - Panel Seniora") },
                actions = {
                    // Przycisk wylogowania
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.SENIOR_WELCOME) { inclusive = true } // Wyczyść stos nawigacji
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Wyloguj")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Cześć!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Text("W czym możemy pomóc?", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))

            // Kafelki nawigacyjne
            MetroTile(
                title = "Potrzebujesz pomocy?",
                description = "Poproś o wsparcie w zakupach, transporcie lub naprawach.",
                icon = Icons.Default.SupportAgent,
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(AppRoutes.SENIOR_DASHBOARD) },
                aspectRatio = 2f
            )

            MetroTile(
                title = "Moje Zlecenia",
                description = "Sprawdź status swoich aktywnych zleceń.",
                icon = Icons.Default.ListAlt,
                backgroundColor = AccentGreen,
                onClick = { navController.navigate(AppRoutes.SENIOR_MOJE_ZLECENIA) },
                aspectRatio = 2f
            )

            MetroTile(
                title = "Mój Organizer",
                description = "Nie zapomnij o wizycie. Zapisz to!",
                icon = Icons.Default.EventAvailable,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                onClick = { navController.navigate(AppRoutes.SENIOR_CALENDAR) },
                aspectRatio = 2f
            )
            MetroTile(
                title = "Moje Zadania Cykliczne",
                description = "Ustaw przypomnienia o regularnych czynnościach.",
                icon = Icons.Default.CheckCircle,
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = { navController.navigate(AppRoutes.SENIORM2_ITEMS) },
                aspectRatio = 2f
            )
            MetroTile(
                title = "Inteligentny Asystent",
                description = "Napisz, czego potrzebujesz, a my dodamy to do kalendarza.",
                icon = Icons.Default.AutoAwesome,
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                onClick = { navController.navigate(AppRoutes.ASSISTANT) },
                aspectRatio = 2f
            )

            // Rząd z mniejszymi kafelkami (Profil i Ustawienia)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetroTile(
                    title = "Mój Profil",
                    icon = Icons.Default.Person,
                    backgroundColor = AccentGreen.copy(alpha = 0.7f),
                    onClick = { navController.navigate(AppRoutes.PROFILE) },
                    modifier = Modifier.weight(1f),
                    aspectRatio = 1f // Kwadrat
                )
                MetroTile(
                    title = "Ustawienia",
                    icon = Icons.Default.Settings,
                    backgroundColor = AccentBlue,
                    onClick = { navController.navigate(AppRoutes.SETTINGS) },
                    modifier = Modifier.weight(1f),
                    aspectRatio = 1f // Kwadrat
                )
            }
        }
    }
}

/**
 * Główny ekran (panel nawigacyjny) dla roli Wolontariusza.
 *
 * @param navController Kontroler nawigacji.
 * @param authViewModel ViewModel do obsługi wylogowania.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WolontariuszWelcomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Wolontariusza") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.WOLONTARIUSZ_WELCOME) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Wyloguj")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Witaj, Wolontariuszu!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Text("Dziękujemy, że pomagasz.", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))

            // Kafelki nawigacyjne
            MetroTile(
                title = "Lista Publicznych Zleceń",
                description = "Zobacz, kto potrzebuje Twojej pomocy.",
                icon = Icons.Default.PlaylistAddCheck,
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(AppRoutes.HELPER_DASHBOARD) },
                aspectRatio = 2f
            )

            MetroTile(
                title = "Moje Zlecenia",
                description = "Zobacz zlecenia, które przyjąłeś.",
                icon = Icons.Default.AssignmentInd,
                backgroundColor = AccentGreen,
                onClick = { navController.navigate(AppRoutes.WOLONTARIUSZ_MOJE_ZLECENIA) },
                aspectRatio = 2f
            )

            MetroTile(
                title = "Mój Organizer",
                description = "Zaplanuj swoje zadania i wizyty.",
                icon = Icons.Default.EventAvailable,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                onClick = { navController.navigate(AppRoutes.SENIOR_CALENDAR) },
                aspectRatio = 2f
            )

            // Rząd z mniejszymi kafelkami (Profil i Ustawienia)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetroTile(
                    title = "Mój Profil",
                    icon = Icons.Default.Person,
                    backgroundColor = AccentGreen.copy(alpha = 0.7f),
                    onClick = { navController.navigate(AppRoutes.PROFILE) },
                    modifier = Modifier.weight(1f),
                    aspectRatio = 1f
                )
                MetroTile(
                    title = "Ustawienia",
                    icon = Icons.Default.Settings,
                    backgroundColor = AccentBlue,
                    onClick = { navController.navigate(AppRoutes.SETTINGS) },
                    modifier = Modifier.weight(1f),
                    aspectRatio = 1f
                )
            }
        }
    }
}

/**
 * Główny ekran (panel nawigacyjny) dla roli Administratora.
 *
 * @param navController Kontroler nawigacji.
 * @param authViewModel ViewModel do obsługi wylogowania.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWelcomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Administratora") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.ADMIN_WELCOME) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Wyloguj")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Panel Zarządzania", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))

            // Kafelki nawigacyjne
            MetroTile(
                title = "Zarządzaj Seniorami",
                description = "Dodaj lub usuń konta seniorów.",
                icon = Icons.Default.PersonSearch,
                backgroundColor = AdminPanelColor,
                onClick = {
                    // Nawigacja z argumentem
                    navController.navigate("${AppRoutes.ADMIN_MANAGE_USERS_ROUTE}/Senior")
                },
                aspectRatio = 1.5f
            )

            MetroTile(
                title = "Zarządzaj Wolontariuszami",
                description = "Dodaj lub usuń konta wolontariuszy.",
                icon = Icons.Default.VolunteerActivism,
                backgroundColor = AdminPanelColor.copy(alpha = 0.8f),
                onClick = {
                    // Nawigacja z argumentem
                    navController.navigate("${AppRoutes.ADMIN_MANAGE_USERS_ROUTE}/Wolontariusz")
                },
                aspectRatio = 1.5f
            )

            MetroTile(
                title = "Przeglądaj Wszystkie Zlecenia",
                description = "Monitoruj wszystkie aktywne i zajęte zlecenia.",
                icon = Icons.Default.PlaylistAddCheck,
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                onClick = { navController.navigate(AppRoutes.ADMIN_WSZYSTKIE_ZLECENIA) },
                aspectRatio = 2f
            )

            // === MODYFIKACJA (Zadanie 1: Archiwum Admina) ===
            MetroTile(
                title = "Archiwum Zleceń",
                description = "Przeglądaj wszystkie zakończone zadania.",
                icon = Icons.Default.Inventory,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                onClick = { navController.navigate(AppRoutes.ADMIN_ARCHIWUM) },
                aspectRatio = 2f
            )
        }
    }
}

/**
 * Ekran "Poproś o Pomoc" dla Seniora.
 * Wyświetla duży przycisk S.O.S. oraz listę standardowych usług (np. Zakupy).
 *
 * @param navController Kontroler nawigacji.
 * @param helpRequestViewModel ViewModel do tworzenia zleceń.
 * @param userProfileViewModel ViewModel (obecnie nieużywany, ale wstrzyknięty).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorDashboardScreen(
    navController: NavController,
    helpRequestViewModel: HelpRequestViewModel,
    userProfileViewModel: UserProfileViewModel
) {
    var showAddRequestDialog by remember { mutableStateOf(false) }
    var selectedServiceTitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Poproś o Pomoc") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } }
            )
        }
    ) { paddingValues ->
        Column(Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            // Przycisk S.O.S.
            Button(
                onClick = { navController.navigate(AppRoutes.SOS_EMERGENCY) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SosRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(100.dp)
            ) {
                Icon(Icons.Default.Sos, "S.O.S.", modifier = Modifier.size(48.dp), tint = Color.White)
                Spacer(Modifier.width(16.dp))
                Text("S.O.S.", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            }
            Text("Poproś o pomoc:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
            // Lista standardowych usług
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mockServices) { service ->
                    ServiceItem(
                        service = service,
                        onClick = {
                            selectedServiceTitle = service.title
                            showAddRequestDialog = true
                        }
                    )
                }
            }
        }
    }

    // Dialog dodawania zlecenia
    if (showAddRequestDialog) {
        AddHelpRequestDialog(
            serviceTitle = selectedServiceTitle,
            onDismiss = { showAddRequestDialog = false },
            helpRequestViewModel = helpRequestViewModel
        )
    }
}

/**
 * Ekran dla Wolontariusza, wyświetlający listę publicznych, "Wolnych" zleceń.
 *
 * @param navController Kontroler nawigacji.
 * @param helpRequestViewModel ViewModel do pobierania listy zleceń.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelperDashboardScreen(
    navController: NavController,
    helpRequestViewModel: HelpRequestViewModel
) {
    val uiState by helpRequestViewModel.zleceniaListUiState.collectAsState()

    // Odśwież listę, gdy ekran staje się widoczny
    LaunchedEffect(Unit) {
        helpRequestViewModel.fetchPublicZlecenia()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista Publicznych Zleceń") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } },
                // --- DODANY BLOK ---
                actions = {
                    IconButton(onClick = {
                        // Wymuś ponowne załadowanie zleceń publicznych
                        helpRequestViewModel.fetchPublicZlecenia()
                    }) {
                        Icon(Icons.Default.Refresh, "Odśwież")
                    }
                }
                // --- KONIEC BLOKU ---
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (val state = uiState) {
                is ZleceniaListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ZleceniaListUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp), textAlign = TextAlign.Center)
                }
                is ZleceniaListUiState.Success -> {
                    // Filtruj listę, aby upewnić się, że zawiera tylko oczekiwany typ
                    val zlecenia = state.zlecenia.filterIsInstance<ZlecenieRef>()
                    if (zlecenia.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Brak nowych zleceń.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.titleSmall)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(zlecenia) { zlecenie ->
                                ZlecenieItemPublic(
                                    zlecenie = zlecenie,
                                    isAdminView = false, // To nie jest widok admina
                                    onClick = {
                                        // Nawiguj do szczegółów zlecenia
                                        navController.navigate("${AppRoutes.ZLECENIE_DETAILS_ROUTE}/${zlecenie.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Ekran dla Administratora, wyświetlający listę *wszystkich* aktywnych zleceń
 * (Wolne, Aktywne, DoPotwierdzenia).
 *
 * @param navController Kontroler nawigacji.
 * @param helpRequestViewModel ViewModel do pobierania listy zleceń.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWszystkieZleceniaScreen(
    navController: NavController,
    helpRequestViewModel: HelpRequestViewModel
) {
    val uiState by helpRequestViewModel.zleceniaListUiState.collectAsState()

    // Pobierz WSZYSTKIE zlecenia (zamiast tylko publicznych)
    LaunchedEffect(Unit) {
        helpRequestViewModel.fetchAllZlecenia()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wszystkie Zlecenia") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (val state = uiState) {
                is ZleceniaListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ZleceniaListUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp), textAlign = TextAlign.Center)
                }
                is ZleceniaListUiState.Success -> {
                    val zlecenia = state.zlecenia.filterIsInstance<ZlecenieRef>()
                    if (zlecenia.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Brak jakichkolwiek zleceń w systemie.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.titleSmall)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(zlecenia) { zlecenie ->
                                ZlecenieItemPublic(
                                    zlecenie = zlecenie,
                                    isAdminView = true, // Włącz widok admina (pokazuje status)
                                    onClick = {
                                        // Admin też może chcieć zobaczyć szczegóły
                                        navController.navigate("${AppRoutes.ZLECENIE_DETAILS_ROUTE}/${zlecenie.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// === MODYFIKACJA (Zadanie 1: Archiwum Admina) ===
/**
 * Ekran dla Administratora, wyświetlający archiwum zakończonych zleceň.
 * (NOWY EKRAN - ZADANIE 1)
 *
 * @param navController Kontroler nawigacji.
 * @param helpRequestViewModel ViewModel do pobierania listy zleceń.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminArchiwumScreen(
    navController: NavController,
    helpRequestViewModel: HelpRequestViewModel
) {
    val uiState by helpRequestViewModel.zleceniaListUiState.collectAsState()

    // Pobierz WSZYSTKIE zakończone zlecenia
    LaunchedEffect(Unit) {
        helpRequestViewModel.fetchArchiwalneZlecenia()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Archiwum Zleceń") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (val state = uiState) {
                is ZleceniaListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ZleceniaListUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp), textAlign = TextAlign.Center)
                }
                is ZleceniaListUiState.Success -> {
                    val zlecenia = state.zlecenia.filterIsInstance<ZlecenieRef>()
                    if (zlecenia.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Brak zakończonych zleceń w archiwum.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.titleSmall)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(zlecenia) { zlecenie ->
                                ZlecenieItemPublic(
                                    zlecenie = zlecenie,
                                    isAdminView = true, // Pokaż status (będzie to "Zakonczone")
                                    onClick = {
                                        // Admin może chcieć zobaczyć szczegóły, nawet archiwalne
                                        navController.navigate("${AppRoutes.ZLECENIE_DETAILS_ROUTE}/${zlecenie.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Ekran szczegółów zlecenia, widoczny dla Wolontariusza przed jego podjęciem.
 * Pozwala na podjęcie zlecenia, sprawdzając limit (logika w ViewModelu).
 *
 * @param navController Kontroler nawigacji.
 * @param helpRequestViewModel ViewModel do pobierania szczegółów i podejmowania zlecenia.
 * @param zlecenieId ID zlecenia pobrane z trasy nawigacji.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZlecenieDetailsScreen(
    navController: NavController,
    helpRequestViewModel: HelpRequestViewModel,
    zlecenieId: String
) {
    // Stan szczegółów zlecenia
    val uiState by helpRequestViewModel.zlecenieDetailsUiState.collectAsState()
    // Stan akcji (podejmowania zlecenia)
    val actionState by helpRequestViewModel.actionUiState.collectAsState()

    val context = LocalContext.current

    // Pobierz szczegóły zlecenia przy wejściu na ekran
    LaunchedEffect(key1 = zlecenieId) {
        helpRequestViewModel.pobierzPelneDaneZlecenia(zlecenieId)
    }

    // Efekt reagujący na wynik akcji podejmowania zlecenia
    LaunchedEffect(actionState) {
        when (val state = actionState) {
            is ZlecenieActionUiState.Success -> {
                // Sukces: pokaż Toast, zresetuj stan i wróć do listy
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                helpRequestViewModel.resetActionState()
                helpRequestViewModel.fetchPublicZlecenia() // Odśwież listę publiczną
                navController.popBackStack()
            }
            is ZlecenieActionUiState.Error -> {
                // Błąd: pokaż Toast i zresetuj stan (zostań na ekranie)
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                helpRequestViewModel.resetActionState()
            }
            else -> {
                // Stan Idle lub Loading
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły Zlecenia") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)) {
            when (val state = uiState) {
                is ZlecenieDetailsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ZlecenieDetailsUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center)
                }
                is ZlecenieDetailsUiState.Success -> {
                    val zlecenie = state.pelneZlecenie
                    val isWolne = zlecenie.status == "Wolne"

                    // Stan ładowania pobierany z actionState
                    val isLoading = actionState is ZlecenieActionUiState.Loading

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Szczegóły Zlecenia", style = MaterialTheme.typography.headlineSmall)

                                Text("Status:", fontWeight = FontWeight.Bold)
                                Text(
                                    text = zlecenie.status.uppercase(),
                                    color = when (zlecenie.status) {
                                        "Wolne" -> MaterialTheme.colorScheme.primary
                                        "Aktywne" -> AccentGreen
                                        "DoPotwierdzenia" -> AccentOrange
                                        else -> MaterialTheme.colorScheme.error
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                                HorizontalDivider()

                                Text("Opis Zlecenia:", fontWeight = FontWeight.Bold)
                                Text(zlecenie.opisZlecenia)
                                HorizontalDivider()

                                Text("Senior (zleceniodawca):", fontWeight = FontWeight.Bold)
                                Text("Imię i Nazwisko: ${zlecenie.seniorImieNazwisko}")
                                Text("Adres: ${zlecenie.seniorAdres}")
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Przycisk jest widoczny tylko dla statusu "Wolne"
                        if (isWolne) {
                            GradientButton(
                                text = if (isLoading) "Przejmowanie..." else "PODEJMIJ ZLECENIE",
                                enabled = !isLoading,
                                onClick = {
                                    helpRequestViewModel.podejmijZlecenie(zlecenie.zlecenieId)
                                }
                            )
                        } else {
                            // Pokaż informację, jeśli zlecenie nie jest wolne
                            Text(
                                "To zlecenie nie jest już dostępne do podjęcia.",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Ekran "Moje Zlecenia" dla Seniora.
 * Wyświetla listę zleceń utworzonych przez zalogowanego seniora.
 * Umożliwia seniorowi potwierdzenie wykonania zlecenia, gdy zmieni ono status na "DoPotwierdzenia".
 *
 * @param navController Kontroler nawigacji.
 * @param helpRequestViewModel ViewModel do pobierania listy i potwierdzania zleceń.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorMojeZleceniaScreen(
    navController: NavController,
    helpRequestViewModel: HelpRequestViewModel
) {
    val listUiState by helpRequestViewModel.zleceniaListUiState.collectAsState()
    val actionUiState by helpRequestViewModel.actionUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    // Przechowuje ID zlecenia, które jest aktualnie przetwarzane (potwierdzane)
    var loadingZlecenieId by remember { mutableStateOf<String?>(null) }

    // Pobierz zlecenia seniora przy wejściu na ekran
    LaunchedEffect(Unit) {
        helpRequestViewModel.fetchMojeZleceniaDlaSeniora()
    }

    // Obserwuj wynik akcji (potwierdzenia) i pokaż Snackbar
    LaunchedEffect(actionUiState) {
        when (val state = actionUiState) {
            is ZlecenieActionUiState.Success -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                loadingZlecenieId = null // Wyłącz wskaźnik ładowania
                helpRequestViewModel.resetActionState()
            }
            is ZlecenieActionUiState.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                loadingZlecenieId = null // Wyłącz wskaźnik ładowania
                helpRequestViewModel.resetActionState()
            }
            is ZlecenieActionUiState.Loading -> { /* loadingZlecenieId jest już ustawione */ }
            is ZlecenieActionUiState.Idle -> { loadingZlecenieId = null }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Moje Zlecenia") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } },
                // --- DODANY BLOK ---
                actions = {
                    IconButton(onClick = {
                        // Wymuś ponowne załadowanie zleceń Seniora
                        helpRequestViewModel.fetchMojeZleceniaDlaSeniora()
                    }) {
                        Icon(Icons.Default.Refresh, "Odśwież")
                    }
                }
                // --- KONIEC BLOKU ---
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (val state = listUiState) {
                is ZleceniaListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ZleceniaListUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp), textAlign = TextAlign.Center)
                }
                is ZleceniaListUiState.Success -> {
                    val zlecenia = state.zlecenia.filterIsInstance<PelneZlecenieDlaSeniora>()
                    if (zlecenia.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Nie masz jeszcze żadnych zleceń.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(zlecenia) { zlecenie ->
                                ZlecenieItemSenior(
                                    zlecenie = zlecenie,
                                    // Pokaż ładowanie tylko dla klikniętego elementu
                                    isLoading = (loadingZlecenieId == zlecenie.zlecenieId),
                                    onPotwierdz = {
                                        loadingZlecenieId = zlecenie.zlecenieId
                                        helpRequestViewModel.potwierdzWykonanie(zlecenie.zlecenieId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Ekran "Moje Zlecenia" dla Wolontariusza.
 * Wyświetla listę zleceń przyjętych przez zalogowanego wolontariusza.
 * Umożliwia oznaczenie zlecenia jako wykonane (co zmienia jego status na "DoPotwierdzenia").
 *
 * @param navController Kontroler nawigacji.
 * @param helpRequestViewModel ViewModel do pobierania listy i oznaczania zleceń.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WolontariuszMojeZleceniaScreen(
    navController: NavController,
    helpRequestViewModel: HelpRequestViewModel
) {
    val listUiState by helpRequestViewModel.zleceniaListUiState.collectAsState()
    val actionUiState by helpRequestViewModel.actionUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var loadingZlecenieId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        helpRequestViewModel.fetchMojeZleceniaDlaWolontariusza()
    }

    // Obserwuj wynik akcji (oznaczenia jako wykonane)
    LaunchedEffect(actionUiState) {
        when (val state = actionUiState) {
            is ZlecenieActionUiState.Success -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                loadingZlecenieId = null
                helpRequestViewModel.resetActionState()
            }
            is ZlecenieActionUiState.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                loadingZlecenieId = null
                helpRequestViewModel.resetActionState()
            }
            is ZlecenieActionUiState.Loading -> { /* loadingZlecenieId jest już ustawione */ }
            is ZlecenieActionUiState.Idle -> { loadingZlecenieId = null }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Moje Przyjęte Zlecenia") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } },
                // --- DODANY BLOK ---
                actions = {
                    IconButton(onClick = {
                        // Wymuś ponowne załadowanie zleceń Wolontariusza
                        helpRequestViewModel.fetchMojeZleceniaDlaWolontariusza()
                    }) {
                        Icon(Icons.Default.Refresh, "Odśwież")
                    }
                }
                // --- KONIEC BLOKU ---
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (val state = listUiState) {
                is ZleceniaListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ZleceniaListUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp), textAlign = TextAlign.Center)
                }
                is ZleceniaListUiState.Success -> {
                    val zlecenia = state.zlecenia.filterIsInstance<PelneZlecenieDlaWolontariusza>()
                    if (zlecenia.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Nie masz obecnie żadnych przyjętych zleceń.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(zlecenia) { zlecenie ->
                                ZlecenieItemWolontariusz(
                                    zlecenie = zlecenie,
                                    isLoading = (loadingZlecenieId == zlecenie.zlecenieId),
                                    onZakoncz = {
                                        loadingZlecenieId = zlecenie.zlecenieId
                                        helpRequestViewModel.oznaczJakoWykonane(zlecenie.zlecenieId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Ekran panelu administratora do zarządzania użytkownikami (Dodawanie/Usuwanie).
 * Ekran jest generyczny i działa dla "Senior" lub "Wolontariusz" na podstawie
 * przekazanego argumentu [userRole].
 *
 * @param navController Kontroler nawigacji.
 * @param authViewModel ViewModel do operacji dodawania/usuwania użytkowników.
 * @param userRole Rola zarządzanych użytkowników ("Senior" lub "Wolontariusz"),
 * przekazana z argumentu nawigacji.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManageUsersScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userRole: String
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val adminState by authViewModel.adminUiState.collectAsState()

    // Stany dla formularza "Dodaj"
    var imie by remember { mutableStateOf("") }
    var nazwisko by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var adres by remember { mutableStateOf("") }
    var haslo by remember { mutableStateOf("") }

    // Stany dla formularza "Usuń"
    var imieDel by remember { mutableStateOf("") }
    var nazwiskoDel by remember { mutableStateOf("") }
    var telefonDel by remember { mutableStateOf("") }

    val isLoading = adminState is AdminUiState.Loading

    // Obserwuj wynik operacji admina
    LaunchedEffect(adminState) {
        when (val state = adminState) {
            is AdminUiState.Success -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                authViewModel.resetAdminState()
                // Wyczyść formularze po sukcesie
                imie = ""; nazwisko = ""; telefon = ""; adres = ""; haslo = ""
                imieDel = ""; nazwiskoDel = ""; telefonDel = ""
            }
            is AdminUiState.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                authViewModel.resetAdminState()
            }
            else -> { /* Idle or Loading */ }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Zarządzaj: $userRole") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wróć") } }
            )
        }
    ) { paddingValues ->
        Column(Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            // Taby "Dodaj" / "Usuń"
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Dodaj $userRole") },
                    icon = { Icon(Icons.Default.Add, null) },
                    enabled = !isLoading
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Usuń $userRole") },
                    icon = { Icon(Icons.Default.Delete, null) },
                    enabled = !isLoading
                )
            }

            Column(Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())) {
                if (selectedTab == 0) {
                    // --- Zakładka "Dodaj" ---
                    Text("Dodaj nowego $userRole", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
                    CustomTextField(imie, { imie = it }, "IMIĘ", enabled = !isLoading)
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(nazwisko, { nazwisko = it }, "NAZWISKO", enabled = !isLoading)
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(telefon, { telefon = it }, "NUMER TELEFONU", KeyboardType.Phone, enabled = !isLoading)
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(adres, { adres = it }, "ADRES", enabled = !isLoading)
                    Spacer(Modifier.height(8.dp))
                    CustomPasswordTextField(haslo, { haslo = it }, "HASŁO", enabled = !isLoading)
                    Spacer(Modifier.height(24.dp))
                    val allFieldsFilled = imie.isNotBlank() && nazwisko.isNotBlank() && telefon.isNotBlank() && adres.isNotBlank() && haslo.isNotBlank()
                    GradientButton(
                        text = if (isLoading) "Zapisywanie..." else "DODAJ UŻYTKOWNIKA",
                        enabled = allFieldsFilled && !isLoading,
                        onClick = { authViewModel.addUser(imie, nazwisko, telefon, adres, haslo, userRole) }
                    )
                } else {
                    // --- Zakładka "Usuń" ---
                    Text("Usuń $userRole", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
                    Text("Aby potwierdzić usunięcie, podaj DOKŁADNIE dane użytkownika.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(16.dp))

                    CustomTextField(imieDel, { imieDel = it }, "IMIĘ", enabled = !isLoading)
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(nazwiskoDel, { nazwiskoDel = it }, "NAZWISKO", enabled = !isLoading)
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(telefonDel, { telefonDel = it }, "NUMER TELEFONU", KeyboardType.Phone, enabled = !isLoading)
                    Spacer(Modifier.height(24.dp))
                    val allDelFieldsFilled = imieDel.isNotBlank() && nazwiskoDel.isNotBlank() && telefonDel.isNotBlank()
                    GradientButton(
                        text = if (isLoading) "Usuwanie..." else "USUŃ UŻYTKOWNIKA",
                        enabled = allDelFieldsFilled && !isLoading,
                        onClick = { authViewModel.removeUserWithCleanup(imieDel, nazwiskoDel, telefonDel, userRole) }
                    )
                }
            }
        }
    }
}

/**
 * Główny ekran kalendarza ("Mój Organizer").
 * Wyświetla [DatePicker] oraz listę wydarzeń [EventItem] na wybrany dzień.
 * Umożliwia dodawanie i usuwanie wydarzeń poprzez [SeniorCalendarViewModel].
 *
 * @param navController Kontroler nawigacji.
 * @param calendarViewModel ViewModel zarządzający logiką kalendarza i wydarzeń.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorCalendarScreen(navController: NavController, calendarViewModel: SeniorCalendarViewModel) {
    val datePickerState = rememberDatePickerState()
    var showAddEventDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val events by calendarViewModel.events.collectAsState()
    val isLoading by calendarViewModel.isLoading.collectAsState()
    val error by calendarViewModel.error.collectAsState()

    // Przekształć milisekundy z DatePickerState na LocalDate
    val selectedDate = datePickerState.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() } ?: LocalDate.now()
    // Filtruj wydarzenia tylko dla wybranej daty
    val eventsForSelectedDate = events[selectedDate] ?: emptyList()

    // Załaduj wydarzenia przy pierwszym wejściu
    LaunchedEffect(Unit) {
        calendarViewModel.loadEvents()
    }

    // Pokaż błędy z ViewModelu w Snackbarze
    LaunchedEffect(error) {
        error?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            calendarViewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Mój Organizer") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wstecz") } }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddEventDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, "Dodaj zadanie", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Column(Modifier.fillMaxSize()) {
                // Komponent kalendarza (DatePicker)
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = null, // Ukryj domyślny tytuł
                    headline = null, // Ukryj domyślny nagłówek
                    showModeToggle = false, // Ukryj przełącznik trybu
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                        todayDateBorderColor = MaterialTheme.colorScheme.primary,
                        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                        todayContentColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(Modifier.height(16.dp))
                Text("Zadania na ${selectedDate.dayOfMonth}.${selectedDate.monthValue}.${selectedDate.year}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))

                // Lista wydarzeń
                if (eventsForSelectedDate.isEmpty() && !isLoading) {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("Brak zadań na ten dzień.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(eventsForSelectedDate) { event ->
                            EventItem(
                                event = event,
                                onDelete = {
                                    calendarViewModel.deleteEvent(event)
                                    scope.launch { snackbarHostState.showSnackbar("Usunięto wydarzenie") }
                                }
                            )
                        }
                    }
                }
            }
            // Wskaźnik ładowania na środku
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        // Dialog dodawania wydarzenia
        if (showAddEventDialog) {
            AddEventDialog(
                onDismiss = { showAddEventDialog = false },
                onSave = { time, title ->
                    calendarViewModel.addEvent(selectedDate, time, title)
                    showAddEventDialog = false
                    scope.launch { snackbarHostState.showSnackbar("Dodano i zapisano w chmurze!") }
                }
            )
        }
    }
}

/**
 * Ekran alarmowy S.O.S.
 * Uruchamiany ręcznie (z SeniorDashboard) lub automatycznie (przez FallDetector).
 * Umożliwia zadzwonienie na 112, wysłanie SMS do opiekuna (po sprawdzeniu uprawnień)
 * oraz anulowanie alarmu (jeśli został wywołany automatycznie).
 *
 * @param navController Kontroler nawigacji.
 * @param fallDetectorViewModel ViewModel do anulowania alarmu.
 * @param userProfileViewModel ViewModel do pobrania numeru opiekuna i adresu.
 */
@Composable
fun SOSEmergencyScreen(
    navController: NavController,
    fallDetectorViewModel: FallDetectorViewModel,
    userProfileViewModel: UserProfileViewModel
) {
    // Efekt pulsowania ikony
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(initialValue = 1f, targetValue = 1.1f, animationSpec = infiniteRepeatable(animation = tween(600), repeatMode = RepeatMode.Reverse), label = "scale")

    val context = LocalContext.current

    val profileState by userProfileViewModel.profileState.collectAsState()
    var carerNumber by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }

    // NAPRAWIONY BŁĄD: Aktywnie ładuj profil przy wejściu na ekran S.O.S.
    // Gwarantuje to, że numer opiekuna i adres są dostępne.
    LaunchedEffect(Unit) {
        userProfileViewModel.loadProfile()
    }

    // Aktualizuj stany lokalne, gdy profil się załaduje
    if (profileState is ProfileState.Success) {
        val data = (profileState as ProfileState.Success).data
        carerNumber = data["OpiekunNumer"] as? String ?: ""
        userAddress = data["Adres"] as? String ?: "Brak adresu w profilu"
    }

    // Launcher do proszenia o uprawnienia do wysyłania SMS
    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val message = "PILNE! Potrzebuję pomocy S.O.S. Moja lokalizacja to prawdopodobnie: $userAddress."
                sendSms(context, carerNumber, message)
            } else {
                Toast.makeText(context, "Nie udzielono zgody na wysłanie SMS.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(SosRed)) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(SosRed)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text("S.O.S.", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = Color.White)
            Icon(Icons.Default.Warning, contentDescription = "Ostrzeżenie", tint = Color.White, modifier = Modifier
                .size(150.dp)
                .scale(scale))
            Text("Zachowaj spokój!\nWezwij pomoc.", style = MaterialTheme.typography.headlineSmall, color = Color.White, textAlign = TextAlign.Center)

            // Przycisk "ZADZWOŃ 112"
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:112") }
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("ZADZWOŃ 112", color = SosRed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            // Przycisk "WYŚLIJ SMS DO OPIEKUNA"
            Button(
                onClick = {
                    val message = "PILNE! Potrzebuję pomocy S.O.S. Moja lokalizacja to prawdopodobnie: $userAddress."
                    // Sprawdź uprawnienia przed wysłaniem
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        sendSms(context, carerNumber, message)
                    } else {
                        // Poproś o uprawnienia
                        smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = carerNumber.isNotBlank() // Włącz tylko, jeśli numer jest załadowany
            ) {
                Text(
                    if (carerNumber.isNotBlank()) "WYŚLIJ SMS DO OPIEKUNA" else "BRAK NUMERU OPIEKUNA",
                    color = SosRed,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Przycisk "ANULUJ" (do wyłączenia alarmu po automatycznym wykryciu upadku)
            Button(
                onClick = {
                    fallDetectorViewModel.cancelAlarm() // Resetuje stan wykrywacza upadku
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("ANULUJ", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

/**
 * Ekran Inteligentnego Asystenta (AI).
 * Pozwala użytkownikowi wpisać polecenie w języku naturalnym, które jest
 * przetwarzane przez [processInputWithGemini].
 * Jeśli AI poprawnie sparsuje datę, godzinę i tytuł, wyświetla dialog potwierdzający
 * dodanie wydarzenia do kalendarza [SeniorCalendarViewModel].
 *
 * @param navController Kontroler nawigacji.
 * @param calendarViewModel ViewModel kalendarza, do którego dodawane są wydarzenia.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(navController: NavController, calendarViewModel: SeniorCalendarViewModel) {
    var textInput by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var parsedResult by remember { mutableStateOf<ParsedResult?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Inteligentny Asystent") },
                navigationIcon = {
                    IconButton(onClick = { if (!isLoading) navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wstecz")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween // Przycisk na dole
        ) {
            Column {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Asystent", tint = MaterialTheme.colorScheme.primary, modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(8.dp))
                Text("Jak mogę Ci pomóc?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("Napisz prostym językiem, co chcesz zrobić, np.\n'Potrzebuję zakupy jutro o 10:00' lub 'Spotkanie w piątek o 14:30'", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp))
                Spacer(Modifier.height(16.dp))
                CustomTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = "Wpisz swoją prośbę...",
                    enabled = !isLoading
                )
            }
            GradientButton(
                text = if (isLoading) "Analizuję..." else "Przeanalizuj",
                onClick = {
                    if (textInput.isNotBlank()) {
                        scope.launch {
                            isLoading = true
                            // Wywołanie funkcji suspend (Gemini API)
                            val response = processInputWithGemini(textInput)
                            when (response) {
                                is AiResponse.Success -> {
                                    // Sukces: pokaż dialog potwierdzający
                                    parsedResult = response.result
                                    showConfirmDialog = true
                                }
                                is AiResponse.Failure -> {
                                    // Błąd (AI prosi o doprecyzowanie): pokaż Snackbar
                                    snackbarHostState.showSnackbar(response.message)
                                }
                            }
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading && textInput.isNotBlank()
            )
        }

        // Dialog potwierdzający dodanie do kalendarza
        if (showConfirmDialog && parsedResult != null) {
            val result = parsedResult!!
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Czy dobrze zrozumiałem?") },
                text = {
                    val formattedDate = "${result.date.dayOfMonth}.${String.format("%02d", result.date.monthValue)}.${result.date.year}"
                    Text("Czy chcesz dodać nowe zadanie do organizera?\n\n" +
                            "Data: $formattedDate\n" +
                            "Godzina: ${result.time}\n" +
                            "Treść: ${result.taskTitle}")
                },
                confirmButton = {
                    Button(onClick = {
                        // Dodaj do kalendarza i wróć
                        calendarViewModel.addEvent(result.date, result.time, result.taskTitle)
                        showConfirmDialog = false
                        navController.popBackStack() // Wróć do ekranu głównego
                    }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                        Text("Tak, dodaj", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) { Text("Anuluj", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            )
        }
    }
}

// --- EKRAN SENIORM2 (POPRAWIONY) ---
/**
 * Ekran zarządzania zadaniami cyklicznymi (Seniorm2).
 * Wyświetla listę zadań i pozwala na ich dodawanie, edytowanie oraz usuwanie
 * za pomocą [AddSeniorm2Dialog].
 *
 * @param navController Kontroler nawigacji.
 * @param seniorm2ViewModel ViewModel zarządzający stanem zadań Seniorm2.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Seniorm2Screen(
    navController: NavController,
    seniorm2ViewModel: Seniorm2ViewModel
) {
    // Stan przechowujący item do edycji lub null (co oznacza, że dialog jest ukryty).
    // Jeśli chcemy dodać nowy, wstawiamy tu `newItemTemplate`.
    var showDialogForItem by remember { mutableStateOf<Seniorm2Item?>(null) }

    val seniorm2Items by seniorm2ViewModel.seniorm2Items.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Pusty szablon używany do tworzenia nowego elementu.
    val newItemTemplate = Seniorm2Item(
        name = "",
        time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
        daysOfWeek = emptySet()
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Moje Zadania Cykliczne") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wstecz") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialogForItem = newItemTemplate }, // Otwórz dialog w trybie "Dodaj"
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Dodaj zadanie", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Column(Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (seniorm2Items.isEmpty()) {
                // Widok, gdy lista jest pusta
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nie dodałeś jeszcze żadnych zadań cyklicznych.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                // Lista zadań
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(seniorm2Items) { item ->
                        Seniorm2DisplayItem(
                            item = item,
                            onClick = {
                                showDialogForItem = item // Otwórz dialog w trybie "Edytuj"
                            },
                            onDelete = {
                                seniorm2ViewModel.deleteSeniorm2(item)
                                scope.launch { snackbarHostState.showSnackbar("Usunięto pomyślnie!") }
                            }
                        )
                    }
                }
            }
        }

        // Dialog dodawania/edycji
        if (showDialogForItem != null) {
            AddSeniorm2Dialog(
                itemToEdit = showDialogForItem!!,
                onDismiss = { showDialogForItem = null },
                onSave = { item ->
                    // Sprawdź, czy item o tym ID już istnieje na liście
                    val isEditing = seniorm2Items.any { it.id == item.id }
                    if (isEditing) {
                        seniorm2ViewModel.updateSeniorm2(item)
                        scope.launch { snackbarHostState.showSnackbar("Zaktualizowano pomyślnie!") }
                    } else {
                        // Jeśli nie istnieje (bo użyliśmy `newItemTemplate`), dodaj go
                        seniorm2ViewModel.addSeniorm2(item.name, item.time, item.daysOfWeek)
                        scope.launch { snackbarHostState.showSnackbar("Dodano przypomnienie!") }
                    }
                    showDialogForItem = null // Zamknij dialog
                }
            )
        }
    }
}
// --- KONIEC POPRAWIONEGO EKRANU SENIORM2 ---

/**
 * Ekran Ustawień Aplikacji.
 * Pozwala użytkownikowi na zmianę globalnych ustawień dostępności,
 * takich jak skalowanie czcionki i tryb wysokiego kontrastu.
 *
 * @param navController Kontroler nawigacji.
 * @param settingsViewModel ViewModel zarządzający stanem ustawień.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val fontSizeScale by settingsViewModel.fontSizeScale.collectAsState()
    val isHighContrast by settingsViewModel.isHighContrast.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ustawienia Dostępności") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wstecz") } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sekcja: Rozmiar tekstu
            Text("Rozmiar tekstu", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(
                "Aa",
                fontSize = (16.sp.value * fontSizeScale).sp, // Podgląd rozmiaru
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Slider(
                value = fontSizeScale,
                onValueChange = { settingsViewModel.setFontSizeScale(it) },
                valueRange = 0.8f..1.5f, // Zakres skali
                steps = 6, // Liczba kroków na suwaku
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Mały", style = MaterialTheme.typography.bodySmall)
                Text("Domyślny", style = MaterialTheme.typography.bodySmall)
                Text("Duży", style = MaterialTheme.typography.bodySmall)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Sekcja: Tryb wyświetlania (Wysoki kontrast)
            Text("Tryb wyświetlania", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { settingsViewModel.setHighContrast(!isHighContrast) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Wysoki kontrast (żółto-czarny)", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Switch(
                    checked = isHighContrast,
                    onCheckedChange = { settingsViewModel.setHighContrast(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        checkedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

/**
 * Ekran profilu użytkownika.
 * Pozwala na edycję podstawowych danych (Imię, Nazwisko, Adres, Telefon)
 * oraz numeru telefonu opiekuna (tylko dla Seniorów).
 *
 * @param navController Kontroler nawigacji.
 * @param userProfileViewModel ViewModel zarządzający stanem profilu użytkownika.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userProfileViewModel: UserProfileViewModel
) {
    val profileState by userProfileViewModel.profileState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Stany lokalne dla pól formularza
    var imie by remember { mutableStateOf("") }
    var nazwisko by remember { mutableStateOf("") }
    var adres by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var opiekunNumer by remember { mutableStateOf("") }
    var isSenior by remember { mutableStateOf(false) }

    // ✅ NAPRAWIONY BŁĄD: Stan ładowania decyduje o aktywności pól
    val isLoading = profileState is ProfileState.Loading
    // Pola są nieaktywne, gdy trwa ładowanie LUB gdy stan jest Idle (dane niepobrane)
    val fieldsEnabled = profileState is ProfileState.Success && !isLoading

    // ✅ NAPRAWIONY BŁĄD: Aktywnie ładuj profil przy wejściu na ekran
    // Gwarantuje to pobranie danych, nawet jeśli VM został odtworzony.
    LaunchedEffect(Unit) {
        userProfileViewModel.loadProfile()
    }

    // Efekt do aktualizacji lokalnych stanów, gdy dane z VM się zmienią
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val data = (profileState as ProfileState.Success).data
            imie = data["Imie"] as? String ?: ""
            nazwisko = data["Nazwisko"] as? String ?: ""
            adres = data["Adres"] as? String ?: ""
            telefon = data["Telefon"] as? String ?: ""
            // Sprawdź, czy pole "OpiekunNumer" istnieje (tylko u Seniora)
            if (data.containsKey("OpiekunNumer")) {
                opiekunNumer = data["OpiekunNumer"] as? String ?: ""
                isSenior = true
            } else {
                opiekunNumer = ""
                isSenior = false
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mój Profil") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Wstecz") } }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            // Pokaż UI w zależności od stanu ładowania
            when (profileState) {
                is ProfileState.Idle, is ProfileState.Loading -> {
                    // Stan ładowania (lub początkowy)
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Error -> {
                    // Stan błędu
                    Text(text = (profileState as ProfileState.Error).message, color = MaterialTheme.colorScheme.error, modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp), textAlign = TextAlign.Center)
                }
                is ProfileState.Success -> {
                    // Stan sukcesu (dane załadowane) - pokaż formularz
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Twoje dane", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        CustomTextField(value = imie, onValueChange = { imie = it }, label = "Imię", enabled = fieldsEnabled)
                        CustomTextField(value = nazwisko, onValueChange = { nazwisko = it }, label = "Nazwisko", enabled = fieldsEnabled)
                        CustomTextField(value = adres, onValueChange = { adres = it }, label = "Adres", enabled = fieldsEnabled)
                        CustomTextField(value = telefon, onValueChange = { telefon = it }, label = "Numer Telefonu", keyboardType = KeyboardType.Phone, enabled = fieldsEnabled)

                        // Sekcja widoczna tylko dla Seniora
                        if (isSenior) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                            Text("Zaufana osoba (Opiekun)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Numer telefonu tej osoby zostanie użyty w razie alarmu S.O.S.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            CustomTextField(value = opiekunNumer, onValueChange = { opiekunNumer = it }, label = "Numer telefonu Opiekuna", keyboardType = KeyboardType.Phone, enabled = fieldsEnabled)
                        }

                        Spacer(Modifier.height(32.dp))
                        GradientButton(
                            text = if (isLoading) "Zapisywanie..." else "ZAPISZ ZMIANY", // Tekst przycisku reaguje na stan
                            enabled = fieldsEnabled, // Włączone tylko gdy dane są załadowane i nie trwa zapis
                            onClick = {
                                userProfileViewModel.saveProfile(imie, nazwisko, adres, telefon, opiekunNumer)
                                // VM używa addOnSuccessListener, więc feedback dajemy natychmiast
                                Toast.makeText(context, "Zaktualizowano profil!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- APPNAVIGATOR (POPRAWIONY) ---
/**
 * Główny komponent nawigacyjny aplikacji (NavHost).
 * Odpowiada za definiowanie wszystkich tras (AppRoutes) i mapowanie ich na
 * odpowiednie ekrany Composable.
 *
 * @param calendarViewModel ViewModel dla kalendarza (współdzielony).
 * @param fallDetectorViewModel ViewModel dla wykrywacza upadku (współdzielony).
 * @param helpRequestViewModel ViewModel dla zleceń (współdzielony).
 * @param seniorm2ViewModel ViewModel dla zadań cyklicznych (współdzielony).
 * @param settingsViewModel ViewModel dla ustawień (współdzielony).
 * @param authViewModel ViewModel dla autentykacji (współdzielony).
 * @param userProfileViewModel ViewModel dla profilu (współdzielony).
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator(
    calendarViewModel: SeniorCalendarViewModel,
    fallDetectorViewModel: FallDetectorViewModel,
    helpRequestViewModel: HelpRequestViewModel,
    seniorm2ViewModel: Seniorm2ViewModel, // Otrzymuje już poprawną, sfabrykowaną instancję z MainActivity
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    userProfileViewModel: UserProfileViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Sprawdzenie, czy użytkownik widział Onboarding
    val sharedPrefs = context.getSharedPreferences("OnboardingPrefs", Context.MODE_PRIVATE)
    val hasSeenOnboarding = sharedPrefs.getBoolean("hasSeenOnboarding", false)

    // Ustawienie ekranu startowego
    val startDestination = if (hasSeenOnboarding) AppRoutes.LOGIN else AppRoutes.ONBOARDING

    // Globalny obserwator stanu wykrywacza upadku
    val fallState by fallDetectorViewModel.fallDetectionState.collectAsState()

    // Prośba o uprawnienia do powiadomień (dla Android 13+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted -> Log.d("AppNavigator", "Uprawnienie do powiadomień: $isGranted") }
        )
        LaunchedEffect(Unit) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Box(Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            // --- Onboarding i Autentykacja ---
            composable(AppRoutes.ONBOARDING) { OnboardingScreen(navController) }
            composable(AppRoutes.LOGIN) { LoginScreen(navController, authViewModel) }
            composable(AppRoutes.REGISTER) { RegisterScreen(navController, authViewModel) }

            // --- Ekrany Główne (Panele) ---
            composable(AppRoutes.SENIOR_WELCOME) { SeniorWelcomeScreen(navController, authViewModel) }
            composable(AppRoutes.WOLONTARIUSZ_WELCOME) { WolontariuszWelcomeScreen(navController, authViewModel) }
            composable(AppRoutes.ADMIN_WELCOME) { AdminWelcomeScreen(navController, authViewModel) }

            // --- Funkcje Seniora ---
            composable(AppRoutes.SENIOR_DASHBOARD) {
                SeniorDashboardScreen(navController, helpRequestViewModel, userProfileViewModel)
            }
            composable(AppRoutes.SENIOR_MOJE_ZLECENIA) { SeniorMojeZleceniaScreen(navController, helpRequestViewModel) }

            // --- Funkcje Wolontariusza ---
            composable(AppRoutes.HELPER_DASHBOARD) { HelperDashboardScreen(navController, helpRequestViewModel) }
            composable(AppRoutes.WOLONTARIUSZ_MOJE_ZLECENIA) { WolontariuszMojeZleceniaScreen(navController, helpRequestViewModel) }

            // --- Funkcje Admina ---
            composable(AppRoutes.ADMIN_WSZYSTKIE_ZLECENIA) { AdminWszystkieZleceniaScreen(navController, helpRequestViewModel) }

            // --- NOWA TRASA (POPRAWKA) ---
            // --- NOWA TRASA (POPRAWKA) ---
            composable(AppRoutes.ADMIN_ARCHIWUM) {
                AdminArchiwumScreen(navController, helpRequestViewModel) // <-- POPRAWKA TUTAJ
            }
            // --- KONIEC NOWEJ TRASY ---

            // Trasa z argumentem (Zarządzanie Użytkownikami)
            composable(
                route = AppRoutes.ADMIN_MANAGE_USERS,
                arguments = listOf(navArgument(AppRoutes.ADMIN_MANAGE_USERS_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                val userRole = backStackEntry.arguments?.getString(AppRoutes.ADMIN_MANAGE_USERS_ARG)
                if (userRole == "Senior" || userRole == "Wolontariusz") {
                    AdminManageUsersScreen(navController, authViewModel, userRole)
                } else {
                    // Jeśli argument jest nieprawidłowy, wróć
                    navController.popBackStack()
                }
            }

            // --- Trasy Wspólne i Funkcyjne ---

            // Trasa z argumentem (Szczegóły Zlecenia)
            composable(
                route = AppRoutes.ZLECENIE_DETAILS,
                arguments = listOf(navArgument(AppRoutes.ZLECENIE_DETAILS_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                val zlecenieId = backStackEntry.arguments?.getString(AppRoutes.ZLECENIE_DETAILS_ARG)
                if (zlecenieId != null) {
                    ZlecenieDetailsScreen(navController, helpRequestViewModel, zlecenieId)
                } else {
                    navController.popBackStack()
                }
            }

            composable(AppRoutes.SENIOR_CALENDAR) { SeniorCalendarScreen(navController, calendarViewModel) }
            composable(AppRoutes.SOS_EMERGENCY) { SOSEmergencyScreen(navController, fallDetectorViewModel, userProfileViewModel) }
            composable(AppRoutes.ASSISTANT) { AssistantScreen(navController, calendarViewModel) }

            // Trasa Seniorm2 - ViewModel jest już poprawnie przekazany z MainActivity
            composable(AppRoutes.SENIORM2_ITEMS) {
                Seniorm2Screen(navController, seniorm2ViewModel)
            }

            composable(AppRoutes.SETTINGS) { SettingsScreen(navController, settingsViewModel) }
            composable(AppRoutes.PROFILE) { ProfileScreen(navController, userProfileViewModel) }
        }

        // --- Globalna Obsługa Wykrywacza Upadku ---

        // Efekt, który nawiguje do S.O.S., gdy stan zmieni się na Alarm
        LaunchedEffect(fallState) {
            if (fallState is FallDetectionState.Alarm) {
                // Sprawdź, czy już nie jesteśmy na ekranie SOS, aby uniknąć wielokrotnej nawigacji
                if (navController.currentDestination?.route != AppRoutes.SOS_EMERGENCY) {
                    navController.navigate(AppRoutes.SOS_EMERGENCY)
                }
            }
        }

        // Jeśli stan to Odliczanie, pokaż dialog odliczania NAWET na innych ekranach
        if (fallState is FallDetectionState.Countdown) {
            val secondsLeft = (fallState as FallDetectionState.Countdown).secondsLeft
            FallCountdownDialog(
                secondsLeft = secondsLeft,
                onCancel = { fallDetectorViewModel.cancelCountdown() }
            )
        }
    }
}