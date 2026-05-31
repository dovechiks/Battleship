# ARMv7 Assembly Programming Cheatsheet

## Register Overview

| Register | Purpose | Alternate Name |
|----------|---------|----------------|
| R0-R3 | Argument registers / Return value | a1-a4 |
| R4-R11 | General purpose (callee-saved) | v1-v8 |
| R12 | Intra-procedure scratch | IP |
| R13 | Stack pointer | SP |
| R14 | Link register | LR |
| R15 | Program counter | PC |
| CPSR | Current program status | Flags register |

## Data Types & Sizes

| Type | Size | Notation |
|------|------|----------|
| Byte | 8 bits | .b |
| Halfword | 16 bits | .h or .s |
| Word | 32 bits | (default) |
| Doubleword | 64 bits | .d |

## Instruction Format

```
MNEMONIC{cond}{S} Rd, Rn, Op2
```

- **{cond}**: Conditional execution (EQ, NE, GT, LT, GE, LE, AL, etc.)
- **{S}**: Set condition codes flag (optional)
- **Rd**: Destination register
- **Rn**: First source operand
- **Op2**: Second operand (register or immediate)

## Arithmetic Instructions

| Instruction | Meaning | Format |
|-------------|---------|--------|
| ADD | Add | `ADD Rd, Rn, Op2` |
| ADC | Add with carry | `ADC Rd, Rn, Op2` |
| SUB | Subtract | `SUB Rd, Rn, Op2` |
| SBC | Subtract with carry | `SBC Rd, Rn, Op2` |
| RSB | Reverse subtract | `RSB Rd, Rn, Op2` |
| MUL | Multiply (32-bit result) | `MUL Rd, Rn, Rm` |
| MULL | Multiply long (64-bit) | `UMULL RdLo, RdHi, Rn, Rm` |
| DIV | Divide | `SDIV Rd, Rn, Rm` |

## Logical Instructions

| Instruction | Meaning | Format |
|-------------|---------|--------|
| AND | Bitwise AND | `AND Rd, Rn, Op2` |
| ORR | Bitwise OR | `ORR Rd, Rn, Op2` |
| EOR | Bitwise XOR | `EOR Rd, Rn, Op2` |
| BIC | Bit clear (AND NOT) | `BIC Rd, Rn, Op2` |
| NOT | Bitwise NOT (using MVN) | `MVN Rd, Op2` |
| LSL | Logical shift left | `LSL Rd, Rn, shift` |
| LSR | Logical shift right | `LSR Rd, Rn, shift` |
| ASR | Arithmetic shift right | `ASR Rd, Rn, shift` |
| ROR | Rotate right | `ROR Rd, Rn, shift` |

## Data Transfer Instructions

| Instruction | Meaning | Format |
|-------------|---------|--------|
| MOV | Move | `MOV Rd, Op2` |
| MOVW | Move 16-bit immediate | `MOVW Rd, #imm16` |
| MOVT | Move 16-bit immediate to top | `MOVT Rd, #imm16` |
| LDR | Load register (word) | `LDR Rd, [Rn]` |
| LDRB | Load register byte | `LDRB Rd, [Rn]` |
| LDRH | Load register halfword | `LDRH Rd, [Rn]` |
| STR | Store register (word) | `STR Rd, [Rn]` |
| STRB | Store register byte | `STRB Rd, [Rn]` |
| STRH | Store register halfword | `STRH Rd, [Rn]` |
| LDM | Load multiple | `LDM Rn, {reg_list}` |
| STM | Store multiple | `STM Rn, {reg_list}` |

## Comparison & Test Instructions

| Instruction | Meaning | Format |
|-------------|---------|--------|
| CMP | Compare | `CMP Rn, Op2` |
| CMN | Compare negative | `CMN Rn, Op2` |
| TST | Test bits | `TST Rn, Op2` |
| TEQ | Test equivalence | `TEQ Rn, Op2` |

## Branch Instructions

| Instruction | Meaning | Format |
|-------------|---------|--------|
| B | Branch | `B label` |
| BL | Branch with link | `BL function` |
| BX | Branch and exchange | `BX Rm` |
| BLX | Branch, link & exchange | `BLX Rm` |
| BEQ | Branch if equal | `BEQ label` |
| BNE | Branch if not equal | `BNE label` |
| BGT | Branch if greater than | `BGT label` |
| BLT | Branch if less than | `BLT label` |
| BGE | Branch if >= | `BGE label` |
| BLE | Branch if <= | `BLE label` |

## Condition Codes

| Code | Meaning | Condition |
|------|---------|-----------|
| EQ | Equal | Z = 1 |
| NE | Not equal | Z = 0 |
| CS/HS | Carry set/unsigned >= | C = 1 |
| CC/LO | Carry clear/unsigned < | C = 0 |
| MI | Minus/negative | N = 1 |
| PL | Plus/positive | N = 0 |
| VS | Overflow set | V = 1 |
| VC | Overflow clear | V = 0 |
| HI | Unsigned higher | C=1 and Z=0 |
| LS | Unsigned lower/same | C=0 or Z=1 |
| GE | Signed >= | N = V |
| LT | Signed < | N ≠ V |
| GT | Signed > | Z=0 and N=V |
| LE | Signed <= | Z=1 or N≠V |
| AL | Always (default) | - |

## Addressing Modes

| Mode | Format | Description |
|------|--------|-------------|
| Immediate | `#value` | Direct constant value |
| Register | `Rm` | Value in register |
| Register shifted | `Rm, LSL #n` | Register shifted by constant |
| Scaled register | `Rm, LSL Rs` | Register shifted by register |
| Pre-indexed | `[Rn, #offset]!` | Address: Rn+offset, writeback |
| Post-indexed | `[Rn], #offset` | Address: Rn, then Rn←Rn+offset |
| PC-relative | `label` or `[PC, #offset]` | Relative to program counter |

## Immediate Value Encoding

- 8-bit value, rotated right by even number of bits (0, 2, 4, ..., 30)
- Valid range: 0-255 rotated
- Examples: `#0xFF`, `#0x100` (invalid - not encodable directly)

## Stack Operations

```assembly
PUSH {R4, R5, LR}       ; Push registers to stack
POP  {R4, R5, PC}       ; Pop registers from stack
; Equivalent to:
STM SP!, {R4, R5, LR}
LDM SP!, {R4, R5, PC}
```

## Function Calling Convention (EABI)

| Aspect | Rule |
|--------|------|
| Arguments | Passed in R0-R3 (rest on stack) |
| Return value | R0 (R1 for 64-bit) |
| Callee-saved | R4-R11, SP, LR (when returning) |
| Caller-saved | R0-R3, R12 |
| Stack alignment | 8-byte aligned on entry |

## Common Patterns

### Load 32-bit immediate
```assembly
MOVW R0, #0x1234       ; Load lower 16 bits
MOVT R0, #0x5678       ; Load upper 16 bits
```

### Loop structure
```assembly
MOV R0, #0             ; Initialize counter
LOOP:
    ; loop body
    ADD R0, R0, #1
    CMP R0, #10
    BLT LOOP
```

### Conditional execution
```assembly
CMP R0, R1
MOVEQ R2, #1           ; Execute if equal
MOVNE R2, #0           ; Execute if not equal
```

### Function call
```assembly
MOV R0, #5             ; First argument
MOV R1, #10            ; Second argument
BL my_function         ; Call function (LR=return address)
; Function returns, result in R0
```

### Save/restore registers
```assembly
PUSH {R4, R5, LR}       ; Save at function start
; ... function body ...
POP  {R4, R5, PC}       ; Restore and return
```

## Useful Directives

| Directive | Purpose | Example |
|-----------|---------|---------|
| .global | Make symbol global | `.global main` |
| .section | Define section | `.section .text` |
| .data | Data section | `.data` |
| .word | 32-bit value | `.word 0x12345678` |
| .byte | 8-bit value | `.byte 0xFF` |
| .ascii | String (no null) | `.ascii "hello"` |
| .asciz | String (null-term) | `.asciz "hello"` |
| .align | Alignment | `.align 4` |
| .equ | Define constant | `.equ PI, 3` |

## Memory Considerations

- **Little-endian**: Least significant byte at lowest address (ARM default)
- **Word alignment**: 32-bit values should be at addresses divisible by 4
- **Stack grows downward**: PUSH decrements SP, POP increments SP
- **SP alignment**: Maintain 8-byte alignment for stability

## Common Mistakes to Avoid

1. **MOV immediates**: Can't use arbitrary 32-bit values; use MOVW/MOVT
2. **Register count**: BX/BLX only work with registers, not labels
3. **Condition codes**: S suffix needed to set flags for conditional branches
4. **Stack alignment**: Misaligned SP causes crashes in function calls
5. **Register preservation**: Violating EABI conventions breaks function calls
6. **Offset ranges**: LDR/STR offsets limited to ±4095 bytes
7. **Shift operand**: Only R0 can be used as shift amount for most instructions

## Useful System Calls (Linux ARM)

| Number | Name | Purpose |
|--------|------|---------|
| 1 | exit | Exit program |
| 4 | write | Write to file |
| 5 | open | Open file |
| 6 | close | Close file |

---

**Good luck on your exam!** This cheatsheet covers the essentials for ARMv7 assembly. Review the instruction formats and addressing modes thoroughly.

**To convert to PDF:**
1. Go to your repository and view this file
2. Use browser print function (Ctrl+P or Cmd+P)
3. Select "Save as PDF"
4. Or use online markdown-to-PDF converters
