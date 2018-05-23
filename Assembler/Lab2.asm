;Reverse each word in string
;hello my friends - olleh ym sdneirf
;o
;l
;l
;e
;h  

output_str macro str
    mov dx,offset str
    mov ah,09h
    int 21h
endm

.model small
.stack 100h
.data
    string db 79,80 DUP('$')
    input db 'Input your string(max 80 symbols): ',0dh,0ah,'$'  
    result db 0dh,0ah,'Result:',0dh,0ah,'$' 
.code
main:
    mov ax, @data
    mov ds,ax
    mov es, ax
     
    
    output_str input  
    
    mov ah,0ah
    lea dx,string
    int 21h
    
    mov si,2
    mov di,2
    jmp loop2 
    
loop1: 
    xor ax,ax
    pop ax
    mov string[di], al
    inc di
    cmp string[di],013              
    je string_reverse
    cmp di,si
    je increment
loop loop1
increment:
    inc di
    inc si    
loop2: 
    cmp string[si],013              
loope loop1
    cmp string[si],' '

loope loop1
    xor ax, ax
    mov al, string[si]
    push ax 
    inc si
    jmp loop2 
    
string_reverse:
    mov di,2
    xor ax,ax
    xor bx,bx
reverse_loop:    
    mov al,string[di]
    mov bl,string[si]
    mov string[si],al
    mov string[di],bl
    inc di
    dec si
    cmp di,si
    jl reverse_loop    
    
exit: 
    output_str result
                                 
    output_str string+2
    
    mov ax, 4c00h
    int 21h
    
end main