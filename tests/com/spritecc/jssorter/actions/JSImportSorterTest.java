package com.spritecc.jssorter.actions;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * jliu@indeed.com
 */
public class JSImportSorterTest extends TestCase {
    private JSImportSorter sorter;

    @Override
    public void setUp() {
        sorter = new JSImportSorter();
    }

    @Test
    public void testIsNoneImport() {
        final String noneImport = "import 'abc'";
        assertTrue(sorter.isNoneImport(noneImport));
    }

    @Test
    public void testIsAllImport() {
        final String allImport = "import * from 'abd'";
        assertTrue(sorter.isAllImport(allImport));

        final String allImportWithAs = "import * as A from 'abc'";
        assertTrue(sorter.isAllImport(allImportWithAs));

        final String multipleImport = "import A, * as B from 'abc'";
        assertFalse(sorter.isAllImport(multipleImport));
    }

    @Test
    public void testIsMultipleImport() {
        final String multipleImport = "import React, { Component } from 'react';";
        assertTrue(sorter.isMultipleImport(multipleImport));

        final String singleImport = "import { Component } from 'react';";
        assertFalse(sorter.isMultipleImport(singleImport));
    }

    @Test
    public void testSortItemsInMultipleImport() {
        final String sorted = "import { ListView, RefreshControl, View } from 'react-native';";
        final String origin = "import { RefreshControl, ListView, View } from 'react-native';";
        assertEquals(sorted, sorter.sortItemsInMultipleImport(origin));

        final String originMixed = "import React, { PropTypes, Element, Component } from 'react';";
        final String sortedMixed = "import React, { Component, Element, PropTypes } from 'react';";
        assertEquals(sortedMixed, sorter.sortItemsInMultipleImport(originMixed));
    }

    @Test
    public void testExtractImportsAndSort() throws IOException {
        final String[] doc = Files.readAllLines(Paths.get("testData/file1.txt"), StandardCharsets.UTF_8).toArray(new String[]{});
        final String sortedImports = new String(Files.readAllBytes(Paths.get("testData/sfile1.txt")));
        sorter.extractImportsAndSort(doc);
        assertEquals(sortedImports, sorter.getSortedImports());
        assertEquals(2, sorter.importStartLine);
        assertEquals(5, sorter.importEndLine);
    }

    @Test
    public void testRenameAsItems() {
        final String src1 = "import A as B from C;";
        final String dst1 = sorter.renameAsItems(src1);
        assertEquals("import B from C;", dst1);

        final String src2 = "import { A as B, C as D } from E;";
        final String dst2 = sorter.renameAsItems(src2);
        assertEquals("import { B, D } from E;", dst2);
    }
}
