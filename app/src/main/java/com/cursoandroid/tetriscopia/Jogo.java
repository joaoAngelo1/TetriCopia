package com.cursoandroid.tetriscopia;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.Random;

public class Jogo extends AppCompatActivity {
    private GridLayout grid;
    private Handler handler;
    private Runnable runable;
    private ImageView[][] botoes;
    private int[][] campo;
    private Typeface fonte;
    private Random random;
    private GerarPecas gerador;
    private MediaPlayer somDeFundo;
    private ImageView setaEsquerda;
    private ImageView setaDireita;
    private ImageView botaoGirar;
    private ImageView botaoDescer;
    private int temp;
    private int[] coordenadasPecas;
    private int peca;
    private TextView score;
    private TextView pontuacao;
    private boolean limpandoLinha;


    @Override
    protected void onStop() {
        super.onStop();
        somDeFundo.stop();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        grid = findViewById(R.id.gridLayout);
        botoes = new ImageView[20][10];
        fonte = ResourcesCompat.getFont(this, R.font.fonte_tetris);
        campo = new int[20][10];
        random = new Random();
        gerador = new GerarPecas();
        handler = new Handler();
        somDeFundo = new MediaPlayer();
        somDeFundo = MediaPlayer.create(getApplicationContext(), R.raw.tetris_theme_song);
        setaDireita = findViewById(R.id.setaDireitaId);
        setaEsquerda = findViewById(R.id.setaEsquerdaId);
        botaoGirar = findViewById(R.id.botaoGirarId);
        botaoDescer = findViewById(R.id.botaoDescerId);
        score = findViewById(R.id.txtScoreId);
        pontuacao = findViewById(R.id.scoreJogadorId);
        score.setTypeface(fonte);
        pontuacao.setTypeface(fonte);
        temp = 500;
        peca = 0;
        somDeFundo.start();

        boolean limpandoLinha  = false;
        //no tetris o campo tem 10 de largura por 20 de altura
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                ImageView temp = new ImageView(this);
                temp.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fundo_jogo));
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();

                layoutParams.height = 0;
                layoutParams.width = 0;
                layoutParams.rowSpec = GridLayout.spec(i, 1f);
                layoutParams.columnSpec = GridLayout.spec(j, 1f);
                layoutParams.setGravity(0);
                temp.setLayoutParams(layoutParams);
                grid.addView(temp);
                botoes[i][j] = temp;
            }
        }
        //sorteia a primeira peça depois se houver colisao cai as peças
            coordenadasPecas = sortearPeca(coordenadasPecas);
            runable = new Runnable() {
                @Override
                public void run() {
                    if (!limpandoLinha) {
                        if (colisaoFundo() && !colisaoPecas()) {
                            descerPeca(coordenadasPecas);
                        } else {
                            coordenadasPecas = sortearPeca(coordenadasPecas);
                        }
                        atualizaCores();
                        verificarCampo();
                        handler.postDelayed(this, temp);
                    }
                }
            };
            handler.post(runable);


        botaoDescer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            temp -= 450;
                            break;
                        case MotionEvent.ACTION_UP:
                            temp += 450;
                            break;
                    }
                    return true;
                }
            });

            setaEsquerda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(colisaoParedeEsquerda()) {
                        moverEsquerda();
                    }
                }
            });

            setaDireita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(colisaoParedeDireita()) {
                        moverDireita();
                    }
                }
            });
            botaoGirar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    girarMatriz(peca);
                }
            });


    }


    //retorna a matriz 3*3 correspondente a figura no seu modo padrao e
    // atribui as coordenadas a figura
    public int[] sortearPeca(int coordenadas[]) {
        int[] figura;
        peca = random.nextInt(7) + 1;
        figura = gerador.gerarFigura(peca);
        coordenadas = new int[figura.length];
        for (int i = 0; i < figura.length; i++) {
            campo[figura[i] / 10][(figura[i] % 10) + 3] = peca;
        }
        for (int i = 0; i < figura.length; i++) {
                coordenadas[i] = figura[i] + 3;
        }
        return coordenadas;
    }
    //funcao que controla o descer das pecas
    public void descerPeca(int coordenadas[]){
        zerarRastro();
            for (int i = 0; i < coordenadas.length; i++) {
                coordenadas[i] += 10;
                    campo[(coordenadas[i] / 10)][coordenadas[i] % 10] = peca;
            }
    }
    public void atualizaCores(){
        for(int i=0; i<20; i++){
            for(int j=0; j<10; j++){
                switch(campo[i][j]){
                    case 0:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fundo_jogo));
                        break;
                    case 1:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cor_roxa));
                        break;
                    case 2:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cor_vermelho));
                        break;
                    case 3:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cor_azul_claro));
                        break;
                    case 4:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cor_laranja));
                        break;
                    case 5:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cor_azul));
                        break;
                    case 6:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cor_verde));
                        break;
                    case 7:
                        botoes[i][j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cor_amarela));
                        break;

                }
            }
        }
    }
    public void moverDireita(){
        zerarRastro();
        for (int i = 0; i < coordenadasPecas.length; i++) {
            coordenadasPecas[i]++;
        }
    }
    public void moverEsquerda(){
        zerarRastro();
        for (int i = 0; i < coordenadasPecas.length; i++) {
            coordenadasPecas[i]--;
        }
    }
    public void zerarRastro(){
        for(int i=0; i<coordenadasPecas.length; i++){
            campo[(coordenadasPecas[i]/10)][(coordenadasPecas[i]%10)] = 0;
        }
    }
    //entra com o tipo da peca, com execeção da 2 e 6 as outras representam uma
    //matriz 3x3
    public void girarMatriz(int tipo) {
        int menorLinha;
        int menorColuna;
        switch(tipo) {
            case 3:
                zerarRastro();
                menorLinha = 99999999;
                menorColuna = 9999999;
                // Encontrar as menores coordenadas para centralizar a rotação
                for (int i = 0; i < coordenadasPecas.length; i++) {
                    int linha = coordenadasPecas[i] / 10;
                    int col = coordenadasPecas[i] % 10;
                    if (linha < menorLinha) menorLinha = linha;
                    if (col < menorColuna) menorColuna = col;
                }

                // Determinar se a peça está horizontal ou vertical
                boolean isHorizontal = coordenadasPecas[0] / 10 == coordenadasPecas[1] / 10;

                // Atualizar a peça para a nova orientação
                for (int i = 0; i < 4; i++) {
                    if (isHorizontal) {
                        coordenadasPecas[i] = (menorLinha + i) * 10 + menorColuna; // Transforma em vertical
                    } else {
                        coordenadasPecas[i] = menorLinha * 10 + (menorColuna + i); // Transforma em horizontal
                    }
                    campo[coordenadasPecas[i] / 10][coordenadasPecas[i] % 10] = tipo;
                }
                atualizaCores();
                return;
            case 7:
                return;
            default:
                zerarRastro();
                int[][] matriz = new int[3][3];
                //menor coordenada da linha fica em cima
                menorLinha = 999999;
                menorColuna = 999999;
                for (int i = 0; i < coordenadasPecas.length; i++) {
                    int linha = coordenadasPecas[i] / 10;
                    int col = coordenadasPecas[i] % 10;
                    if (menorLinha > linha) menorLinha = linha;
                    if (menorColuna > col) menorColuna = col;
                } //marcando o desenho da figura
                for (int k = 0; k < coordenadasPecas.length; k++) {
                    int linha = (coordenadasPecas[k] / 10) - menorLinha;
                    int col = (coordenadasPecas[k] % 10) - menorColuna;
                    if (linha >= 0 && linha < 3 && col >= 0 && col < 3) {
                        matriz[linha][col] = 1;
                    }
                }//girando a matriz propriamente
                int[][] rotacionada = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (matriz[i][j] == 1) {
                            rotacionada[j][2 - i] = 1;
                        }
                    }
                }//transferindo as informações novamente para o vetor
                int k = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (rotacionada[i][j] == 1) {
                            coordenadasPecas[k++] = (i + menorLinha) * 10 + (menorColuna + j);
                            campo[i + menorLinha][j + menorColuna] = tipo;

                        }
                    }
                }
                atualizaCores();
                break;

        }
    }


    //colide no fundo se for falso
    public boolean colisaoFundo(){
        for(int i=0; i<coordenadasPecas.length; i++){
            if(coordenadasPecas[i]/10 >= 19) {
                return false;
            }
        }
        return true;
    }
    //erro estava é que eu tenho que pegar a maior linha e verificar se a posterior está ocupada
    public boolean colisaoPecas(){
        int maiorL = -1;
        int ocorrencias = 0;
        for(int i=0; i<coordenadasPecas.length; i++){
            if(maiorL <coordenadasPecas[i]/10)
                maiorL = coordenadasPecas[i]/10;
        }
        for(int i=0; i<coordenadasPecas.length; i++){
            if(coordenadasPecas[i]/10 == maiorL)
                ocorrencias++;
        }
        int[] p = new int[ocorrencias];
        int x=0;
        for(int i=0; i<coordenadasPecas.length; i++){
            if(coordenadasPecas[i]/10 == maiorL)
                p[x++] = coordenadasPecas[i];
        }
        for(int i=0; i<p.length; i++){
            if(campo[(p[i]/10)+1][p[i]%10]!=0){
                return true;
            }
        }
        return false;
    }
    public boolean colisaoParedeDireita(){
        for(int i=0; i<coordenadasPecas.length; i++) {
            if ((coordenadasPecas[i] % 10)+1 >= 10)
                return false;
        }
        return true;
    }
    public boolean colisaoParedeEsquerda(){
        for(int i=0; i<coordenadasPecas.length; i++) {
            if ((coordenadasPecas[i]%10)-1<0)
                return false;
        }
        return true;
    }
    public void verificarCampo(){
        for(int i=0; i<campo.length; i++){
            verificarLinha(i);
        }
    }
    public void removerLinha(int linha){
        limpandoLinha = true;
        final int[] x = {5};
        final int[] y = {4};
        Handler hand = new Handler();
        Runnable runn;
        runn= new Runnable() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void run() {
                campo[linha][x[0]++] = 0;
                campo[linha][y[0]--] = 0;
                if(x[0]<=9 && y[0]>=0) {
                    hand.postDelayed(this, 70);
                }else{
                    String p = String.valueOf(pontuacao.getText());
                    int aumento = Integer.parseInt(p) + 100;
                    pontuacao.setText(String.valueOf(aumento));
                    descerLinhasAcima(linha);
                    limpandoLinha = false;
                }
            }
        };
        hand.post(runn);

    }
    public void descerLinhasAcima(int linhaRemovida) {
        for (int i = linhaRemovida; i > 0; i--) {
            for (int j = 0; j < campo[0].length; j++) {
                campo[i][j] = campo[i - 1][j];
            }
        }
        // Limpar a linha superior
        for (int j = 0; j < campo[0].length; j++) {
            campo[0][j] = 0;
        }
    }

    public void verificarLinha(int linha){
        for(int j=0; j<campo[0].length; j++){
            if(campo[linha][j]==0) {
                return;
            }
        }
        removerLinha(linha);
    }

}