;������������ ������ �3
;��������� ����� �������������� �������� ��� ����� ������ �������,
;��������������� � 10-� ������� ���������
;�������� ����� -32768...32767

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
    ;array - ������, � ������� ����� ��������� ����� 
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
    mov ah,8		    ;������� DOS 08h - ���� ������� ��� ���
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
    cmp ax,32767	    ;������������� ����� ������ ���� �� ������ 32767
    ja stsdw_error	    ;���� ������ (��� �����), ���������� ������    
    
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

    mov si, dx		    ;SI = ����� ������
    mov di, 10		    ;DI = ��������� 10 (��������� ������� ���������)
    xor cx, cx
    mov cl, al          ;CX = ������� ����� = ����� ������
    jcxz studw_error	;���� ����� = 0, ���������� ������
    xor ax, ax		    ;AX = 0
    xor bx, bx		    ;BX = 0

studw_lp:
    mov bl, [si] 	    ;�������� � BL ���������� ������� ������
    inc si		        ;��������� ������
    cmp bl, '0'		    ;���� ��� ������� ������ ���� '0'
    jl studw_error	    ; ���������� ������
    cmp bl, '9'		    ;���� ��� ������� ������ ���� '9'
    jg studw_error	    ; ���������� ������
    sub bl, '0'		    ;�������������� �������-����� � �����
    mul di		        ;AX = AX * 10
    jc studw_error	    ;���� ��������� ������ 16 ��� - ������
    add ax, bx		    ;���������� �����
    jc studw_error	    ;���� ������������ - ������
    loop studw_lp	    ;������� �����
    jmp studw_exit	    ;�������� ���������� (����� ������ CF = 0)

studw_error:
    xor ax, ax		    ;AX = 0
    stc 		        ;CF = 1 (���������� ������)

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
    test ax,ax              ;�������� ����� AX
    jns wtsds_no_sign       ;���� >= 0, ����������� ��� �����������
                            ;���������� ����� � ������ ������
    mov output_buffer[di], '-'
    inc di                  ;��������� DI
    neg ax                  ;��������� ����� �������� AX
wtsds_no_sign:
    call word_to_udec_str   ;�������������� ������������ ��������
    pop ax
    ret
word_to_sdec_str ENDP
 
word_to_udec_str PROC
    push ax
    push cx
    push dx
    push bx
    xor cx,cx               ;��������� CX
    mov bx,10               ;� BX �������� (10 ��� ���������� �������)
 
wtuds_lp1:                  ;���� ��������� �������� �� �������
    xor dx,dx               ;��������� ������� ����� �������� �����
    div bx                  ;������� AX=(DX:AX)/BX, ������� � DX
    add dl,'0'              ;�������������� ������� � ��� �������
    push dx                 ;���������� � �����
    inc cx                  ;���������� �������� ��������
    test ax,ax              ;�������� AX
    jnz wtuds_lp1           ;������� � ������ �����, ���� ������� �� 0.
 
wtuds_lp2:                  ;���� ���������� �������� �� �����
    pop dx                  ;�������������� ������� �� �����
    mov output_buffer[di],dl;���������� ������� � ������
    inc di                  ;��������� ������ ������
    loop wtuds_lp2          ;������� �����
 
    pop bx
    pop dx
    pop cx
    pop ax
    ret
word_to_udec_str ENDP
                     
end main       