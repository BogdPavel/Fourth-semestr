;Лабораторная работа №3
;Выполнить набор арифметических операций над двумя целыми числами,
;представленными в 10-й системе счисления
;знаковые числа -32768...32767

output_str macro str 
    push ax
    mov dx, offset str
    mov ah, 09h
    int 21h
    pop ax
endm

.model small 

.stack 100h 

.data
    ;array - массив, в котором будут храниться числа 
    array dw 2 DUP(?)  
    output_buffer db 7 DUP('$') 
    overflow_message db 13, 10, 'Calculating overflow', 13, 10, '$'
    continue_message db 13, 10, 'Contunue?(y - yes, n - now)', 13, 10, '$'
    result_message db 13, 10, 'Result: $'
    first_message db 13, 10, 'Input first number', 13, 10, '$' 
    second_message db 13, 10, 'Input second number',13, 10, '$' 
    operation db 13, 10, 'Choose the operation:', 13, 10, 9, '+', 13, 10, 9, '-', 13, 10, 9, '*', 13, 10, 9, '/', 13, 10, 9, 'q - input new numbers', 13, 10, '$' 
    error  db 13, 10, 'ERROR!', 13, 10, '$'
    zero_error  db 13, 10, 'ERROR! DIVISION BY ZERO', 13, 10, '$'
    pak	 db 13, 10, 'Press any key...$'  
    exit_flag db 0
.code 
main:
    mov ax, @data
    mov ds, ax
    xor di, di
    jmp in_first_word_lp
    
continue: 
    xor ax, ax
    output_str continue_message 
    mov ah,8
    int 21h 
    cmp al, 'y'
    je in_first_word_lp
    cmp al, 'n'
    je exit
    jmp continue

in_first_word_lp:
    output_str first_message
    call input_dec_word    
    jnc write_first_in		       
    output_str error		             
    jmp in_first_word_lp
    
write_first_in:
    mov array[di], ax     
    
in_second_word_lp:
    output_str second_message
    call input_dec_word    
    jnc write_second_in		       
    output_str error		             
    jmp in_second_word_lp
    
write_second_in:
    mov array[di + 2], ax
    
oper_loop:    
    output_str operation
    call input_operation
    cmp exit_flag, 1
    je continue
    jmp oper_loop
    
exit: 
    output_str pak
    mov ah,8		    ;Функция DOS 08h - ввод символа без эха
    int 21h
    mov ax, 4c00h
    int 21h
     
input_dec_word PROC    
    push dx
    mov al, 7
    call input_str
    call str_to_sdec_word
    pop dx
    ret         
input_dec_word ENDP 

input_str PROC    
    push cx
    mov cx, ax
    mov ah, 0Ah
    mov [buffer], al
    mov [buffer + 1], 0
    lea dx, buffer
    int 21h
    mov al, [buffer + 1]
    add dx, 2
    mov ah, ch
    pop cx
    ret                         
input_str ENDP    
 
str_to_sdec_word PROC    
    push bx
    push dx
    test al, al
    jz stsdw_error
    mov bx, dx
    mov bl, [bx]
    cmp bl, '-'
    jne stsdw_no_sign
    inc dx
    dec al 
   
stsdw_no_sign:
    call str_to_udec_word
    jc stsdw_exit 
    cmp bl, '-'
    jne stsdw_plus
    cmp ax, 32768
    ja stsdw_error
    neg ax
    jmp stsdw_ok 
    
stsdw_plus:
    cmp ax,32767	    ;Положительное число должно быть не больше 32767
    ja stsdw_error	    ;Если больше (без знака), возвращаем ошибку    
    
stsdw_ok: 
    ;CF = 0
    clc 		         
    jmp stsdw_exit	
        
stsdw_error:  
    xor ax,ax
    ;CF = 1		    
    stc 
    		  
stsdw_exit:
    pop dx		    
    pop bx
    ret           
str_to_sdec_word ENDP 
 
str_to_udec_word PROC                      
    push cx		   
    push dx
    push bx
    push si
    push di

    mov si, dx		    ;SI = адрес строки
    mov di, 10		    ;DI = множитель 10 (основание системы счисления)
    xor cx, cx
    mov cl, al          ;CX = счётчик цикла = длина строки
    jcxz studw_error	;Если длина = 0, возвращаем ошибку
    xor ax, ax		    ;AX = 0
    xor bx, bx		    ;BX = 0

studw_lp:
    mov bl, [si] 	    ;Загрузка в BL очередного символа строки
    inc si		        ;Инкремент адреса
    cmp bl, '0'		    ;Если код символа меньше кода '0'
    jl studw_error	    ; возвращаем ошибку
    cmp bl, '9'		    ;Если код символа больше кода '9'
    jg studw_error	    ; возвращаем ошибку
    sub bl, '0'		    ;Преобразование символа-цифры в число
    mul di		        ;AX = AX * 10
    jc studw_error	    ;Если результат больше 16 бит - ошибка
    add ax, bx		    ;Прибавляем цифру
    jc studw_error	    ;Если переполнение - ошибка
    loop studw_lp	    ;Команда цикла
    jmp studw_exit	    ;Успешное завершение (здесь всегда CF = 0)

studw_error:
    xor ax, ax		    ;AX = 0
    stc 		        ;CF = 1 (Возвращаем ошибку)

studw_exit:
    pop di		    
    pop si
    pop bx
    pop dx
    pop cx
    ret                       
str_to_udec_word ENDP  

input_operation PROC
input_loop:
    push di        
    push ax
    push bx
    xor bx, bx
    xor ax, ax
    xor di, di
    mov ah, 8
    int 21h
    cmp al, 43
    je calculate_plus
    cmp al, 45
    je calculate_minus
    cmp al, 42
    je calculate_multiply
    cmp al, 47
    je calculate_divide
    cmp al, 'q'
    je quit
    output_str error
    jmp input_loop
    
calculate_plus:
    mov ax, array[di]
    add ax, array[di + 2]
    jo overflow
    jmp output_calculate 
    
calculate_minus:
    mov ax, array[di]
    sub ax, array[di + 2]
    jo overflow
    jmp output_calculate
    
calculate_multiply:
    mov ax, array[di]
    imul ax, array[di + 2]
    jo overflow
    jmp output_calculate
    
calculate_divide:
    
    cmp array[di + 2], 0
    je zero_divide
    mov ax, array[di]
    mov bx, array[di + 2]
    idiv bl
     
    cmp ah, 0
    jne overflow
    
    cmp al, 0
    jl invert
    
    jmp output_calculate            
    
invert: 
    not al
    inc al
    neg ax
    jmp output_calculate
    
overflow:
    output_str overflow_message
    jmp input_exit

zero_divide:
    output_str zero_error 
    jmp input_exit
    
quit:
    mov exit_flag, 1
    jmp input_exit    
    
output_calculate:
    call output_result 
    
input_exit:
    pop bx       
    pop ax
    pop di
    ret          
input_operation ENDP    
 
output_result PROC
    output_str result_message
    push di
    xor di, di		    
    call word_to_sdec_str 
    mov output_buffer[di], '$'  
    output_str output_buffer	    	
    pop di
    ret    
output_result ENDP 
 
word_to_sdec_str PROC
    push ax
    test ax,ax              ;Проверка знака AX
    jns wtsds_no_sign       ;Если >= 0, преобразуем как беззнаковое
                            ;Добавление знака в начало строки
    mov output_buffer[di], '-'
    inc di                  ;Инкремент DI
    neg ax                  ;Изменение знака значения AX
wtsds_no_sign:
    call word_to_udec_str   ;Преобразование беззнакового значения
    pop ax
    ret
word_to_sdec_str ENDP
 
word_to_udec_str PROC
    push ax
    push cx
    push dx
    push bx
    xor cx,cx               ;Обнуление CX
    mov bx,10               ;В BX делитель (10 для десятичной системы)
 
wtuds_lp1:                  ;Цикл получения остатков от деления
    xor dx,dx               ;Обнуление старшей части двойного слова
    div bx                  ;Деление AX=(DX:AX)/BX, остаток в DX
    add dl,'0'              ;Преобразование остатка в код символа
    push dx                 ;Сохранение в стеке
    inc cx                  ;Увеличение счетчика символов
    test ax,ax              ;Проверка AX
    jnz wtuds_lp1           ;Переход к началу цикла, если частное не 0.
 
wtuds_lp2:                  ;Цикл извлечения символов из стека
    pop dx                  ;Восстановление символа из стека
    mov output_buffer[di],dl;Сохранение символа в буфере
    inc di                  ;Инкремент адреса буфера
    loop wtuds_lp2          ;Команда цикла
 
    pop bx
    pop dx
    pop cx
    pop ax
    ret
word_to_udec_str ENDP
                     
end main       