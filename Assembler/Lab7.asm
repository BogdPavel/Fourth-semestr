;открыть все программы в каталоге, переданном через командную строку
length EQU 11

output_str macro str 
    push ax
    mov dx, offset str
    mov ah, 09h
    int 21h
    pop ax
endm

.model small

stack segment dw 128 DUP(0) ends    

.data
cmdLength db ?
cmdLine db 128 DUP(0)
path db '\\', 0
exitMessage db 13, 10, 'Press any key...', 13, 10, '$'
noPathMessage db 'No path to work', 13, 10, '$'
errorMessage db 13, 10, 'Error!', 13, 10, '$'
findErrorMessage db 13, 10, 'Can not find file. Error!', 13, 10, '$'
search db '*.*', 0
appName db 11 DUP ('$'), 1 DUP (0)
newString db 13, 10, '$'
DTA db 128 DUP('$')
clearDTAString db 17 DUP ('$')
clearNameString  db 12 DUP (0)
EPB dw 0000
    dw offset commandLine
    dw 005ch, 0, 006ch, 0
    commandLine db 125
    db " /?"
    command_text db 122 dup(?)
EPBlen dw $ - EPB    
dataSize = $ - cmdLength                   
                                    
.code
main:
    ;освобождение память под програмыы
    mov bx, ((codeSize/16)+1)+((dataSize/16)+1)+32
    mov ah, 4ah
    int 21h
    ;считывание командной строки 
    mov ax, @data
    mov es, ax
    
    xor cx, cx
    
    mov cl, ds:[80h]
    jcxz no_path
    
    push cx
    dec cx
    
    mov si, 82h
    mov di, offset cmdLine
    repnz movsb
    
    pop cx
    mov cmdLength, cl
    dec cmdLength
    
    ;начало работы
    mov ax, @data
    mov ds, ax
    
    xor ax, ax
    ;переопределение DTA
    lea dx, DTA
    mov ah, 1Ah
    int 21h   
    
    xor ax, ax
    
    mov bx, offset cmdLine
    call workWithCatalog
    jc exit        
    
no_path:
    mov ax, @data
    mov ds, ax
    output_str noPathMessage
    jmp exit       
            
exit:          
    output_str exitMessage
    
    xor ax, ax
    int 16h
      
    mov ax, 4c00h
    int 21h
     

workWithCatalog PROC
            
    push ax
    push dx
    push cx
    
change_catalog:
    mov dx, offset path
    mov ah, 3bh
    int 21h
    jc quit
                               
    mov dx, bx
    mov ah, 3bh
    int 21h
    jc quit
    
find_first:
    call clearDTA
    xor cx, cx
    mov ah, 4eh
    mov dx, offset search
    int 21h
    jc find_error
    call writeAppName
    ;call openApp
    
find_next:
    call clearDTA
    mov dx, offset DTA
    mov ah, 4fh
    int 21h
    jc find_error
    jnc open_app
    
open_app:
    call writeAppName
    call openApp
    jmp find_next    

find_error: 
    output_str findErrorMessage
    call numberConvert
    
quit:
    pop cx
    pop dx
    pop ax
    
    ret                              
            
workWithCatalog ENDP

openApp PROC
    
    push ax
    push bx
    push dx
    
    mov dx, offset appName
    mov bx, offset EPB
    mov ax, 4b00h     
    int 21h 
    
    jc mistake_output
    jnc return
             
mistake_output:
    call numberConvert
    jmp return            
   
return:
    pop dx
    pop bx
    pop ax
    
    ret       
      
openApp ENDP

writeAppName PROC
    
    push cx
    push di
    push si
    
    lea di, appName
    lea si, clearNameString
    mov cx, length 
    rep movsb
            
    lea di, appName
    lea si, DTA
    add si, 1eh
    mov cx, length
    rep movsb 
    
    output_str newString
    output_str appName
    output_str newString    
    
    pop si
    pop di
    pop cx
    
    ret        
            
writeAppName ENDP    

clearDTA PROC
     
    push si
    push di
    push cx
    
    lea di, DTA
    add di, 1eh
    lea si, clearDTAString
    mov cx, 16
    rep movsb
     
    pop cx 
    pop di
    pop si
    
    ret         
             
clearDTA ENDP

numberConvert PROC
    
    push ax
    push bx
    push cx
    push dx
    
    mov bx, 16
    xor cx, cx
    
again:    
    xor dx, dx
    
    div bx
    inc cx
    push dx 
    cmp ax, 0
    jnz again
    
loop_output:    
    pop dx
    add dx, 30h
    cmp dx, 39h
    jle no_more_9
    add dx, 7
    
no_more_9:
    mov ah, 2
    int 21h
    loop loop_output
    
    pop dx
    pop cx
    pop bx 
    pop ax
    
    ret    
    
numberConvert ENDP

codeSize = $ - main

end main