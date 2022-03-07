package processor

import java.lang.Exception

const val MENU = "\n" +
        "1. Add matrices\n" +
        "2. Multiply matrix by a constant\n" +
        "3. Multiply matrices\n" +
        "4. Transpose matrix\n" +
        "5. Calculate a determinant\n" +
        "6. Inverse matrix\n" +
        "0. Exit\n" +
        "Your choice: "

const val TRANSPOSE_MENU = "\n" +
        "1. Main diagonal\n" +
        "2. Side diagonal\n" +
        "3. Vertical line\n" +
        "4. Horizontal line\n" +
        "Your choice: "

fun inputMatrix(which: String): Matrix {
    print(if (which.isEmpty()) "Enter size of matrix: " else "Enter size of $which matrix: ")
    val (rows, cols) = readln().split(" ").map { it.toInt() }
    println(if (which.isEmpty()) "Enter matrix" else "Enter $which matrix:")
    return Matrix(Array(rows) { readln().split(" ").subList(0, cols).toMutableList() })
}

fun inputConstant(): String {
    print("Enter constant: ")
    return readln()
}

fun addMatrices() {
    try {
        println("The result is:\n${inputMatrix("first") + inputMatrix("second")}")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun multiplyWithConstant() {
    println("The result is:\n${inputMatrix("") * inputConstant()}")
}

fun multiplyMatrices() {
    try {
        println("The result is:\n${inputMatrix("first") * inputMatrix("second")}")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun transposeMatrix() {
    print(TRANSPOSE_MENU)
    when (val choice = readln()) {
        "1" -> println("The result is:\n${inputMatrix("").transpose()}")
        "2" -> println("The result is:\n${inputMatrix("").sideTranspose()}")
        "3" -> println("The result is:\n${inputMatrix("").verticalTranspose()}")
        "4" -> println("The result is:\n${inputMatrix("").horizontalTranspose()}")
    }
}

fun determinant() {
    try {
        println("The result is:\n${inputMatrix("").determinant()}")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun inverse() {
    try {
        println("The result is:\n${inputMatrix("").inv()}")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun main() {
    while (true) {
        print(MENU)
        when (val choice = readln()) {
            "1" -> addMatrices()
            "2" -> multiplyWithConstant()
            "3" -> multiplyMatrices()
            "4" -> transposeMatrix()
            "5" -> determinant()
            "6" -> inverse()
            "0" -> break
            else -> println("$choice:  No such option!\nTry again\n")
        }
    }
}