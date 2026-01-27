package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

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