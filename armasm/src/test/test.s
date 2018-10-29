main 
	MOV r0, #0x4000000 
	MOV r1, #0x400 
	ADD r1, r1, #3 
	STR r1, [r0] 
	MOV r0, #0x6000000 
	MOV r1, #0xFF 
	MOV r2, #0x9600 
loop1 
	STRH r1, [r0], #2 
	SUBS r2, r2, #1 
	BNE loop1 
infin 
	B infin


