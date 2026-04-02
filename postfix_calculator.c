#include <stdio.h>
#include <string.h>
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
        printf("stack is empty\n");
        return (element)0;
    }
    return (s->arr[(s->top)--]);
}

element postfix_calculator(Stack *s, char arr[]) {
    for(int i = 0; i < strlen(arr); i++) {
        if('0' <= arr[i] && arr[i] <='9') {
            push(s, arr[i] - '0');
        }
        else {
            element num2 = pop(s);
            element num1 = pop(s);
            switch(arr[i]) {
                case '+':
                    push(s, num1 + num2);
                    break;
                case '-':
                    push(s, num1 - num2);
                    break;
                case '*':
                    push(s, num1 * num2);
                    break;
                case '/':
                    push(s, num1 / num2);
                    break;
                
            }
        }
    }
    return pop(s);
}

int main() {

    Stack stack;
    Stack *s = &stack;
    init(s);

    char input[MAX_SIZE];
    scanf("%s", input);

    printf("%d", postfix_calculator(s, input));

    return 0;
}