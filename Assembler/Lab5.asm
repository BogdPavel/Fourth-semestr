;написать программу сравнения каталогов по содержащимся в них набором файлов 

length EQU 11
 
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
    cmdLength             db ?
    cmdLine               db 128 DUP('$') 
    errorMessage          db 'Error!' , 13, 10, '$'
    okMessage             db 'Ok!' , 13, 10, '$'           
    path                  db '\\', 0
    firstCatalogPath      db 60 DUP (0) 
    search                db '*.*', 0
    newString             db 13, 10, '$'
    secondCatalogPath     db 60 DUP (0)
    exitMessage           db 13, 10, 'Press any key...', 13, 10, '$'
    notEqualNumberMessage db 13, 10, "Different number of files", '$'
    notEqualMessage       db 13, 10, "Catalogs are not equal", 13, 10, '$'
    emptyMessage          db 13, 10, "Catalogs are empty", 13, 10, '$'  
    equalMessage          db 13, 10, "Catalogs are equal", 13, 10, '$' 
    noPathMessage         db 'No path to work', 13, 10, '$'
    fileName              db 12 DUP ('$')
    bufferName            db 12 DUP ('$')
    clearString           db 17 DUP ('$')
    DTA                   db 128 DUP('$')
    count                 db 0
    currentFile           db 0 
    
.code

main PROC
    ;считывание командной строки 
    mov ax, @data
    mov es, ax
    
    xor cx, cx
    
    mov cl, ds:[80h]
    jcxz no_path
    
    dec cx
    mov cmdLength, cl
    
    mov si, 82h
    mov di, offset firstCatalogPath
    
write_first_path:
    cmp ds:[si], 0dh
    je no_path   
    movsb
    dec cx
    cmp ds:[si], ' '
    jne write_first_path
    je write_second_path
    
write_second_path:
    inc si
    dec cx
    mov di, offset secondCatalogPath
    repnz movsb     
    
    ;начало работы
    mov ax, @data
    mov ds, ax

    xor ax, ax 
    
    lea dx, DTA
    mov ah, 1Ah
    int 21h
    
    xor ax, ax     
    
    mov bx, offset firstCatalogPath
    call printCatalog 
    jnc error
    
    output_str newString
    mov al, count
    mov currentFile, al
    mov count, 0    
    
    mov bx, offset secondCatalogPath
    call printCatalog
    jnc error
    
    mov al, count
    cmp al, currentFile
    jne not_equal_number_of_files
     
    mov currentFile, 1 
    cmp al, 0
    je empty_catalogs
    dec count
     
    ;сравнение названий файлов 
compare_first:
    mov bx, offset firstCatalogPath 
    call writeFirstFile
    
    lea di, bufferName
    lea si, fileName
    mov cx, length
    rep movsb
    
    mov bx, offset secondCatalogPath
    call writeFirstFile
                       
    lea di, bufferName
    lea si, fileName
    mov cx, length
    repe cmpsb                   
    jne not_equal_files
    je compare_next
    
compare_next:    
    cmp count, 0
    je equal_files
    
compare_loop:
    mov bx, offset firstCatalogPath 
    call writeNextFiles
    
    lea di, bufferName
    lea si, fileName
    mov cx, length
    rep movsb
    
    mov bx, offset secondCatalogPath
    call writeNextFiles
                       
    lea di, bufferName
    lea si, fileName
    mov cx, length
    repe cmpsb                   
    jne not_equal_files
    
    inc currentFile
    dec count 
    cmp count, 0
    je equal_files
    jne compare_loop             
                  
not_equal_number_of_files:
    output_str notEqualNumberMessage
    output_str notEqualMessage
    jmp exit 
    
not_equal_files:
    output_str notEqualMessage
    jmp exit 
    
equal_files:
    output_str equalMessage
    jmp exit       
    
empty_catalogs:
    output_str emptyMessage
    jmp exit 
    
no_path:   
    mov ax, @data
    mov ds, ax
    output_str noPathMessage
    jmp exit           
       
error:
    output_str errorMessage
    jmp exit                
     
exit: 
    output_str exitMessage
    
    xor ax, ax
    int 16h
     
    mov ax, 4c00h
    int 21h            
  
main ENDP 

printCatalog PROC
    
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
    jc quit
    inc count
    
print_file_name:    
    mov dx, offset DTA
    add dx, 1eh
    mov ah, 09h       
    int 21h   
    output_str newString    
    
find_next:
    call clearDTA
    mov dx, offset DTA
    mov ah, 4fh
    int 21h
    jc quit
    jnc inc_count
    
inc_count:
    inc count
    jmp print_file_name    

quit:   
    pop cx
    pop dx
    pop ax
    
    ret
    
printCatalog ENDP

writeFirstFile PROC
                   
    push ax
    push dx
    push cx
    push di
    push si
                             
change_cat:
    mov dx, offset path
    mov ah, 3bh
    int 21h
                               
    mov dx, bx
    mov ah, 3bh
    int 21h
    
find_first_file:
    call clearDTA
    xor cx, cx
    mov ah, 4eh
    mov dx, offset search
    int 21h
    
    lea di, fileName
    lea si, DTA
    add si, 1eh
    mov cx, length
    rep movsb  
    
    pop si
    pop di
    pop cx
    pop dx
    pop ax
    
    ret             
                   
writeFirstFile ENDP  

writeNextFiles PROC
                   
    push ax
    push bx
    push cx
    push dx
    push di
    push si
                             
change_catlg:
    mov dx, offset path
    mov ah, 3bh
    int 21h
                               
    mov dx, bx
    mov ah, 3bh
    int 21h
    
    xor bx, bx
    mov bl, currentFile
    
find_frst_files:
    call clearDTA
    xor cx, cx
    mov ah, 4eh
    mov dx, offset search
    int 21h
      
find_next_file:
    call clearDTA
    mov dx, offset DTA
    mov ah, 4fh
    int 21h
    dec bl
    cmp bl, 0
    jne find_next_file      
      
    lea di, fileName
    lea si, DTA
    add si, 1eh
    mov cx, length
    rep movsb  
    
    pop si
    pop di
    pop dx
    pop cx
    pop bx
    pop ax
    
    ret                       
                   
writeNextFiles ENDP    

clearDTA PROC
     
    push si
    push di
    push cx
    
    lea di, DTA
    add di, 1eh
    lea si, clearString
    mov cx, 16
    rep movsb
     
    pop cx 
    pop di
    pop si
    
    ret         
             
clearDTA ENDP    