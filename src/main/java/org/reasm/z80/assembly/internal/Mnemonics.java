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
        // TODO

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
