package lt.furniplan.seimosfinansai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF0D1110)
private val Surface = Color(0xFF171C1A)
private val Furni = Color(0xFF7B8981)
private val FurniLight = Color(0xFFAFC1B7)
private val TextPrimary = Color(0xFFF2F5F3)
private val TextSecondary = Color(0xFFABB3AF)
private val Divider = Color(0xFF303934)

enum class Screen(val label: String, val symbol: String) {
    Home("Pradžia", "⌂"),
    Payments("Mokėjimai", "€"),
    Calendar("Kalendorius", "▦"),
    Funds("Fondai", "◎"),
    Contracts("Sutartys", "□")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FinanceApp() }
    }
}

@Composable
fun FinanceApp() {
    var screen by remember { mutableStateOf(Screen.Home) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = FurniLight,
            secondary = Furni,
            background = Bg,
            surface = Surface,
            onBackground = TextPrimary,
            onSurface = TextPrimary
        )
    ) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF101513)) {
                    Screen.entries.forEach { item ->
                        NavigationBarItem(
                            selected = screen == item,
                            onClick = { screen = item },
                            icon = { Text(item.symbol, fontSize = 20.sp) },
                            label = { Text(item.label, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = FurniLight,
                                selectedTextColor = FurniLight,
                                indicatorColor = Color(0xFF202723),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (screen) {
                    Screen.Home -> HomeScreen()
                    Screen.Payments -> PaymentsScreen()
                    Screen.Calendar -> CalendarScreen()
                    Screen.Funds -> FundsScreen()
                    Screen.Contracts -> ContractsScreen()
                }
            }
        }
    }
}

@Composable
private fun Page(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        content()
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun AppCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun Stat(label: String, value: String, accent: Boolean = false) {
    Column {
        Text(label, color = TextSecondary, fontSize = 12.sp)
        Text(
            value,
            color = if (accent) FurniLight else TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun HomeScreen() = Page("Šeimos finansai") {
    Text("Spalis 2027", color = TextSecondary)
    AppCard {
        Text("Mėnesio apžvalga", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Stat("Pajamos", "3 000 €")
            Stat("Suplanuota", "2 314 €")
            Stat("Lieka", "686 €", true)
        }
        LinearProgressIndicator(
            progress = { 0.77f },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = FurniLight,
            trackColor = Divider
        )
    }
    Text("Pagrindinės kategorijos", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryCard("Namas", "945,52 €", 0.41f, Modifier.weight(1f))
        CategoryCard("Vaikas", "241,63 €", 0.10f, Modifier.weight(1f))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryCard("Automobilis", "110,00 €", 0.05f, Modifier.weight(1f))
        CategoryCard("Darbas", "230,93 €", 0.10f, Modifier.weight(1f))
    }
    Text("Artimiausi mokėjimai", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    PaymentRow("20 spalio", "SEB paskola", "510,86 €")
    PaymentRow("20 spalio", "Gyvybės draudimas", "20,34 €")
    PaymentRow("25 spalio", "Telia", "78,00 €")
}

@Composable
private fun CategoryCard(title: String, amount: String, progress: Float, modifier: Modifier) {
    AppCard(modifier) {
        Text(title, color = TextSecondary)
        Text(amount, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = FurniLight,
            trackColor = Divider
        )
    }
}

@Composable
private fun PaymentRow(date: String, name: String, amount: String, showButton: Boolean = false) {
    AppCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(date, color = FurniLight, fontSize = 12.sp)
                Text(name, fontWeight = FontWeight.SemiBold)
            }
            Text(amount, fontWeight = FontWeight.Bold)
        }
        if (showButton) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FurniLight,
                    contentColor = Color(0xFF101513)
                )
            ) {
                Text("✓ Sumokėta")
            }
        }
    }
}

@Composable
private fun PaymentsScreen() = Page("Mokėjimai") {
    PaymentRow("20 spalio", "SEB paskola", "510,86 €", true)
    PaymentRow("20 spalio", "Gyvybės draudimas", "20,34 €", true)
    PaymentRow("25 spalio", "Telia", "78,00 €", true)
    PaymentRow("1 lapkričio", "Dropbox", "16,99 €", true)
}

@Composable
private fun CalendarScreen() = Page("Kalendorius") {
    AppCard {
        Text("Spalis 2027", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        val weeks = listOf(
            listOf("Pr","An","Tr","Kt","Pn","Št","Sk"),
            listOf("27","28","29","30","1","2","3"),
            listOf("4","5","6","7","8","9","10"),
            listOf("11","12","13","14","15","16","17"),
            listOf("18","19","20","21","22","23","24"),
            listOf("25","26","27","28","29","30","31")
        )
        weeks.forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                row.forEach { day ->
                    val selected = day == "20" || day == "25"
                    Surface(
                        color = if (selected) Furni else Color.Transparent,
                        shape = RoundedCornerShape(99.dp),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(day, color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
    PaymentRow("20 spalio", "SEB paskola", "510,86 €")
    PaymentRow("20 spalio", "Gyvybės draudimas", "20,34 €")
    PaymentRow("25 spalio", "Telia", "78,00 €")
}

@Composable
private fun FundsScreen() = Page("Fondai") {
    FundCard("Rezervas", "1 840 €", "2 500 €", 0.74f)
    FundCard("Granulės", "1 050 €", "2 100 €", 0.50f)
    FundCard("Auto fondas", "430 €", "1 200 €", 0.36f)
}

@Composable
private fun FundCard(name: String, saved: String, target: String, progress: Float) {
    AppCard(Modifier.fillMaxWidth()) {
        Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("$saved / $target", color = FurniLight)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = FurniLight,
            trackColor = Divider
        )
    }
}

@Composable
private fun ContractsScreen() = Page("Sutartys") {
    ContractCard("Būsto draudimas", "478,76 € / metus", "Galioja iki 2027-10-05")
    ContractCard("Saugus kreditas", "20,34 € / mėn.", "Patvirtinta dokumentu")
    ContractCard("Telia", "78,00 € / mėn.", "Tavo pateikta suma")
    ContractCard("Elektra", "120,00 € / mėn.", "Preliminari sąmata")
}

@Composable
private fun ContractCard(name: String, price: String, detail: String) {
    AppCard(Modifier.fillMaxWidth()) {
        Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(price, color = FurniLight, fontSize = 18.sp)
        Text(detail, color = TextSecondary)
    }
}
