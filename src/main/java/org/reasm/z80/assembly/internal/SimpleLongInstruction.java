package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * Instruction that takes no operands and is encoded as two bytes, the first of which is <code>0xED</code>.
 *
 * @author Francis Gagné
 */
class SimpleLongInstruction extends Mnemonic {

    @Nonnull
    static final SimpleLongInstruction CPD = new SimpleLongInstruction(0xA9);
    @Nonnull
    static final SimpleLongInstruction CPDR = new SimpleLongInstruction(0xB9);
    @Nonnull
    static final SimpleLongInstruction CPI = new SimpleLongInstruction(0xA1);
    @Nonnull
    static final SimpleLongInstruction CPIR = new SimpleLongInstruction(0xB1);
    @Nonnull
    static final SimpleLongInstruction IND = new SimpleLongInstruction(0xAA);
    @Nonnull
    static final SimpleLongInstruction INDR = new SimpleLongInstruction(0xBA);
    @Nonnull
    static final SimpleLongInstruction INI = new SimpleLongInstruction(0xA2);
    @Nonnull
    static final SimpleLongInstruction INIR = new SimpleLongInstruction(0xB2);
    @Nonnull
    static final SimpleLongInstruction LDD = new SimpleLongInstruction(0xA8);
    @Nonnull
    static final SimpleLongInstruction LDDR = new SimpleLongInstruction(0xB8);
    @Nonnull
    static final SimpleLongInstruction LDI = new SimpleLongInstruction(0xA0);
    @Nonnull
    static final SimpleLongInstruction LDIR = new SimpleLongInstruction(0xB0);
    @Nonnull
    static final SimpleLongInstruction NEG = new SimpleLongInstruction(0x44);
    @Nonnull
    static final SimpleLongInstruction OTDR = new SimpleLongInstruction(0xBB);
    @Nonnull
    static final SimpleLongInstruction OTIR = new SimpleLongInstruction(0xB3);
    @Nonnull
    static final SimpleLongInstruction OUTD = new SimpleLongInstruction(0xAB);
    @Nonnull
    static final SimpleLongInstruction OUTI = new SimpleLongInstruction(0xA3);
    @Nonnull
    static final SimpleLongInstruction RETI = new SimpleLongInstruction(0x4D);
    @Nonnull
    static final SimpleLongInstruction RETN = new SimpleLongInstruction(0x45);
    @Nonnull
    static final SimpleLongInstruction RLD = new SimpleLongInstruction(0x6F);
    @Nonnull
    static final SimpleLongInstruction RRD = new SimpleLongInstruction(0x67);

    private final int opcode;

    private SimpleLongInstruction(int opcode) {
        this.opcode = opcode;
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        context.requireNumberOfOperands(0);
        context.appendByte((byte) 0xED);
        context.appendByte((byte) this.opcode);
    }

}
