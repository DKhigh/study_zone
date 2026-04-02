#include <stdio.h>
#include <string.h>
#define MAX_SIZE 100
typedef char element;

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

void infix_to_postfix(Stack *s, element arr[]) {
    element temp;
    for(int i = 0; i < strlen(arr); i++) {
        if('0' <= arr[i] && arr[i] <= '9') { //숫자일때
            printf("%c", arr[i]);
        }
        else { //부호일때
            switch(arr[i]) {
                case '(':
                    push(s, arr[i]);
                    break;

                case '+':
                case '-':
                    if(is_empty(s)) push(s, arr[i]);
                    while(!is_empty(s)) {
                        temp = pop(s);
                        if(temp == '*' || temp == '/') {
                            printf("%c", temp);
                        }
                        else {
                            push(s, arr[i]);
                            break;
                        }
                    }
                    break;

                case '*':
                case '/':
                    push(s, arr[i]);
                    break;

                case ')':
                    while(!is_empty(s)) {
                        temp = pop(s);
                        if(temp != '(') {
                            printf("%c", temp);
                        }
                        else {
                            break;
                        }
                    }     
                }      
        }
    }
    while(!is_empty(s)) {
        printf("%c", pop(s));
    }
}

int main() {

    Stack stack;
    Stack *s = &stack;
    init(s);

    element input[MAX_SIZE];
    scanf("%s", input);

    infix_to_postfix(s, input);

    return 0;
}