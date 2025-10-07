package com.example.techmatch.datastructures;

public class Stack<T> {
    private Node<T> top;
    private int size;

    public Stack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (top == null) return null;
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        return top != null ? top.data : null;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }
}