package com.russian_exams.myapplication

import java.util.*
import kotlin.math.max

interface FractionStoreysDistrict {
    val upperLevels : Float
    val lowerLevels : Float
    val height: Float
        get() = upperLevels + lowerLevels
    val baselineBias: Float
        get() = upperLevels / height
}

interface Expr: FractionStoreysDistrict {
    override fun toString(): String

    operator fun plus(increment: Expr): Expr = Sum(this, increment)
    operator fun times(factor: Expr): Expr = Mult(this, factor)
    operator fun minus(decrement: Expr) = Subt(this, decrement)
    operator fun div(divisor: Expr) = Div(this, divisor)

}

interface Primitive: Expr {
    override val upperLevels: Float
        get() = 0.5f
    override val lowerLevels: Float
        get() = 0.5f
}

data class Number(var num: Int): Primitive {
    override fun toString(): String {
        return num.toString()
    }
}

data class Var(var name: Char): Primitive {
    override fun toString(): String {
        return name.toString()
    }
}

val Int.f : Primitive
get() = Number(this)

enum class Const: Primitive {
    PI {
        override fun toString(): String = "𝜋"
    },
    E {
        override fun toString(): String = "𝜋"
    }
}

interface BinaryOperation: Expr {
    var l: Expr
    var r: Expr
}


interface RowRepresentable: BinaryOperation {
    override val upperLevels: Float
        get() = max(l.upperLevels, r.upperLevels)

    override val lowerLevels: Float
        get() = max(l.lowerLevels, r.lowerLevels)
}

data class Sum(override var l: Expr,
               override var r: Expr) : RowRepresentable {
    override fun toString(): String {
        return "($l + $r)"
    }
}

data class Subt(override var l: Expr,
                override var r: Expr) : RowRepresentable {
    override fun toString(): String {
        return "($l $r)"
    }
}

data class Mult(override var l: Expr,
                override var r: Expr) : RowRepresentable {
    override fun toString(): String {
        return "($l $r)"
    }
}

data class Div(override var l: Expr,
               override var r: Expr) : BinaryOperation {
    override fun toString(): String {
        return "($l / $r)"
    }

    override val upperLevels: Float
        get() = l.height

    override val lowerLevels: Float
        get() = r.height
}

data class Pow(override var l: Expr,
               override var r: Expr) : BinaryOperation {
    override fun toString(): String {
        return "($l ^ $r)"
    }

    override val upperLevels: Float
        get() = TODO("Not yet implemented")
    override val lowerLevels: Float
        get() = TODO("Not yet implemented")
}

data class Log(override var l: Expr,
               override var r: Expr): BinaryOperation {
    override fun toString(): String {
        return "log($l, $r)"
    }

    override val upperLevels: Float
        get() = TODO("Not yet implemented")
    override val lowerLevels: Float
        get() = TODO("Not yet implemented")
}

interface UnaryOperation: Expr {
    var arg: Expr

    override val upperLevels: Float
        get() = arg.upperLevels
    override val lowerLevels: Float
        get() = arg.lowerLevels
}

abstract class UnaryFunction(override var arg: Expr): UnaryOperation {
    final override fun toString(): String {
        return "${this::class.simpleName?.toLowerCase(Locale.ROOT)}($arg)"
    }
}

data class Sqrt(override var arg: Expr): UnaryFunction(arg)

data class Ln(override var arg: Expr): UnaryFunction(arg)

data class Lg(override var arg: Expr): UnaryFunction(arg)

data class Sin(override var arg: Expr): UnaryFunction(arg)

data class Cos(override var arg: Expr): UnaryFunction(arg)

data class Tg(override var arg: Expr): UnaryFunction(arg)

data class Ctg(override var arg: Expr): UnaryFunction(arg)

data class Arcsin(override var arg: Expr): UnaryFunction(arg)

data class Arcccos(override var arg: Expr): UnaryFunction(arg)

data class Arctg(override var arg: Expr): UnaryFunction(arg)

data class Arcctg(override var arg: Expr): UnaryFunction(arg)

fun main() {
    val expr = 1.f + 1.f / 1.f
    val expr1 = 1.f + 1.f / (1.f + 1.f/(1.f + 1.f / (1.f + 1.f / 1.f)))
    println(expr.height)
    println(expr.baselineBias)
}