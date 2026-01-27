package com.example.trabajonavegacionpmdm.data

import com.example.trabajonavegacionpmdm.R

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