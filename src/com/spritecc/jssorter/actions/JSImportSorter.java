package com.spritecc.jssorter.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.*;

/**
 * jliu@indeed.com
 */
public class JSImportSorter extends AnAction {
    private final Comparator<String> IGNORE_OPEN_CURLY_STRING_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            final String tmp1 = renameAsItems(o1).replace("{ ", "");
            final String tmp2 = renameAsItems(o2).replace("{ ", "");
            return tmp1.compareTo(tmp2);
        }
    };
    private final Set<String> singleImports = new TreeSet<>(IGNORE_OPEN_CURLY_STRING_COMPARATOR);
    private final Set<String> multipleImports = new TreeSet<>(IGNORE_OPEN_CURLY_STRING_COMPARATOR);
    private final Set<String> allImports = new TreeSet<>();
    private final Set<String> noneImports = new TreeSet<>();
    int importStartLine = Integer.MAX_VALUE;
    int importEndLine = Integer.MIN_VALUE;

    @Override
    public void update(AnActionEvent e) {
        final Presentation presentation = e.getPresentation();
        final VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        presentation.setEnabled((file != null) && ("js".equals(file.getExtension())));
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            return;
        }

        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            return;
        }

        final Document document = editor.getDocument();
        final String[] lines = document.getText().split("\n");
        extractImportsAndSort(lines);
        replaceWithSortedImports(project, document);
    }

    private void replaceWithSortedImports(final Project project, final Document document) {
        if (importStartLine > importEndLine) {
            return;
        }
        final String sortedImports = getSortedImports();
        final int importStartOffset = document.getLineStartOffset(importStartLine);
        final int importEndOffset = document.getLineEndOffset(importEndLine);

        final Runnable runner = new Runnable() {
            @Override
            public void run() {
                document.replaceString(importStartOffset, importEndOffset, sortedImports);
            }
        };

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(runner);
                    }
                }, "Sort JS Imports", null);
            }
        });
    }

    private void init() {
        singleImports.clear();
        multipleImports.clear();
        allImports.clear();
        noneImports.clear();
        importStartLine = Integer.MAX_VALUE;
        importEndLine = Integer.MIN_VALUE;
    }

    void extractImportsAndSort(final String[] lines) {
        init();
        for (int i = 0; i < lines.length; ++i) {
            final String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("import")) {
                sortAndClassify(line);
                importEndLine = i;
                importStartLine = Math.min(i, importStartLine);
            } else if (importStartLine <= importEndLine) {
                break;
            }
        }
    }

    String getSortedImports() {
        final List<Set<String>> importSets = new ArrayList<>();
        importSets.add(noneImports);
        importSets.add(allImports);
        importSets.add(multipleImports);
        importSets.add(singleImports);
        final StringBuilder res = new StringBuilder();
        for (final Set<String> imports: importSets) {
            for (final String line: imports) {
                res.append(line);
                res.append("\n");
            }
        }
        if (res.length() > 0) {
            res.setLength(res.length() - 1);
        }

        return res.toString();
    }

    private void sortAndClassify(final String line) {
        if (isNoneImport(line)) {
            noneImports.add(line);
        } else if (isAllImport(line)) {
            allImports.add(line);
        } else if (isMultipleImport(line)) {
            final String sortedLine = sortItemsInMultipleImport(line);
            multipleImports.add(sortedLine);
        } else {
            // TODO maybe not true
            singleImports.add(line);
        }
    }

    boolean isNoneImport(final String line) {
        return !line.contains(" from ");
    }

    boolean isMultipleImport(final String line) {
        return line.contains(",");
    }

    boolean isAllImport(final String line) {
        return line.contains("*") && !isMultipleImport(line);
    }

    String sortItemsInMultipleImport(final String line) {
        if (line.contains("*")) {
            return line;
        }
        final int openCurly = line.indexOf("{");
        final int closeCurly = line.indexOf("}");
        final String[] items = line.substring(openCurly + 1, closeCurly).split(",");
        final Set<String> sortedItems = new TreeSet<>();
        for (String item: items) {
            sortedItems.add(item.trim());
        }
        final StringBuilder res = new StringBuilder();
        res.append(line.substring(0, openCurly));
        res.append("{ ");

        for (String item: sortedItems) {
            if (!item.isEmpty()) {
                res.append(item);
                res.append(", ");
            }
        }

        res.setLength(res.length() - 2);

        res.append(" }");
        res.append(line.substring(closeCurly + 1));
        return res.toString();
    }

    // Turn 'A as B' into 'B'
    String renameAsItems(final String src) {
        final String as = " as ";
        final int asIndex = src.indexOf(as);
        if (asIndex == -1) {
            return src;
        }

        final int dstWordEndIndex = src.indexOf(" ", asIndex + as.length());
        final String dstWord = src.substring(asIndex + as.length(), dstWordEndIndex);
        final int orgWordStartIndex = src.lastIndexOf(" ", asIndex - 1) + 1;

        final String newSrc = src.replace(src.substring(orgWordStartIndex, dstWordEndIndex), dstWord);
        return renameAsItems(newSrc);
    }

}
