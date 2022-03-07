package processor

import java.lang.Exception

class Matrix(private val sMatrix: Array<MutableList<String>>) {

    data class vector(val values: List<Number>, val type: String)

    val matrixType = mType(sMatrix)
    private val matrix = createMatrix(sMatrix)
    private val aMatrix = matrix[0]
    private val aMatrixT = matrix[1]
    val size = size()
    val rows = size[0]
    val cols = size[1]

    fun isInt() = matrixType == "Int"

    fun row(n: Int) = aMatrix[n]

    fun col(n: Int) = aMatrixT[n]

    private fun size() = if (aMatrix.isNotEmpty()) listOf(aMatrix.size, aMatrix[0].size) else listOf(0, 0)

     private fun mType(sMatrix: Array<MutableList<String>>): String {
        var matrixType = "Int"
        for (row in sMatrix) {
            row.forEach {
                when {
                    it.matches(Regex("[+-]?\\d+")) -> {}
                    it.matches(Regex("[+-]?\\d*.\\d+")) -> matrixType = "Double"
                    else -> throw Exception("Not a matrix")
                }
            }
        }
        return matrixType
    }

    private fun createMatrix(sMat: Array<MutableList<String>>): List<Array<MutableList<Number>>> {
        if (sMat.isNotEmpty() && sMat[0].isNotEmpty()) {
            val newMat = Array(sMat.size) { MutableList<Number>(sMat[0].size) { if (isInt()) 0 else 0.0 } }
            val newMatT = Array(sMat[0].size) { MutableList<Number>(sMat.size) { if (isInt()) 0 else 0.0 } }
            val newMatST = Array(sMat[0].size) { MutableList<Number>(sMat.size) { if (isInt()) 0 else 0.0 } }
            val newMatVT = Array(sMat.size) { MutableList<Number>(sMat[0].size) { if (isInt()) 0 else 0.0 } }
            val newMatHT = Array(sMat.size) { MutableList<Number>(sMat[0].size) { if (isInt()) 0 else 0.0 } }
            for (i in sMat.indices) {
                for (j in sMat[0].indices) {
                    newMat[i][j] = if (isInt()) sMat[i][j].toInt() else sMat[i][j].toDouble()
                    newMatT[j][i] = if (isInt()) sMat[i][j].toInt() else sMat[i][j].toDouble()
                    newMatST[sMat[0].lastIndex - j][sMat.lastIndex - i] = if (isInt()) sMat[i][j].toInt() else sMat[i][j].toDouble()
                    newMatVT[i][sMat[0].lastIndex - j] = if (isInt()) sMat[i][j].toInt() else sMat[i][j].toDouble()
                    newMatHT[sMat.lastIndex - i][j] = if (isInt()) sMat[i][j].toInt() else sMat[i][j].toDouble()
                }
            }
            return listOf(newMat, newMatT, newMatST, newMatVT, newMatHT)
        }
        return listOf(emptyArray(), emptyArray())
    }

    private fun toMatrix(mat: Array<MutableList<Number>>): Matrix {
        if (mat.isNotEmpty()) {
            val newMat = Array(mat.size) { MutableList(mat[0].size) { "" } }
            for (i in mat.indices) {
                for (j in mat[0].indices) {
                    newMat[i][j] = mat[i][j].toString()
                }
            }
            return Matrix(newMat)
        }
        return Matrix(emptyArray())
    }

    private fun dotProduct(v1: vector, v2: vector): String {
        if (v1.type == "Int" && v2.type == "Int") {
            var result = 0
            for (index in v1.values.indices) {
                result += v1.values[index].toInt() * v2.values[index].toInt()
            }
            return result.toString()
        } else {
            var result = 0.0
            for (index in v1.values.indices) {
                result += v1.values[index].toDouble() * v2.values[index].toDouble()
            }
            return result.toString()
        }
    }

    private fun reduce(mat: Array<MutableList<Number>>, row: Int, col: Int): Matrix {
        if (rows < row + 1 || cols < col + 1) throw Exception("Can reduce matrix. Index outside of matrix.")
        if (rows <= 1 || cols <= 1) {
            return Matrix(emptyArray())
        }
        val reduced = toMatrix(mat).aMatrix.toMutableList()
        reduced.removeAt(row)
        reduced.forEach { it.removeAt(col) }
        return toMatrix(reduced.toTypedArray())
    }

    private fun cofactor(mat: Matrix, i: Int, j: Int): Number {
        return if (mat.isInt()){
            (if ((i + j) % 2 == 0) 1 else -1) * reduce(mat.aMatrix, i, j).determinant().toInt()
        } else {
            (if ((i + j) % 2 == 0) 1 else -1) * reduce(mat.aMatrix, i, j).determinant().toDouble()
        }
    }

    fun adj(mat: Matrix): Matrix {
        val newMat = Array(mat.rows) { MutableList<Number>(mat.cols) { if (isInt()) 0 else 0.0 } }
        for (i in 0 until mat.rows) {
            for (j in 0 until mat.cols) {
                newMat[i][j] = cofactor(mat, i, j)
            }
        }
        return toMatrix(newMat).transpose()
    }

    fun transpose() = toMatrix(matrix[1])

    fun sideTranspose() = toMatrix(matrix[2])

    fun verticalTranspose() = toMatrix(matrix[3])

    fun horizontalTranspose() = toMatrix(matrix[4])

    fun determinant(): Number {
        if (rows != cols || rows < 0) throw Exception("Determinant only exists for square matrices")
        if (rows == 0) return 1
        if (isInt()) {
            var det = 0
            for (j in aMatrix[0].indices) {
                det += aMatrix[0][j].toInt() * cofactor(toMatrix(aMatrix), 0, j).toInt()
            }
            return det
        } else {
            var det = 0.0
            for (j in aMatrix[0].indices) {
                det += aMatrix[0][j].toDouble() * cofactor(toMatrix(aMatrix), 0, j).toDouble()
            }
            return det
        }
    }

    fun inv(): Matrix {
        val det = this.determinant()
        if (det == 0) throw Exception("This matrix doesn't have an inverse.")
        return adj(this) / det.toDouble()
    }

    private operator fun div(c: Double): Matrix {
        if (aMatrix.isEmpty()) return Matrix(emptyArray())
        val new = Array(aMatrix.size){ MutableList<String>(aMatrix[0].size) { "" } }
        for (i in aMatrix.indices) {
            for (j in aMatrix[0].indices) {
                new[i][j] = (aMatrix[i][j].toDouble() / c).toString()
            }
        }
        return Matrix(new)
    }

    operator fun plus(bMatrix: Matrix): Matrix {
        if (size() != bMatrix.size()) throw Exception("The operation cannot be performed.")
        if (aMatrix.isEmpty()) return Matrix(emptyArray())
        val sumMatrix = Array(aMatrix.size){ MutableList<String>(aMatrix[0].size) { "" } }
        for (i in aMatrix.indices) {
            for (j in aMatrix[0].indices) {
                sumMatrix[i][j] = (if (isInt() && bMatrix.isInt()) aMatrix[i][j].toInt() + bMatrix.aMatrix[i][j].toInt()
                else aMatrix[i][j].toDouble() + bMatrix.aMatrix[i][j].toDouble()).toString()
            }
        }
        return Matrix(sumMatrix)
    }

    operator fun times(c: Int): Matrix {
        if (aMatrix.isEmpty()) return Matrix(emptyArray())
        val new = Array(aMatrix.size){ MutableList<String>(aMatrix[0].size) { "" } }
        for (i in aMatrix.indices) {
            for (j in aMatrix[0].indices) {
                new[i][j] = (if (isInt()) aMatrix[i][j].toInt() * c else aMatrix[i][j].toDouble() * c).toString()
            }
        }
        return Matrix(new)
    }

    operator fun times(c: Double): Matrix {
        if (aMatrix.isEmpty()) return Matrix(emptyArray())
        val new = Array(aMatrix.size){ MutableList<String>(aMatrix[0].size) { "" } }
        for (i in aMatrix.indices) {
            for (j in aMatrix[0].indices) {
                new[i][j] = (aMatrix[i][j].toDouble() * c).toString()
            }
        }
        return Matrix(new)
    }

    operator fun times(c: String): Matrix {
        if (aMatrix.isEmpty()) return Matrix(emptyArray())
        return when {
            (c.matches(Regex("\\d+"))) -> this * c.toInt()
            else -> this * c.toDouble()
        }
    }

    operator fun times(bMatrix: Matrix): Matrix {
        if (cols != bMatrix.rows) throw Exception("Matrix multiplication not possible. Wrong dimensions.")
        if (aMatrix.isNotEmpty() && bMatrix.aMatrix.isNotEmpty()) {
            val newMatrix = Array(rows) { MutableList(bMatrix.cols) { "" } }
            for (i in 0 until rows) {
                for (j in 0 until bMatrix.cols) {
                    newMatrix[i][j] = dotProduct(vector(row(i), matrixType), vector(bMatrix.col(j), bMatrix.matrixType))
                }
            }
            return Matrix(newMatrix)
        }
        return Matrix(emptyArray())
    }

    operator fun get(i: Int): List<Any> {
        return aMatrix[i]
    }

    override fun toString(): String {
        var matrixPrint = ""
        if (aMatrix.isEmpty()) return ""
        for (row in aMatrix) {
            matrixPrint += row.joinToString(" ") + "\n"
        }
        return matrixPrint.dropLast(1)
    }
}