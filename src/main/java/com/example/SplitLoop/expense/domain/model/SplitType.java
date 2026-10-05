package com.example.SplitLoop.expense.domain.model;

public enum SplitType {

    /**
     * Todos pagan lo mismo.
     */
    EQUAL,

    /**
     * Cada participante tiene un porcentaje.
     */
    PERCENTAGE,

    /**
     * Cada participante tiene un importe fijo.
     */
    FIXED
}