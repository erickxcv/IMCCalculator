package com.erickmarques.imccalculator


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var bmi: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()


    } // fim do metodo onCreate

    private fun setListeners() {
        switchSistema.setOnCheckedChangeListener { buttonView, isChecked ->
            limparTextos()
            if(isChecked){
                //se o switch estiver 'checado' user quer sistema imperial
                bmi = true
                tvDigitePeso.text = resources.getText(R.string.digitePesoLb)
                tvPeso.text = resources.getText(R.string.libras)
                tvDigiteAltura.text = resources.getText(R.string.digiteAlturaPP)
                tvAltura.text = resources.getText(R.string.pes)
                etPolegadas.visibility = View.VISIBLE
                tvPolegadas.visibility = View.VISIBLE
                switchSistema.text = resources.getText(R.string.sistemaimperial)
            }else{
                //senao ele prefere o sistema metrico
                bmi = false
                tvDigitePeso.text = resources.getText(R.string.digitePesoKg)
                tvPeso.text = resources.getText(R.string.quilos)
                tvDigiteAltura.text = resources.getText(R.string.digiteAlturaM)
                tvAltura.text = resources.getText(R.string.metros)
                etPolegadas.visibility = View.INVISIBLE
                tvPolegadas.visibility = View.INVISIBLE
                switchSistema.text = resources.getText(R.string.sistemametrico)
            }
        }

        btLimpar?.setOnClickListener {
            limparTextos()
        }

        btCalcular?.setOnClickListener {
            var peso = etPeso!!.text.toString().toFloatOrNull()
            var altura = etAltura!!.text.toString().toFloatOrNull()
            if(bmi){
                var polegadas = etPolegadas!!.text.toString().toFloatOrNull()
                if(altura!=null && polegadas!=null){
                    altura = (altura*12)+polegadas
                }
            }
            if(peso!=null && altura!=null){
                if(altura>10 && !bmi){
                    altura/=100
                    /*
                    esse if verifica se o usuario digitou o valor de metros sem a virgula
                     */
                }
                calcularIMC(peso, altura)
            }else{
                Toast.makeText(this,"Valores de peso ou altura inválidos", Toast.LENGTH_LONG).show()
                limparTextos()
            }

        }
    }
    private fun limparTextos(){
        etPeso.text= null
        etAltura.text = null
        etPolegadas.text = null
        tvIMC.visibility = View.INVISIBLE
        tvClassificacao.visibility = View.INVISIBLE
    }
    private fun calcularIMC(peso: Float, altura:Float) {

            var imc = peso / (altura * altura)
            if(bmi) imc*=703
            classificarIMC(imc)
    }

    private fun classificarIMC(imc: Float){
        tvIMC.text = "%.2f".format(imc)
        tvIMC.visibility = View.VISIBLE
        tvClassificacao.visibility = View.VISIBLE
        if(imc<18.5){
            changeColorAndText(R.color.magreza, R.string.magreza)
            }
        else if(imc>=18.5 && imc<25){
            changeColorAndText(R.color.normal, R.string.normal)
        }
        else if (imc>=25 && imc<30){
            changeColorAndText(R.color.sobrepeso, R.string.sobrepeso)
        }
        else if(imc>=30 && imc<40){
            changeColorAndText(R.color.obesidade, R.string.obesidade)
        }
        else{
            changeColorAndText(R.color.obesidade_grave, R.string.obesidade_grave)
        }
        }

    private fun changeColorAndText(resultColor: Int, resultString: Int){
        tvIMC.setTextColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(this, resultColor))))
        tvClassificacao.setTextColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(this, resultColor))))
        tvClassificacao.text = resources.getText(resultString)
    }
}