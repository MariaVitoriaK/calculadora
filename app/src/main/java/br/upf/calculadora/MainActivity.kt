package br.upf.calculadora

import android.R.attr.color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import br.upf.calculadora.ui.theme.CalculadoraTheme
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraScreen()
        }
    }
}
@Composable
fun CalculadoraScreen() {
    var primeiroValor by rememberSaveable { mutableStateOf("")}
    var segundoValor by rememberSaveable { mutableStateOf("") }
    var operador by rememberSaveable { mutableStateOf("") }
    var displayText by rememberSaveable { mutableStateOf("0") }
    CalculadoraTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Display(displayText)
            Teclado { input ->
                when {
                    input in listOf("+", "-", "*", "/") -> {

                        if (primeiroValor.isNotEmpty()) {
                            operador = input
                        }
                    }
                    input == "=" -> {
                        if (primeiroValor.isNotEmpty() && segundoValor.isNotEmpty() && operador.isNotEmpty()) {
                            displayText = processarEntrada(primeiroValor, segundoValor, operador)
                            primeiroValor = displayText
                            segundoValor = ""
                            operador = ""
                        }
                    }
                    input == "C" -> {
                        primeiroValor = ""
                        segundoValor = ""
                        operador = ""
                        displayText = "0"
                    }
                    else -> {

                        if (operador.isEmpty()) {
                            primeiroValor += input
                            displayText = primeiroValor
                        } else {
                            segundoValor += input
                            displayText = segundoValor
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Display(displayText: String) {
    Text(
        text = displayText,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .background(Color(0xFFD3D3D3), RoundedCornerShape(16.dp)) // Fundo e bordas arredondadas
            .padding(24.dp)

    )
}

@Composable
fun Teclado(tratarOnClick: (String) -> Unit) {
    val botoes = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".","+", "C"),
        listOf("=", "%", "√")
    )

    Column {
        for (linha in botoes) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (botao in linha) {
                    if (botao == "=") {
                        Botao(
                            texto = botao,
                            tratarOnClick = tratarOnClick,
                            modifier = Modifier.weight(2f). background(Color(0xFFD3D3D3))
                        )
                    } else {
                        Botao(
                            texto = botao,
                            tratarOnClick = tratarOnClick ,
                            modifier = Modifier.weight(1f). background(Color(0xFFD3D3D3))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Botao(texto: String, tratarOnClick: (String) -> Unit,  modifier: Modifier = Modifier) {


   Button(
        onClick = {
            tratarOnClick(texto)
      },
      modifier = Modifier
           //.size(85.dp)
            .padding(5.dp)
             // colors = ButtonDefaults.buttonColors(
              //containerColor = Color(0xFFD3D3D3),
         ) {
      Text(
           text = texto,
            style = MaterialTheme.typography.titleLarge,
          modifier = Modifier.background(Color(0xFFD3D3D3)).padding(10.dp),

        )
  }
}

fun processarEntrada(primeiroValor: String?, segundoValor: String?, operador: String): String {
    if (primeiroValor.isNullOrEmpty() || segundoValor.isNullOrEmpty()) {
        return "Erro"
    }
    return try {
        when (operador) {
            "+" -> (primeiroValor.toDouble() + segundoValor.toDouble()).toString()
            "-" -> (primeiroValor.toDouble() - segundoValor.toDouble()).toString()
            "*" -> (primeiroValor.toDouble() * segundoValor.toDouble()).toString()

            "/" -> {
                if (segundoValor == "0") "Erro"
                else (primeiroValor.toDouble() / segundoValor.toDouble()).toString()
            }

            else -> "Erro"
        }
    } catch (e: Exception) {
        "Erro"
    }
}


