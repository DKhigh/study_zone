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

int check_matching(Stack *s, char arr[]) {
    element temp;
    for(int i = 0; i < strlen(arr); i++) {
        switch(arr[i]) {
            case '(': case '{': case '[':
                push(s, arr[i]);
                break;
            case ')':
                temp = pop(s);
                if(temp != '(') {
                    return 0;
                }
                break;
            case '}':
                temp = pop(s);
                if(temp != '{') {
                    return 0;
                }
                break;
            case ']':
                temp = pop(s);
                if(temp != '[') {
                    return 0;
                }
                break;
        }
    }
    if(!is_empty(s)) {
        return 0;
    }
    return 1;
}

int main() {

    Stack stack;
    Stack *s = &stack;
    init(s);

    char input[MAX_SIZE];
    scanf("%s", input);

    if(check_matching(s, input)) {
        printf("success\n");
    }
    else {
        printf("false\n");
    }

    return 0;
}