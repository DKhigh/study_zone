#include <stdio.h>
#define MAX_SIZE_LIST 10
typedef int element;

typedef struct list {
    element arr[MAX_SIZE_LIST];
    int length;
} List;

void list_init(List *list) {
    list->length = 0;
}

int is_full(List *list) {
    if(list->length >= MAX_SIZE_LIST) return 1;
    return 0;
}

int is_invalid_position(List *list, int position) {
    if(list->length < position || position < 0) return 1;
    return 0;
}

int get_length(List *list) {
    return list->length;
}

void add(List *list, int position, element value) {
    if(is_full(list)) {
        printf("list is full(add)\n");
        return;
    }
    if(is_invalid_position(list, position)) {
        printf("invailid position(add)\n");
        return ;
    }
    for(int i = get_length(list); i > position; i--) {
        list->arr[i] = list->arr[i-1];
    }
    list->arr[position] = value;
    list->length++;
}

void add_last(List *list, element value) {
    add(list, list->length, value);
}


element get_entry(List *list, int position) {
    return list->arr[position];
}

int main() {

    
    int i, n;
    List list;
    list_init(&list);

    add_last(&list, 1);
    add_last(&list, 2);
    add_last(&list, 3);
    add_last(&list, 4);
    add_last(&list, 5);
    add(&list, 2, 6);

    n = get_length(&list);
    printf("항목수는 %d입니다.\n", n);
    for(i = 0; i < n; i++) {
        printf("%d항목은 %d입니다.\n", i, get_entry(&list, i));
    }
    return 0;
}