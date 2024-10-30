package com.cursoandroid.tetriscopia;

public class GerarPecas {
    private int[] figura;
    private int tipo;


    public GerarPecas(){
    }

    public int[] getFigura(){
        return figura;
    }

    public void setTipo(int tipo){
        this.tipo = tipo;
    }
    public int getTipo(){
        return tipo;
    }
    //receba a opcao gerada de maneira randomica
    public int[] gerarFigura(int x){
        setTipo(x);

        switch(x) {
            case 1:
                //gera a figura 1
                figura= new int[]{1,10,11,12};
            break;
            case 2:
                //gera a figura 2
                figura = new int[]{10,11,21,22};
                break;
            case 3:
                //gera a figura 3
                figura = new int[]{2,12,22,32};
                break;
            case 4:
                //gera a figura 3
                figura = new int[]{12,20,21,22};
                break;
            case 5:
                //gera a figura 4
                figura = new int[]{0,1,10,20};
                break;
            case 6:
                figura = new int[]{11,12,20,21};
                break;
            case 7:
                figura = new int[]{11,12,21,22};
                break;
        }
        return figura;
    }

}
