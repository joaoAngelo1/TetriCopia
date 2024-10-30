package com.cursoandroid.tetriscopia;

public enum Pecas {

    A1(11),B1(21),B2(22), // peca que representa 2
    C1(31),C2(32),
    D1(41), D2(42),D3(43), D4(43),
    E1(51),E2(52),E3(53),E4(54),
    F1(61), F2(62),
    G1(71);


    private int peca;

    Pecas(int peca){
        this.peca = peca;
    }

    public int retornarPecas(){
        return peca;
    }
}
