#include <stdio.h>
#define MAX_SIZE 100
typedef int element;

typedef struct stack {
    element arr[MAX_SIZE];
    int top;
}Stack;

void init(Stack *s) {
    s->top = -1;
}

int is_full(Stack *s) {
    return (s->top == MAX_SIZE - 1);
}

int is_empty(Stack *s) {
    return (s->top == -1);
}

void push(Stack *s, element value) {
    if(is_full(s)) {
        printf("stack is full\n");
        return ;
    }
    s->arr[++(s->top)] = value;
}

element pop(Stack *s) {
    if(is_empty(s)) {
        printf("stack is empty");
        return 0;
    }
    return (s->arr[(s->top)--])
}

int main() {

    
    
    return 0;
}