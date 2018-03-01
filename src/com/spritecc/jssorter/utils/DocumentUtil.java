package com.spritecc.jssorter.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;

/**
 * @author jliu@indeed.com
 */
public class DocumentUtil {
	public static void replaceWithText(final Project project, final Document document, final String text,
									   final int startLine, final int endLine, final String client) {
		if (text == null || (startLine > endLine)) {
			return;
		}

		final int lineStartOffset = document.getLineStartOffset(startLine);
		final int lineEndOffset = document.getLineEndOffset(endLine);

		final Runnable runner = () -> document.replaceString(lineStartOffset, lineEndOffset, text);

		ApplicationManager.getApplication()
				.invokeLater(() -> CommandProcessor.getInstance()
						.executeCommand(project, () -> ApplicationManager.getApplication()
								.runWriteAction(runner), client, null));
	}
}
