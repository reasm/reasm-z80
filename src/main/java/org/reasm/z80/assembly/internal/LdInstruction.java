package org.reasm.z80.assembly.internal;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import org.reasm.commons.messages.AddressingModeNotAllowedHereErrorMessage;

import com.google.common.collect.ImmutableList;

/**
 * The <code>LD</code> instruction.
 *
 * @author Francis Gagné
 */
class LdInstruction extends Mnemonic {

    @Nonnull
    static final LdInstruction LD = new LdInstruction();

    @Nonnull
    private static final List<AddressingMode> REGISTER_PAIRS = ImmutableList.of(AddressingMode.REGISTER_BC,
            AddressingMode.REGISTER_DE, AddressingMode.REGISTER_HL, AddressingMode.REGISTER_SP);

    private LdInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(2)) {
            context.appendByte((byte) 0x00);
            return;
        }

        final EffectiveAddress ea0 = context.ea0;
        final EffectiveAddress ea1 = context.ea1;

        context.getEffectiveAddress(0, ea0);
        context.getEffectiveAddress(1, ea1);

        final AddressingMode addressingMode0 = ea0.addressingMode;
        final AddressingMode addressingMode1 = ea1.addressingMode;

        if (addressingMode0.isCommon()) {
            if (addressingMode1.isCommon()) {
                // LD r, r'
                // LD r, (HL)
                // LD (HL), r
                context.appendByte((byte) (0b01_000_000 | addressingMode0.value << 3 | addressingMode1.value));

                if (addressingMode0 == AddressingMode.REGISTER_HL_INDIRECT
                        && addressingMode1 == AddressingMode.REGISTER_HL_INDIRECT) {
                    // LD (HL), (HL) is not valid. (HALT takes its opcode.)
                    context.addMessage(new AddressingModeNotAllowedHereErrorMessage());
                }

                return;
            }

            if (addressingMode1.isIndexed()) {
                // LD r, (IX+d)
                // LD r, (IY+d)
                context.appendByte((byte) addressingMode1.value);
                context.appendByte((byte) (0b01_000_110 | addressingMode0.value << 3));
                context.appendByte(ea1.displacement);

                if (addressingMode0 == AddressingMode.REGISTER_HL_INDIRECT) {
                    // LD (HL), (IX+d) and LD (HL), (IY+d) are not valid.
                    context.addMessage(new AddressingModeNotAllowedHereErrorMessage());
                }

                return;
            }

            if (addressingMode1 == AddressingMode.IMMEDIATE) {
                // LD r, n
                // LD (HL), n
                context.appendByte((byte) (0b00_000_110 | addressingMode0.value << 3));
                context.appendByte(valueToByte(ea1.immediate, context));
                return;
            }
        }

        if (addressingMode0.isIndexed()) {
            if (addressingMode1.isCommon()) {
                // LD (IX+d), r
                // LD (IY+d), r
                context.appendByte((byte) addressingMode0.value);
                context.appendByte((byte) (0b01_110_000 | addressingMode1.value));
                context.appendByte(ea0.displacement);

                if (addressingMode1 == AddressingMode.REGISTER_HL_INDIRECT) {
                    // LD (IX+d), (HL) and LD (IY+d), (HL) are not valid.
                    context.addMessage(new AddressingModeNotAllowedHereErrorMessage());
                }

                return;
            }

            if (addressingMode1 == AddressingMode.IMMEDIATE) {
                // LD (IX+d), n
                // LD (IY+d), n
                context.appendByte((byte) addressingMode0.value);
                context.appendByte((byte) 0b00_110_110);
                context.appendByte(ea0.displacement);
                context.appendByte(valueToByte(ea1.immediate, context));
                return;
            }
        }

        if (addressingMode0 == AddressingMode.REGISTER_A) {
            if (addressingMode1 == AddressingMode.REGISTER_BC_INDIRECT) {
                // LD A, (BC)
                context.appendByte((byte) 0b00_001_010);
                return;
            }

            if (addressingMode1 == AddressingMode.REGISTER_DE_INDIRECT) {
                // LD A, (DE)
                context.appendByte((byte) 0b00_011_010);
                return;
            }

            if (addressingMode1 == AddressingMode.IMMEDIATE_INDIRECT) {
                // LD A, (nn)
                context.appendByte((byte) 0b00_111_010);
                context.appendWord(valueToWord(ea1.immediate, context));
                return;
            }

            if (addressingMode1 == AddressingMode.REGISTER_I) {
                // LD A, I
                context.appendByte((byte) 0b11_101_101);
                context.appendByte((byte) 0b01_010_111);
                return;
            }

            if (addressingMode1 == AddressingMode.REGISTER_R) {
                // LD A, R
                context.appendByte((byte) 0b11_101_101);
                context.appendByte((byte) 0b01_011_111);
                return;
            }
        }

        if (addressingMode1 == AddressingMode.REGISTER_A) {
            if (addressingMode0 == AddressingMode.REGISTER_BC_INDIRECT) {
                // LD (BC), A
                context.appendByte((byte) 0b00_000_010);
                return;
            }

            if (addressingMode0 == AddressingMode.REGISTER_DE_INDIRECT) {
                // LD (DE), A
                context.appendByte((byte) 0b00_010_010);
                return;
            }

            if (addressingMode0 == AddressingMode.IMMEDIATE_INDIRECT) {
                // LD (nn), A
                context.appendByte((byte) 0b00_110_010);
                context.appendWord(valueToWord(ea0.immediate, context));
                return;
            }

            if (addressingMode0 == AddressingMode.REGISTER_I) {
                // LD I, A
                context.appendByte((byte) 0b11_101_101);
                context.appendByte((byte) 0b01_000_111);
                return;
            }

            if (addressingMode0 == AddressingMode.REGISTER_R) {
                // LD R, A
                context.appendByte((byte) 0b11_101_101);
                context.appendByte((byte) 0b01_001_111);
                return;
            }
        }

        if (addressingMode1 == AddressingMode.IMMEDIATE) {
            final int registerPair = REGISTER_PAIRS.indexOf(addressingMode0);
            if (registerPair != -1) {
                // LD dd, nn
                context.appendByte((byte) (0b00_000_001 | registerPair << 4));
                context.appendWord(valueToWord(ea1.immediate, context));
                return;
            }

            if (addressingMode0.isIndex()) {
                // LD IX, nn
                // LD IY, nn
                context.appendByte((byte) addressingMode0.value);
                context.appendByte((byte) 0b00_100_001);
                context.appendWord(valueToWord(ea1.immediate, context));
                return;
            }
        }

        if (addressingMode1 == AddressingMode.IMMEDIATE_INDIRECT) {
            if (addressingMode0 == AddressingMode.REGISTER_HL) {
                // LD HL, (nn)
                context.appendByte((byte) 0b00_101_010);
                context.appendWord(valueToWord(ea1.immediate, context));
                return;
            }

            final int registerPair = REGISTER_PAIRS.indexOf(addressingMode0);
            if (registerPair != -1) {
                // LD dd, (nn)
                context.appendByte((byte) 0b11_101_101);
                context.appendByte((byte) (0b01_001_011 | registerPair << 4));
                context.appendWord(valueToWord(ea1.immediate, context));
                return;
            }

            if (addressingMode0.isIndex()) {
                // LD IX, (nn)
                // LD IY, (nn)
                context.appendByte((byte) addressingMode0.value);
                context.appendByte((byte) 0b00_101_010);
                context.appendWord(valueToWord(ea1.immediate, context));
                return;
            }
        }

        if (addressingMode0 == AddressingMode.IMMEDIATE_INDIRECT) {
            if (addressingMode1 == AddressingMode.REGISTER_HL) {
                // LD (nn), HL
                context.appendByte((byte) 0b00_100_010);
                context.appendWord(valueToWord(ea0.immediate, context));
                return;
            }

            final int registerPair = REGISTER_PAIRS.indexOf(addressingMode1);
            if (registerPair != -1) {
                // LD (nn), dd
                context.appendByte((byte) 0b11_101_101);
                context.appendByte((byte) (0b01_000_011 | registerPair << 4));
                context.appendWord(valueToWord(ea0.immediate, context));
                return;
            }

            if (addressingMode1.isIndex()) {
                // LD (nn), IX
                // LD (nn), IY
                context.appendByte((byte) addressingMode1.value);
                context.appendByte((byte) 0b00_100_010);
                context.appendWord(valueToWord(ea0.immediate, context));
                return;
            }
        }

        if (addressingMode0 == AddressingMode.REGISTER_SP) {
            if (addressingMode1 == AddressingMode.REGISTER_HL) {
                // LD SP, HL
                context.appendByte((byte) 0b11_111_001);
                return;
            }

            if (addressingMode1.isIndex()) {
                // LD SP, IX
                // LD SP, IY
                context.appendByte((byte) addressingMode1.value);
                context.appendByte((byte) 0b11_111_001);
                return;
            }
        }

        context.appendByte((byte) 0x00);
        context.addMessage(new AddressingModeNotAllowedHereErrorMessage());
    }

}
