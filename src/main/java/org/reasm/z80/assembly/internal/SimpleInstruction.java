package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Instruction that takes no operands and is encoded as a single byte.
 *
 * @author Francis Gagné
 */
@Immutable
class SimpleInstruction extends Mnemonic {

    @Nonnull
    static final SimpleInstruction CCF = new SimpleInstruction(0x3F);
    @Nonnull
    static final SimpleInstruction CPL = new SimpleInstruction(0x2F);
    @Nonnull
    static final SimpleInstruction DAA = new SimpleInstruction(0x27);
    @Nonnull
    static final SimpleInstruction DI = new SimpleInstruction(0xF3);
    @Nonnull
    static final SimpleInstruction EI = new SimpleInstruction(0xFB);
    @Nonnull
    static final SimpleInstruction EXX = new SimpleInstruction(0xD9);
    @Nonnull
    static final SimpleInstruction HALT = new SimpleInstruction(0x76);
    @Nonnull
    static final SimpleInstruction NOP = new SimpleInstruction(0x00);
    @Nonnull
    static final SimpleInstruction RLA = new SimpleInstruction(0x17);
    @Nonnull
    static final SimpleInstruction RLCA = new SimpleInstruction(0x07);
    @Nonnull
    static final SimpleInstruction RRA = new SimpleInstruction(0x1F);
    @Nonnull
    static final SimpleInstruction RRCA = new SimpleInstruction(0x0F);
    @Nonnull
    static final SimpleInstruction SCF = new SimpleInstruction(0x37);

    private final int opcode;

    private SimpleInstruction(int opcode) {
        this.opcode = opcode;
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        context.requireNumberOfOperands(0);
        context.appendByte((byte) this.opcode);
    }

}
