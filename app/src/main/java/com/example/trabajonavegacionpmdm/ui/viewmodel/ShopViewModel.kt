package com.example.trabajonavegacionpmdm.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.trabajonavegacionpmdm.data.Vehicle

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