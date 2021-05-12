package com.russian_exams.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.russian_exams.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val k = Var('k')
                    RenderExpr((1.f + 1.f / 1.f) / 1.f )
                }
            }
        }
    }
}

@Composable
fun RenderPrimitive(primitive: Primitive, modifier: Modifier) {
    when(primitive) {
        is Var -> Text(text = primitive.toString(),
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.SansSerif,
            modifier = modifier)
        else -> Text(text = primitive.toString(),
            fontFamily = FontFamily.SansSerif,
            modifier = modifier)
    }
}

@Composable
fun RenderBinary(operation: BinaryOperation, modifier: Modifier) {
    when(operation) {
        is Div -> RenderDiv(operation.l, operation.r)
        is Sum -> RenderRowBinary(
            left = operation.l,
            right = operation.r,
            sign = "+",
            modifier = modifier)
        is Mult -> {
            val sign = if(shouldShowMultiplicationSign(operation.l, operation.r)){
                "⋅"
            } else {
                ""
            }
            RenderRowBinary(
                left = operation.l,
                right = operation.r,
                sign = sign,
                modifier = modifier)
        }
        is Subt -> RenderRowBinary(
            left = operation.l,
            right = operation.r,
            sign = "—",
            modifier = modifier)
        is Pow -> RenderPow(
            base = operation.l,
            power = operation.r ,
            modifier = modifier)
    }
}

@Composable
fun RenderDiv(numerator: Expr, denominator: Expr) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Min)
            .padding(1.dp)
    ) {
        Box(
            Modifier
                .weight(numerator.height)
                .fillMaxHeight()) {
            RenderExpr(expr = numerator)
        }
        Divider(color = Color.Black,
            thickness = 1.dp)
        Box(
            Modifier
                .weight(denominator.height)
                .fillMaxHeight()) {
            RenderExpr(expr = denominator)
        }
    }
}

@Composable
fun RenderRowBinary(left: Expr, right: Expr, sign: String, modifier: Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(1.dp)) {

        val leftModifier = modifier.alignBy {
            (it.measuredHeight * left.baselineBias).toInt()
        }
        val rightModifier = modifier.alignBy {
            (it.measuredHeight * right.baselineBias).toInt()
        }

        Box(leftModifier) {
            RenderExpr(expr = left, modifier)
        }

        Text(sign,  modifier = modifier
            .padding(2.dp)
            .alignBy { it.measuredHeight / 2 })
        Box(rightModifier) {
            RenderExpr(expr = right, modifier)
        }
    }
}

@Composable
fun RenderPow(base: Expr, power: Expr, modifier: Modifier) {
    Row (horizontalArrangement = Arrangement.End) {
        Box(
            Modifier.alignBy { it.measuredHeight / 4 }
            .wrapContentWidth()
        ) {
            RenderExpr(base)
        }
        Box(
                Modifier
                .scale(0.4f)
                .alignBy { it.measuredHeight / 2 }
                .wrapContentWidth()
        ) {

            RenderExpr(power, modifier)
        }
    }
}

@Composable
fun RenderExpr(expr: Expr, modifier: Modifier = Modifier) {
    when(expr) {
        is Primitive -> RenderPrimitive(expr, modifier)
        is BinaryOperation -> RenderBinary(expr, modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val x = Var('x')
    val k = Var('k')
    MyApplicationTheme {
        val e1 = 1.f / (24.f * x) + (2.f + 5.f * (11.f / 3.f))/ (31.f - 2.f * (1.f / (5.f+ 2.f * (x / 3.f)))) + 12.f
        val e2 = 1.f + (14.f + (1.f/(1.f + 1.f / 1.f)))
        val e3 = 1.f - 2.f / (1.f - (1.f- (1.f+2.f)/2.f)/(2.f * 1.f))- (Const.PI/(6.f + x) + 2.f * Const.PI * k)/ x
        val e4 = Pow(2.f , k + 1.f / 2.f)
        RenderExpr(e4)
    }
}

fun shouldShowMultiplicationSign(left: Expr, right: Expr) : Boolean {
    return (left is Number && right is Number) ||
            (left is Number && right is Div && right.l is Number && right.r is Number) ||
            (left is Div && (right is Number || right is Div))
}