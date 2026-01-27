package com.example.trabajonavegacionpmdm
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// SIMULACIÓN DE DATOS
data class Vehicle(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Double,
    val hp: Int,
    val imageRes: Int // Sustituiremos por imagenes
)

val vehicleList = listOf(
    Vehicle(1, "Toyota", "Corolla", 25000.0, 140, R.drawable.corolla),
    Vehicle(2, "Ford", "Mustang", 55000.0, 450, R.drawable.mustang),
    Vehicle(3, "Tesla", "Model 3", 45000.0, 300, R.drawable.model3),
    Vehicle(4,"Porsche","911",100000.0,400,R.drawable.porsche)
)

// VIEWMODEL (Para gestión del estado del carrito)

//ViewModel Compartido
// Usamos esto para pasar datos complejos o mantener estado entre pantallas.
class ShopViewModel : ViewModel() {
    var selectedQuantity by mutableStateOf(0)
    var selectedVehicle: Vehicle? by mutableStateOf(null)

    val totalPrice: Double
        get() = (selectedVehicle?.price ?: 0.0) * selectedQuantity

    fun addToCart(vehicle: Vehicle, quantity: Int) {
        selectedVehicle = vehicle
        selectedQuantity = quantity
    }

    fun clearCart() {
        selectedVehicle = null
        selectedQuantity = 0
    }
}

//MainActivity (lo que se ve)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shopViewModel: ShopViewModel by viewModels()

        setContent {
            //Inicializar NavController
            val navController = rememberNavController()

            MaterialTheme {
                //NavHost contenedor principal
                NavHost(navController = navController, startDestination = "welcome") {

                    // Definimos un nombre para cada pantalla
                    composable("welcome") {
                        WelcomeScreen(navController)
                    }

                    composable("law_info") {
                        LawInfoScreen(navController)
                    }

                    composable("home") {
                        HomeScreen(navController)
                    }

                    composable(
                        route = "details/{vehicleId}",
                        arguments = listOf(navArgument("vehicleId") { type = NavType.IntType }) // - Uso de NavType.IntType (Línea 348)
                    ) { backStackEntry ->
                        val vehicleId = backStackEntry.arguments?.getInt("vehicleId") ?: 0
                        DetailsScreen(navController, vehicleId, shopViewModel)
                    }

                    composable("cart") {
                        CartScreen(navController, shopViewModel)
                    }
                }
            }
        }
    }
}
//PANTALLA LEY
@Composable
fun LawInfoScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ACCESO DENEGADO", color = MaterialTheme.colorScheme.error)
        Text("Según la ley vigente, debes ser mayor de edad para comprar un vehículo.")
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de vuelta a pantalla bienvenida
        Button(onClick = {
            //volver
            navController.popBackStack()
        }) {
            Text("Volver a Bienvenida")
        }
    }
}

//BIENVENIDA
@Composable
fun WelcomeScreen(navController: NavController) {
    var ageInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido al Concesionario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = ageInput,
            onValueChange = { ageInput = it },
            label = { Text("Introduce tu edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val age = ageInput.toIntOrNull()
            if (age != null) {
                if (age >= 18) {
                    //Si es mayor de edad, pantalla de inicio
                    navController.navigate("home")
                } else {
                    //Si es menor, pantalla de aviso
                    navController.navigate("law_info")
                }
            } else {
                Toast.makeText(context, "Por favor introduce un número", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Entrar")
        }
    }
}

//INICIO (HOME)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var NombreVehiculoSeleccionado by remember { mutableStateOf("Ninguno seleccionado") }
    var VehiculoSeleccionado by remember { mutableStateOf<Int?>(null) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JoOS") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            Text("Catálogo de Vehículos", style = MaterialTheme.typography.headlineSmall)
            Text("Seleccionado: $NombreVehiculoSeleccionado") //Indica el item seleccionado

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de items
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(vehicleList) { vehicle ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                NombreVehiculoSeleccionado = "${vehicle.brand} ${vehicle.model}"
                                VehiculoSeleccionado = vehicle.id
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${vehicle.brand} ${vehicle.model}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = "ID: ${vehicle.id} - Precio: $${vehicle.price}")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    enabled = VehiculoSeleccionado != null,
                    onClick = {
                        VehiculoSeleccionado?.let { id ->
                            //Le pasamos el coche seleccionado
                            navController.navigate("details/$id")
                        }
                    }
                ) {
                    Text("Confirmar / Detalles")
                }
            }
        }
    }
}

//DETALLES
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, vehicleId: Int, viewModel: ShopViewModel) {
    // Buscamos el vehículo por ID
    val vehicle = vehicleList.find { it.id == vehicleId }

    val options = listOf("1", "2", "3")
    var expanded by remember { mutableStateOf(false) }
    var selectedQuantity by remember { mutableStateOf(options[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JoOS") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (vehicle != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text("Detalles del Vehículo", style = MaterialTheme.typography.headlineMedium)

                Image(
                    painter = painterResource(id = vehicle.imageRes),
                    contentDescription = "Imagen del ${vehicle.model}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit
                )

                Text("Marca: ${vehicle.brand}")
                Text("Modelo: ${vehicle.model}")
                Text("Potencia: ${vehicle.hp} CV")
                Text("Precio Unidad: $${vehicle.price}")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Selecciona la cantidad:", style = MaterialTheme.typography.bodyMedium)

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedQuantity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cantidad") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedQuantity = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //Comprar lleva a carrito
                    Button(onClick = {
                        val qty = selectedQuantity.toInt()
                        viewModel.addToCart(vehicle, qty)
                        navController.navigate("cart")
                    }) {
                        Text("Comprar")
                    }
                }
            }
        } else {
            Box(modifier = Modifier.padding(innerPadding)) {
                Text("Vehículo no encontrado")
            }
        }
    }
}

//CARRITO COMPRA
@Composable
fun CartScreen(navController: NavController, viewModel: ShopViewModel) {
    //Recuperamos datos del ViewModel
    val vehicle = viewModel.selectedVehicle
    val quantity = viewModel.selectedQuantity
    val total = viewModel.totalPrice

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Carrito de Compra", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        if (vehicle != null) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Vehículo: ${vehicle.brand} ${vehicle.model}")
                    Text("Cantidad seleccionada: $quantity")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("TOTAL A PAGAR: $$total", style = MaterialTheme.typography.titleLarge)
                }
            }
        } else {
            Text("El carrito está vacío")
        }

        Spacer(modifier = Modifier.height(32.dp))

        //Vuelta a casa
        Button(onClick = {
            //Limpiamos toda la ruta para volver al home directos
            navController.navigate("home") {
                popUpTo("home") {
                    inclusive = true
                }
            }
            viewModel.clearCart() //Limpiamos carrito al salir
        }) {
            Text("Volver al Inicio (Home)")
        }
    }
}