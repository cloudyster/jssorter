package com.spritecc.jssorter.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.spritecc.jssorter.utils.DocumentUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author jliu@indeed.com
 */
public class JSSwitchSorter extends AnAction {
	@Override
	public void actionPerformed(AnActionEvent e) {
		final Project project = e.getProject();
		if (project == null) {
			return;
		}

		final Editor editor = FileEditorManager.getInstance(project)
				.getSelectedTextEditor();
		if (editor == null) {
			return;
		}

		final Document document = editor.getDocument();
		final SelectionModel selectionModel = editor.getSelectionModel();
		final int startLine = document.getLineNumber(selectionModel.getSelectionStart());
		final int endLine = document.getLineNumber(selectionModel.getSelectionEnd());
		if (startLine != endLine) {
			return;
		}

		final String[] lines = document.getText()
				.split("\n");

		final String switchLine = lines[startLine].trim();
		if (! switchLine.startsWith("switch") || ! switchLine.endsWith("{")) {
			return;
		}

		int brackets = 1;
		final Map<String, List<String>> cases = new TreeMap<>();
		String caseLine = "";
		int switchEndLine = startLine + 1;
		for (int i = startLine + 1; i < lines.length; ++ i) {
			final String trimLine = lines[i].trim();
			brackets = checkBrackets(trimLine, brackets);
			if (brackets == 0) {
				switchEndLine = i;
				break;
			}
			if (trimLine.startsWith("case") || trimLine.startsWith("default")) {
				caseLine = lines[i];
				if (! cases.containsKey(caseLine)) {
					cases.put(caseLine, new LinkedList<>());
				}
			}
			if (! caseLine.isEmpty()) {
				cases.get(caseLine)
						.add(lines[i]);
			}
		}

		if (brackets != 0 || cases.isEmpty() || (switchEndLine <= startLine + 1)) {
			return;
		}

		final String sortedCases = getSortedCases(cases);
		DocumentUtil.replaceWithText(project, document, sortedCases, startLine + 1, switchEndLine - 1,
				"Sort switch cases");
	}

	private String getSortedCases(final Map<String, List<String>> cases) {
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, List<String>> entry : cases.entrySet()) {
			for (final String s : entry.getValue()) {
				sb.append(s);
				sb.append("\n");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	private int checkBrackets(final String line, final int brackets) {
		int count = brackets;
		for (final char c : line.toCharArray()) {
			if (c == '{') {
				++ count;
			} else if (c == '}') {
				-- count;
			}
		}
		return count;
	}
}
