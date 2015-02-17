package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.reasm.AssemblyBuilder;
import org.reasm.source.SourceNode;

/**
 * Provides the implementation for assembling the various {@link SourceNode} subclasses defined in the
 * <code>org.reasm.z80.source</code> package.
 *
 * @author Francis Gagné
 */
public final class SourceNodesImpl {

    /**
     * Assembles a directive that delimits a block.
     *
     * @param builder
     *            an assembly builder
     * @throws IOException
     *             an I/O exception occurred while assembling the directive
     */
    public static void assembleBlockDirectiveLine(@Nonnull AssemblyBuilder builder) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>DO</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleDoBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>FOR</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleForBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles an <code>IF</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleIfBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a logical line.
     *
     * @param builder
     *            an assembly builder
     * @throws IOException
     *             an I/O exception occurred while assembling the logical line
     */
    public static void assembleLogicalLine(@Nonnull AssemblyBuilder builder) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>MACRO</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleMacroBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>NAMESPACE</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleNamespaceBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>PHASE</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assemblePhaseBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>REPT</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleReptBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>REPT</code> block body.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleReptBody(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>TRANSFORM</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleTransformBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Assembles a <code>WHILE</code> block.
     *
     * @param builder
     *            an assembly builder
     */
    public static void assembleWhileBlock(@Nonnull AssemblyBuilder builder) {
        throw new RuntimeException("Not implemented");
    }

    // This class is not meant to be instantiated.
    private SourceNodesImpl() {
    }

}
