package com.spritecc.jssorter.actions;

import com.spritecc.jssorter.utils.DocumentUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
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
        final SelectionModel selectionModel = editor.getSelectionModel();
        final int startLine = document.getLineNumber(selectionModel.getSelectionStart());
        final int endLine = document.getLineNumber(selectionModel.getSelectionEnd());

        if (startLine == endLine) {
            return;
        }

        final String[] lines = document.getText().split("\n");
        final String sortedLines = sortAndGetLines(lines, startLine, endLine);
        DocumentUtil.replaceWithText(project, document, sortedLines, startLine, endLine, "Sort selected lines");
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
}
