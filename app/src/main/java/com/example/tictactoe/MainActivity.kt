package com.example.tictactoe

import android.content.Intent
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.airbnb.lottie.LottieAnimationView
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class MainActivity : AppCompatActivity() {

    lateinit var buttons: Array<Array<ImageButton>>
    private lateinit var textViewPlayer1: TextView
    lateinit var textViewPlayer2: TextView

    private var player1Turn: Boolean = true
    private var roundCount: Int = 0
    private var player1Points: Int = 0
    private var player2Points: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewPlayer1 = findViewById(R.id.player1TextView)
        textViewPlayer2 = findViewById(R.id.player2TextView)

        buttons = Array(3) { r ->
            Array(3) { c ->
                initButtons(r, c)
            }
        }

        val btnReset: ImageButton = findViewById(R.id.btnReset)
        btnReset.setOnClickListener {
            player1Points = 0
            player2Points = 0
            updateScore()
            clearBoard()
        }
    }

    private fun initButtons(r: Int, c: Int): ImageButton {

        val btn: ImageButton = findViewById(resources.getIdentifier("btn$r$c", "id", packageName))

        btn.setOnClickListener {
            onBtnClick(btn)
        }
        return btn
    }

    private fun onBtnClick(btn: ImageButton) {

        if (btn.drawable != null) return

        if (player1Turn) {
            btn.setImageResource(R.drawable.x)
        } else {
            btn.setImageResource(R.drawable.o)
        }

        roundCount++

        if(checkForWin()){
            if(player1Turn) win(1) else win(2)
        }else if(roundCount == 9){
            draw()
        }else{
            player1Turn = !player1Turn
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array(3) { r ->
            Array(3) { c ->
                getField(buttons[r][c])
            }
        }

        for (i in 0..2){
            if ( (field[i][0] == field[i][1]) &&
                (field[i][0] == field[i][2]) &&
                (field[i][0] != null)
            )return true
        }

        for (i in 0..2){
            if ( (field[0][i] == field[1][i]) &&
                (field[1][i] == field[2][i]) &&
                (field[2][i] != null)
            )return true
        }

        for (i in 0..2) {
            if ((field[0][0] == field[1][1]) &&
                (field[0][0] == field[2][2]) &&
                (field[0][0] != null)
            ) return true
        }

        if ( (field[0][2] == field[1][1]) &&
            (field[0][2] == field[2][0]) &&
            (field[0][2] != null)
        )return true

        return false
    }

    private fun getField(btn: ImageButton) : Char? {
        val drw: Drawable? = btn.drawable
        val drwCross: Drawable? = ResourcesCompat.getDrawable(resources,R.drawable.x,null)
        val drwO: Drawable? = ResourcesCompat.getDrawable(resources,R.drawable.o,null)

        return when(drw?.constantState){
            drwCross?.constantState -> 'x'
            drwO?.constantState -> 'o'
            else -> null
        }
    }

    private fun win(player:Int){

        val l1:LottieAnimationView = findViewById(R.id.lottie1)
        val l2:LottieAnimationView = findViewById(R.id.lottie2)
        if(player == 1 ) player1Points++ else player2Points++

        l1.visibility = View.VISIBLE
        l2.visibility = View.VISIBLE

        Toast.makeText(applicationContext,"Player $player Won!", Toast.LENGTH_SHORT).show()
        updateScore()
        Handler().postDelayed(Runnable {
           clearBoard()
        },2000)
    }


    private fun clearBoard() {
        val l1:LottieAnimationView = findViewById(R.id.lottie1)
        val l2:LottieAnimationView = findViewById(R.id.lottie2)
        for (i in 0..2){
            for (j in 0..2){
                buttons[i][j].setImageResource(0)
            }
        }

        roundCount = 0
        player1Turn = true

        l1.visibility = View.GONE
        l2.visibility = View.GONE

    }

    private fun updateScore() {
        textViewPlayer1.text = "Player 1: $player1Points"
        textViewPlayer2.text = "Player 2: $player2Points"
    }

    private fun draw(){
        Toast.makeText(applicationContext,"Match Draw!", Toast.LENGTH_SHORT).show()
        clearBoard()
    }


}