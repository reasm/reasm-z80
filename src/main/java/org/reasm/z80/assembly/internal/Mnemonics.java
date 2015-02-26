package org.reasm.z80.assembly.internal;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.reasm.Symbol;
import org.reasm.SymbolReference;
import org.reasm.SymbolResolutionFallback;

/**
 * Exposes constants for the instructions and directives supported by the Z80 Family assembler.
 *
 * @author Francis Gagné
 */
@SuppressWarnings("javadoc")
public final class Mnemonics {

    static final class MnemonicMap {

        private final Map<String, MnemonicSymbol> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        @CheckForNull
        MnemonicSymbol get(@Nonnull String mnemonicName) {
            return this.map.get(mnemonicName);
        }

        void put(@Nonnull String mnemonicName, @Nonnull Mnemonic mnemonicHandler) {
            this.map.put(mnemonicName, new MnemonicSymbol(mnemonicName, mnemonicHandler));
        }

    }

    public static final String ADC = "ADC";
    public static final String ADD = "ADD";
    public static final String AND = "AND";
    public static final String BIT = "BIT";
    public static final String CCF = "CCF";
    public static final String CP = "CP";
    public static final String CPD = "CPD";
    public static final String CPDR = "CPDR";
    public static final String CPI = "CPI";
    public static final String CPIR = "CPIR";
    public static final String CPL = "CPL";
    public static final String DAA = "DAA";
    public static final String DEC = "DEC";
    public static final String DI = "DI";
    public static final String EI = "EI";
    public static final String EX = "EX";
    public static final String EXX = "EXX";
    public static final String HALT = "HALT";
    public static final String IM = "IM";
    public static final String INC = "INC";
    public static final String IND = "IND";
    public static final String INDR = "INDR";
    public static final String INI = "INI";
    public static final String INIR = "INIR";
    public static final String LD = "LD";
    public static final String LDD = "LDD";
    public static final String LDDR = "LDDR";
    public static final String LDI = "LDI";
    public static final String LDIR = "LDIR";
    public static final String NEG = "NEG";
    public static final String NOP = "NOP";
    public static final String OR = "OR";
    public static final String OTDR = "OTDR";
    public static final String OTIR = "OTIR";
    public static final String OUTD = "OUTD";
    public static final String OUTI = "OUTI";
    public static final String POP = "POP";
    public static final String PUSH = "PUSH";
    public static final String RES = "RES";
    public static final String RETI = "RETI";
    public static final String RETN = "RETN";
    public static final String RL = "RL";
    public static final String RLA = "RLA";
    public static final String RLC = "RLC";
    public static final String RLCA = "RLCA";
    public static final String RLD = "RLD";
    public static final String RR = "RR";
    public static final String RRA = "RRA";
    public static final String RRC = "RRC";
    public static final String RRCA = "RRCA";
    public static final String RRD = "RRD";
    public static final String SBC = "SBC";
    public static final String SCF = "SCF";
    public static final String SET = "SET";
    public static final String SLA = "SLA";
    public static final String SRA = "SRA";
    public static final String SRL = "SRL";
    public static final String SUB = "SUB";
    public static final String XOR = "XOR";

    public static final String DEPHASE = "DEPHASE";
    public static final String DO = "DO";
    public static final String ELSE = "ELSE";
    public static final String ELSEIF = "ELSEIF";
    public static final String ENDIF = "ENDIF";
    public static final String ENDM = "ENDM";
    public static final String ENDNS = "ENDNS";
    public static final String ENDR = "ENDR";
    public static final String ENDTRANSFORM = "ENDTRANSFORM";
    public static final String ENDW = "ENDW";
    public static final String FOR = "FOR";
    public static final String IF = "IF";
    public static final String MACRO = "MACRO";
    public static final String NAMESPACE = "NAMESPACE";
    public static final String NEXT = "NEXT";
    public static final String PHASE = "PHASE";
    public static final String REPT = "REPT";
    public static final String TRANSFORM = "TRANSFORM";
    public static final String UNTIL = "UNTIL";
    public static final String WHILE = "WHILE";

    @Nonnull
    static final MnemonicMap MAP;
    @Nonnull
    static final SymbolResolutionFallback SYMBOL_RESOLUTION_FALLBACK;

    static {
        final MnemonicMap map = new MnemonicMap();

        // Put the instructions in the dispatch map.
        map.put(ADC, AdcSbcInstruction.ADC);
        map.put(ADD, AddInstruction.ADD);
        map.put(AND, BinaryArithmeticLogicalInstruction.AND);
        map.put(BIT, BitManipulationInstruction.BIT);
        map.put(CCF, SimpleInstruction.CCF);
        map.put(CP, BinaryArithmeticLogicalInstruction.CP);
        map.put(CPD, SimpleLongInstruction.CPD);
        map.put(CPDR, SimpleLongInstruction.CPDR);
        map.put(CPI, SimpleLongInstruction.CPI);
        map.put(CPIR, SimpleLongInstruction.CPIR);
        map.put(CPL, SimpleInstruction.CPL);
        map.put(DAA, SimpleInstruction.DAA);
        map.put(DEC, IncDecInstruction.DEC);
        map.put(DI, SimpleInstruction.DI);
        map.put(EI, SimpleInstruction.EI);
        map.put(EX, ExInstruction.EX);
        map.put(EXX, SimpleInstruction.EXX);
        map.put(HALT, SimpleInstruction.HALT);
        map.put(IM, ImInstruction.IM);
        map.put(INC, IncDecInstruction.INC);
        map.put(IND, SimpleLongInstruction.IND);
        map.put(INDR, SimpleLongInstruction.INDR);
        map.put(INI, SimpleLongInstruction.INI);
        map.put(INIR, SimpleLongInstruction.INIR);
        map.put(LD, LdInstruction.LD);
        map.put(LDD, SimpleLongInstruction.LDD);
        map.put(LDDR, SimpleLongInstruction.LDDR);
        map.put(LDI, SimpleLongInstruction.LDI);
        map.put(LDIR, SimpleLongInstruction.LDIR);
        map.put(NEG, SimpleLongInstruction.NEG);
        map.put(NOP, SimpleInstruction.NOP);
        map.put(OR, BinaryArithmeticLogicalInstruction.OR);
        map.put(OTDR, SimpleLongInstruction.OTDR);
        map.put(OTIR, SimpleLongInstruction.OTIR);
        map.put(OUTD, SimpleLongInstruction.OUTD);
        map.put(OUTI, SimpleLongInstruction.OUTI);
        map.put(POP, PushPopInstruction.POP);
        map.put(PUSH, PushPopInstruction.PUSH);
        map.put(RES, BitManipulationInstruction.RES);
        map.put(RETI, SimpleLongInstruction.RETI);
        map.put(RETN, SimpleLongInstruction.RETN);
        map.put(RL, RotateShiftInstruction.RL);
        map.put(RLA, SimpleInstruction.RLA);
        map.put(RLC, RotateShiftInstruction.RLC);
        map.put(RLCA, SimpleInstruction.RLCA);
        map.put(RLD, SimpleLongInstruction.RLD);
        map.put(RR, RotateShiftInstruction.RR);
        map.put(RRA, SimpleInstruction.RRA);
        map.put(RRC, RotateShiftInstruction.RRC);
        map.put(RRCA, SimpleInstruction.RRCA);
        map.put(RRD, SimpleLongInstruction.RRD);
        map.put(SBC, AdcSbcInstruction.SBC);
        map.put(SCF, SimpleInstruction.SCF);
        map.put(SET, BitManipulationInstruction.SET);
        map.put(SLA, RotateShiftInstruction.SLA);
        map.put(SRA, RotateShiftInstruction.SRA);
        map.put(SRL, RotateShiftInstruction.SRL);
        map.put(SUB, BinaryArithmeticLogicalInstruction.SUB);
        map.put(XOR, BinaryArithmeticLogicalInstruction.XOR);

        // Put the directives in the dispatch map.
        // TODO

        MAP = map;

        SYMBOL_RESOLUTION_FALLBACK = new SymbolResolutionFallback() {
            @Override
            public Symbol resolve(SymbolReference symbolReference) {
                return MAP.get(symbolReference.getName());
            }
        };
    }

    // This class is not meant to be instantiated.
    private Mnemonics() {
    }

}
