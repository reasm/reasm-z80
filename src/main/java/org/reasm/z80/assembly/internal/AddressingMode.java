package org.reasm.z80.assembly.internal;

import javax.annotation.concurrent.Immutable;

/**
 * The addressing mode of an effective address.
 *
 * @author Francis Gagné
 */
@Immutable
enum AddressingMode {

    // B
    REGISTER_B(0),

    // C
    REGISTER_C(1),

    // D
    REGISTER_D(2),

    // E
    REGISTER_E(3),

    // H
    REGISTER_H(4),

    // L
    REGISTER_L(5),

    // (HL)
    REGISTER_HL_INDIRECT(6),

    // A
    REGISTER_A(7),

    // BC
    REGISTER_BC,

    // DE
    REGISTER_DE,

    // HL
    REGISTER_HL,

    // SP
    REGISTER_SP,

    // (BC)
    REGISTER_BC_INDIRECT,

    // (DE)
    REGISTER_DE_INDIRECT,

    // (SP)
    REGISTER_SP_INDIRECT,

    // IX
    REGISTER_IX(AddressingMode.IX),

    // IY
    REGISTER_IY(AddressingMode.IY),

    // (IX)
    REGISTER_IX_INDIRECT(AddressingMode.IX),

    // (IY)
    REGISTER_IY_INDIRECT(AddressingMode.IY),

    // (IX+d)
    REGISTER_IX_INDEXED(AddressingMode.IX),

    // (IY+d)
    REGISTER_IY_INDEXED(AddressingMode.IY),

    // AF
    REGISTER_AF,

    // AF'
    REGISTER_AF_ALTERNATE,

    // I
    REGISTER_I,

    // R
    REGISTER_R,

    // (C)
    REGISTER_C_INDIRECT,

    // n, nn
    IMMEDIATE,

    // (n), (nn)
    IMMEDIATE_INDIRECT;

    // These constants are accessed from the enum member initializers.
    // Normally, this isn't allowed, but we can work around that limitation
    // by qualifying them with the enum's name.
    // These constants are of type int, so their value is inlined.
    // If they weren't inlined, this trick wouldn't work.
    private static final int IX = 0xDD;
    private static final int IY = 0xFD;

    final int value;

    private AddressingMode() {
        this(-1);
    }

    /**
     * Initializes a new AddressingMode.
     *
     * @param value
     *            a value associated with the addressing mode. The meaning of this value depends on the addressing mode.
     */
    private AddressingMode(int value) {
        this.value = value;
    }

    final boolean isCommon() {
        switch (this) {
        case REGISTER_B:
        case REGISTER_C:
        case REGISTER_D:
        case REGISTER_E:
        case REGISTER_H:
        case REGISTER_L:
        case REGISTER_HL_INDIRECT:
        case REGISTER_A:
            return true;

        default:
            return false;
        }
    }

    final boolean isIndex() {
        switch (this) {
        case REGISTER_IX:
        case REGISTER_IY:
            return true;

        default:
            return false;
        }
    }

    final boolean isIndexed() {
        switch (this) {
        case REGISTER_IX_INDIRECT:
        case REGISTER_IY_INDIRECT:
        case REGISTER_IX_INDEXED:
        case REGISTER_IY_INDEXED:
            return true;

        default:
            return false;
        }
    }

}
