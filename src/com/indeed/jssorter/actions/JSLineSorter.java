package com.indeed.jssorter.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

import java.util.Arrays;

/**
 * @author jliu@indeed.com
 */
public class JSLineSorter extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        final Presentation presentation = e.getPresentation();
        final Project project = e.getProject();
        if (project == null) {
            return;
        }

        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            return;
        }

        final SelectionModel selectionModel = editor.getSelectionModel();
        final VisualPosition startPosition = selectionModel.getSelectionStartPosition();
        final VisualPosition endPosition = selectionModel.getSelectionEndPosition();
        presentation.setEnabled(startPosition != null && endPosition != null && (startPosition.getLine() != endPosition.getLine()));
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

        final SelectionModel selectionModel = editor.getSelectionModel();
        final VisualPosition startPosition = selectionModel.getSelectionStartPosition();
        final VisualPosition endPosition = selectionModel.getSelectionEndPosition();
        if (startPosition == null || endPosition == null) {
            return;
        }
        final int startLine = startPosition.getLine();
        final int endLine = endPosition.getLine();

        final String sortedLines = sortAndGetLines(lines, startLine, endLine);
        replaceWithSortedLines(project, document, sortedLines, startLine, endLine);
    }

    private String sortAndGetLines(final String[] lines, final int startLine, final int endLine) {
        if (startLine >= endLine || endLine >= lines.length) {
            return null;
        }

        Arrays.sort(lines, startLine, endLine + 1);

        final StringBuilder sb = new StringBuilder();
        for (int i = startLine; i <= endLine; ++i) {
            sb.append(lines[i]);
            sb.append("\n");
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private void replaceWithSortedLines(
            final Project project,
            final Document document,
            final String sortedLines,
            final int startLine,
            final int endLine
    ) {
        if (sortedLines == null || (startLine == endLine)) {
            return;
        }

        final int lineStartOffset = document.getLineStartOffset(startLine);
        final int lineEndOffset = document.getLineEndOffset(endLine);

        final Runnable runner = new Runnable() {
            @Override
            public void run() {
                document.replaceString(lineStartOffset, lineEndOffset, sortedLines);
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
                }, "Sort Lines", null);
            }
        });
    }
}
